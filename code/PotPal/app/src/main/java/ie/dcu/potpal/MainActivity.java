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

        ArrayList<Plant> plants = new ArrayList<>();
        plantsRecView = findViewById(R.id.plantsRecView);

        PlantAdapter adapter = new PlantAdapter(this);

        plants.add(new Plant("Foxglove", "Outside", "Flowering", "https://irishseedsavers.ie/wp-content/uploads/2022/11/Flowers_Foxglove-scaled-e1667476559896.jpg"));
        plants.add(new Plant("Christmas", "Inside", "Cactus", "https://hips.hearstapps.com/hmg-prod/images/christmas-cactus-1633368132.jpg?crop=1.00xw:0.701xh;0,0.138xh&resize=1200:*"));
        plants.add(new Plant("Poppy", "Outside", "Flowering", "https://irishseedsavers.ie/wp-content/uploads/2020/07/Poppy-Mix.jpg"));
        plants.add(new Plant("Ivy", "Inside", "Foliage", "https://gardenerspath.com/wp-content/uploads/2022/03/How-to-Grow-English-Ivy-Indoors-Feature.jpg"));
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

    private class PlantIdentifyTask extends AsyncTask<File, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(File... files) {
            if (files.length > 0) {
                // Get the first image file (assuming there's only one image)
                File imageFile = files[0];

                // Call the PlantIdentify method with the captured image
                return PlantIdentify.findPlant(imageFile);
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            // Handle the result in the UI thread (e.g., update UI components or log the result)
            Log.d("PlantIdentify", "Result: " + result.toString());
        }
    }


}