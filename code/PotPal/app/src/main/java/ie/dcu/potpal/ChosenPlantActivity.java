package ie.dcu.potpal;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
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
        String commonNames = getIntent().getStringExtra("commonNames");

        ImageView plantImageView = findViewById(R.id.plantImageView);
        TextView plantNameTextView = findViewById(R.id.chosenPlantNameTextView);
        TextView typeTextView = findViewById(R.id.typeTextView);
        TextView sunlightTextView = findViewById(R.id.sunlightTextView);
        TextView waterTextView = findViewById(R.id.waterTextView);
        TextView commonNamesTextView = findViewById(R.id.chosenCommonNamesTextView);
        SeekBar waterSeekBar = findViewById(R.id.waterSeekBar);
        SeekBar sunlightSeekBar = findViewById(R.id.sunlightSeekBar);
        Spinner plantTypeSpinner = findViewById(R.id.plantTypeSpinner);

        // Display other plant information
        if (chosenPlant != null && chosenPlant.size() > 0) {
            String latinName = chosenPlant.get(0);
            DatabaseConnector.copyDataBaseFromAssets(this);
            Log.d("LatinName", latinName);
            List<String> plantData = DatabaseConnector.fetchPlantData(latinName);
            Glide.with(this).load(photoFilePath).into(plantImageView);
            plantNameTextView.setText("Latin Name: " + latinName);
            if (plantData.size() > 1) {

                typeTextView.setText("Type: " + plantData.get(0));
                sunlightTextView.setText("Sunlight: " + plantData.get(1));
                waterTextView.setText("Water: " + plantData.get(2));
                commonNamesTextView.setText("Common Names: " + plantData.get(3));

                Button finishButton = findViewById(R.id.finishButton);
                finishButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        savePlantData(latinName, plantData, photoFilePath);
                        finish();
                    }
                });

            } else {
                typeTextView.setVisibility(View.GONE);
                commonNamesTextView.setVisibility(View.GONE);
                waterSeekBar.setVisibility(View.VISIBLE);
                sunlightSeekBar.setVisibility(View.VISIBLE);
                plantTypeSpinner.setVisibility(View.VISIBLE);
                String[] waterLevels = getResources().getStringArray(R.array.water_levels);
                String[] sunlightLevels = getResources().getStringArray(R.array.sunlight_levels);
                String[] plantTypes = getResources().getStringArray(R.array.plant_types);

                waterTextView.setText("Choose plant's water level!");
                waterSeekBar.setProgress(0);
                sunlightTextView.setText("Choose plant's sunlight needs!");
                sunlightSeekBar.setProgress(0);

                waterSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        // Update water level text view based on progress
                        if (fromUser) {
                            waterTextView.setText(waterLevels[progress]);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

                sunlightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        // Update water level text view based on progress
                        if (fromUser) {
                            sunlightTextView.setText(sunlightLevels[progress]);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

                // Set up the spinner for plant types
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, plantTypes);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                plantTypeSpinner.setAdapter(spinnerAdapter);

                Button finishButton = findViewById(R.id.finishButton);
                finishButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("XDD", "CLICKED");
                        plantData.add(plantTypeSpinner.getSelectedItem().toString());
                        plantData.add(sunlightLevels[sunlightSeekBar.getProgress()]);
                        plantData.add(waterLevels[waterSeekBar.getProgress()]);
                        plantData.add(commonNames);
                        Log.d("Type", plantData.get(0));
                        Log.d("Sunlight", plantData.get(1));
                        Log.d("Water", plantData.get(2));
                        Log.d("Common Names", plantData.get(3));
                        savePlantData(latinName, plantData, photoFilePath);
                        finish();
                    }
                });
            }
        }
    }

    private int generateUniqueId() {
        return (int) System.currentTimeMillis();
    }

    private void savePlantData(String latinName, List<String> plantData, String photoFilePath) {
        SharedPreferences sharedPreferences = getSharedPreferences("ChosenPlants", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int plantId = generateUniqueId();
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