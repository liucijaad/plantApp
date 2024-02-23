package ie.dcu.potpal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;
    private RecyclerView plantsRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Plant> plants = getChosenPlants();

        plantsRecView = findViewById(R.id.plantsRecView);

        PlantAdapter adapter = new PlantAdapter(this);

        adapter.setPlants(plants);

        plantsRecView.setAdapter(adapter);
        plantsRecView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fabAddPlant = findViewById(R.id.fabAddPlant);
        fabAddPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
    }

    protected void onResume() {
        super.onResume();
        // Reload the list of chosen plants when MainActivity is resumed
        ArrayList<Plant> plants = getChosenPlants();
        updatePlantList(plants);

        if (plants.isEmpty()) {
            // Show the placeholder if the list of plants is empty
            findViewById(R.id.placeholderLayout).setVisibility(View.VISIBLE);
            plantsRecView.setVisibility(View.GONE);
        } else {
            // Hide the placeholder and display the RecyclerView if the list of plants is not empty
            findViewById(R.id.placeholderLayout).setVisibility(View.GONE);
            plantsRecView.setVisibility(View.VISIBLE);
        }
    }

    private void updatePlantList(ArrayList<Plant> plants) {
        PlantAdapter adapter = new PlantAdapter(this);
        adapter.setPlants(plants);
        plantsRecView.setAdapter(adapter);
        plantsRecView.setLayoutManager(new LinearLayoutManager(this));
    }

    private File photoFile;

    private void takePicture() {
        // Create an AlertDialog.Builder instance
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Identify a plant");
        builder.setMessage("Would you like to take a new picture or choose one from your gallery?");

        // Add the buttons for each option
        builder.setPositiveButton("Take a New Picture", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dispatchTakePictureIntent(); // Start the process to take a new picture
            }
        });
        builder.setNegativeButton("Choose from Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dispatchChooseFromGalleryIntent(); // Start the process to choose from gallery
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this, "ie.dcu.potpal.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void dispatchChooseFromGalleryIntent() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhotoIntent.setType("image/*");
        startActivityForResult(pickPhotoIntent, REQUEST_PICK_IMAGE);
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                executePlantIdentifyTask(photoFile);
            } else if (requestCode == REQUEST_PICK_IMAGE) {
                if (data != null) {
                    Uri selectedImageUri = data.getData();
                    String imagePath = getRealPathFromURI(selectedImageUri);
                    if (imagePath != null) {
                        photoFile = new File(imagePath);
                        executePlantIdentifyTask(photoFile);
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "Failed to load image", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return null;
    }

    private void executePlantIdentifyTask(File imageFile) {
        new PlantIdentifyTask().execute(imageFile);
    }

    private class PlantIdentifyTask extends AsyncTask<File, Void, List<List<String>>> {

        @Override
        protected List<List<String>> doInBackground(File... files) {
            if (files.length > 0) {
                File imageFile = files[0];
                return PlantIdentify.findPlantNames(imageFile);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<List<String>> results) {
            Log.d("PlantIdentify", "Results: " + results);

            if (results != null && !results.isEmpty()) {
                List<String> FirstPlant = results.get(0);
                List<String> SecondPlant = results.get(1);
                List<String> ThirdPlant = results.get(2);

                Intent intent = new Intent(MainActivity.this, AddPlantActivity.class);
                intent.putExtra("photoFilePath", photoFile.getAbsolutePath());
                intent.putStringArrayListExtra("FirstPlant", (ArrayList<String>) FirstPlant);
                intent.putStringArrayListExtra("SecondPlant", (ArrayList<String>) SecondPlant);
                intent.putStringArrayListExtra("ThirdPlant", (ArrayList<String>) ThirdPlant);
                startActivity(intent);
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Couldn't identify the plant. Please try again.", Snackbar.LENGTH_LONG)
                        .setDuration(7000) // Display the message for 7 seconds
                        .setAction("Retake Photo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                takePicture(); // Retake the photo if the user clicks on the action
                            }
                        })
                        .show();
            }
        }
    }

    private ArrayList<Plant> getChosenPlants() {
        ArrayList<Plant> chosenPlants = new ArrayList<>();
        Set<Integer> plantIds = new HashSet<>(); // Maintain a set of unique plant IDs

        SharedPreferences sharedPreferences = getSharedPreferences("ChosenPlants", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            // Extract plantId from the key
            int plantId = Integer.parseInt(entry.getKey().split("_")[0]);
            if (!plantIds.contains(plantId)) { // Check if the plant with the same ID has already been added
                Plant plant = getPlantFromSharedPreferences(plantId);
                if (plant != null) {
                    chosenPlants.add(plant);
                    plantIds.add(plantId); // Add the plant ID to the set
                }
            }
        }

        return chosenPlants;
    }

    private Plant getPlantFromSharedPreferences(int plantId) {
        SharedPreferences sharedPreferences = getSharedPreferences("ChosenPlants", Context.MODE_PRIVATE);
        Integer id = sharedPreferences.getInt(plantId + "_id", 0);
        String latinName = sharedPreferences.getString(plantId + "_latinName", "");
        String type = sharedPreferences.getString(plantId + "_type", "");
        String sunlight = sharedPreferences.getString(plantId + "_sunlight", "");
        String water = sharedPreferences.getString(plantId + "_water", "");
        String commonNames = sharedPreferences.getString(plantId + "_commonNames", "");
        String photoFilePath = sharedPreferences.getString(plantId + "_photoFilePath", "");
        String lastWatered = sharedPreferences.getString(plantId + "_lastWatered", "");

        // Check if any required data is missing
        if (!latinName.isEmpty() && !type.isEmpty() && !sunlight.isEmpty() && !water.isEmpty() && !photoFilePath.isEmpty()) {
            return new Plant(id, latinName, type, sunlight, water, commonNames, photoFilePath, lastWatered);
        } else {
            return null;
        }
    }
}