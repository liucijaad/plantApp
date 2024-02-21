package ie.dcu.potpal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private RecyclerView plantsRecView;
    private TextView textViewPlantName;
    private TextView textViewEnvironment;
    private TextView textViewPlantType;
    private TextView textViewImageUri;

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
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Handle the error
                ex.printStackTrace();
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this, "ie.dcu.potpal.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Image is stored in the photoFile, use it for identification
            executePlantIdentifyTask(photoFile);
        }
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
                Log.d("PlantIdentify", "No results available");
            }
        }
    }

    private ArrayList<Plant> getChosenPlants() {
        ArrayList<Plant> chosenPlants = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("ChosenPlants", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String latinName = entry.getKey();
            String[] plantDataArray = entry.getValue().toString().split("&");
            String type = plantDataArray[0];
            String sunlight = plantDataArray[1];
            String water = plantDataArray[2];
            String commonNames = plantDataArray[3];
            String photoFilePath = plantDataArray[4];

            // Create Plant object and add to the list
            chosenPlants.add(new Plant(latinName, type, sunlight, water, commonNames, photoFilePath));
        }

        return chosenPlants;
    }
}