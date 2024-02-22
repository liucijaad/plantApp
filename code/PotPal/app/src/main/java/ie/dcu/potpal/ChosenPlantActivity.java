package ie.dcu.potpal;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ChosenPlantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_plant);

        // Get chosen plant information passed as extras
        ArrayList<String> chosenPlant = getIntent().getStringArrayListExtra("ChosenPlant");
        String photoFilePath = getIntent().getStringExtra("photoFilePath");

        ImageView plantImageView = findViewById(R.id.plantImageView);
        TextView plantNameTextView = findViewById(R.id.chosenPlantNameTextView);
        TextView typeTextView = findViewById(R.id.typeTextView);
        TextView sunlightTextView = findViewById(R.id.sunlightTextView);
        TextView waterTextView = findViewById(R.id.waterTextView);
        TextView commonNamesTextView = findViewById(R.id.chosenCommonNamesTextView);

        // Display other plant information
        if (chosenPlant != null && chosenPlant.size() > 0) {
            String latinName = chosenPlant.get(0);
            DatabaseConnector.copyDataBaseFromAssets(this);
            Log.d("LatinName", latinName);
            List<String> plantData = DatabaseConnector.fetchPlantData(latinName);
            Glide.with(this).load(photoFilePath).into(plantImageView);
            if (plantData.size() > 1) {
                // Generate a unique ID for the plant
                int plantId = generateUniqueId();

                // Save plant data to SharedPreferences with unique IDs
                savePlantData(plantId, latinName, plantData, photoFilePath);

                // Display plant information
                plantNameTextView.setText("Latin Name: " + latinName);
                typeTextView.setText("Type: " + plantData.get(0));
                sunlightTextView.setText("Sunlight: " + plantData.get(1));
                waterTextView.setText("Water: " + plantData.get(2));
                commonNamesTextView.setText("Common Names: " + plantData.get(3));
            } else {
                // Handle case where plant data is not available
                plantNameTextView.setText("We don't know this plant yet! Add it to our database :) (I'll do it on Wednesday)");
            }
        }

        Button finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private int generateUniqueId() {
        return (int) System.currentTimeMillis();
    }

    private void savePlantData(int plantId, String latinName, List<String> plantData, String photoFilePath) {
        SharedPreferences sharedPreferences = getSharedPreferences("ChosenPlants", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Save plant data to SharedPreferences with unique IDs
        editor.putInt(plantId + "_id", plantId);
        editor.putString(plantId + "_latinName", latinName);
        editor.putString(plantId + "_type", plantData.get(0));
        editor.putString(plantId + "_sunlight", plantData.get(1));
        editor.putString(plantId + "_water", plantData.get(2));
        editor.putString(plantId + "_commonNames", plantData.get(3));
        editor.putString(plantId + "_photoFilePath", photoFilePath);
        editor.putString(plantId + "_lastWatered", "Never");
        editor.apply();
    }
}