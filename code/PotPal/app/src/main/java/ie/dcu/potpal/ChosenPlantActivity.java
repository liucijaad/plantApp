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
            if (plantData.size() > 1) {
                SharedPreferences preferences = getSharedPreferences("PlantData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("LatinName", latinName);
                editor.putString("Type", plantData.get(0));
                editor.putString("Sunlight", plantData.get(1));
                editor.putString("Water", plantData.get(2));
                editor.putString("CommonNames", plantData.get(3));
                editor.apply();

                Glide.with(this).load(photoFilePath).into(plantImageView);
                plantNameTextView.setText("Latin Name: " + latinName);
                typeTextView.setText("Type: " + plantData.get(0));
                sunlightTextView.setText("Sunlight: " + plantData.get(1));
                waterTextView.setText("Water: " + plantData.get(2));
                commonNamesTextView.setText("Common Names: " + plantData.get(3));

                saveChosenPlant(latinName, plantData);
            } else {
                Glide.with(this).load(photoFilePath).into(plantImageView);
                plantNameTextView.setText("We don't know this plant yet! Add it to our database :) (I'll do it on wednesday)");
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

    private void saveChosenPlant(String latinName, List<String> plantData) {
        SharedPreferences sharedPreferences = getSharedPreferences("ChosenPlants", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String photoFilePath = getIntent().getStringExtra("photoFilePath");
        Log.d("PHOTO", photoFilePath);
        // Serialize the plantData list to a single string
        StringBuilder plantDataString = new StringBuilder();
        for (String data : plantData) {
            plantDataString.append(data).append("&");
        }
        plantDataString.append(photoFilePath).append("&");


        // Save the serialized plantData string with the Latin Name as the key
        editor.putString(latinName, plantDataString.toString());
        editor.apply();
    }
}