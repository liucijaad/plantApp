package ie.dcu.potpal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import android.widget.LinearLayout;

public class PlantProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_profile);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("plant")) {
            Plant plant = (Plant) intent.getSerializableExtra("plant");

            // Display plant details in the layout
            TextView plantNameTextView = findViewById(R.id.plantNameTextView);
            TextView waterTextView = findViewById(R.id.waterTextView);
            TextView sunlightTextView = findViewById(R.id.sunlightTextView);
            TextView typeTextView = findViewById(R.id.typeTextView);
            TextView commonNamesTextView = findViewById(R.id.commonNamesTextView);
            ImageView plantImageView = findViewById(R.id.plantImageView);

            plantNameTextView.setText(plant.getScientificName());
            waterTextView.setText(plant.getWater());
            sunlightTextView.setText(plant.getSunlight());
            typeTextView.setText(plant.getType());
            commonNamesTextView.setText(plant.getCommonNames());
            Glide.with(this).asBitmap().load(plant.getPhotoFilePath()).into(plantImageView);
            displayWateringTimestamps(plant.getId());
        }
    }

    private void displayWateringTimestamps(int plantId) {
        SharedPreferences sharedPreferences = getSharedPreferences("ChosenPlants", Context.MODE_PRIVATE);
        String timestampsString = sharedPreferences.getString(plantId + "_timestamps", "");


        if (!timestampsString.isEmpty()) {

            List<String> timestampsList = new ArrayList<>(Arrays.asList(timestampsString.split("&")));
            Collections.reverse(timestampsList);
            LinearLayout timestampsLayout = findViewById(R.id.wateringTimestampsLayout);

            // Add TextViews for each timestamp
            for (String timestamp : timestampsList) {
                TextView timestampTextView = new TextView(this);
                timestampTextView.setText(timestamp);
                timestampTextView.setPadding(0, 5, 0, 5);
                timestampsLayout.addView(timestampTextView);
            }
        }
    }
}