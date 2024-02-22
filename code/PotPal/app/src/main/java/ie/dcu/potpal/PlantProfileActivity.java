package ie.dcu.potpal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

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
        }
    }
}