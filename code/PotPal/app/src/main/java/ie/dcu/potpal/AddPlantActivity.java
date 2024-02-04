package ie.dcu.potpal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddPlantActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imagePlant;
    private EditText editTextPlantName;
    private Spinner spinnerEnvironment;
    private Spinner spinnerPlantType;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        imagePlant = findViewById(R.id.imagePlant);
        editTextPlantName = findViewById(R.id.editTextPlantName);
        spinnerEnvironment = findViewById(R.id.spinnerEnvironment);
        spinnerPlantType = findViewById(R.id.spinnerPlantType);

        // Set up environment spinner
        ArrayAdapter<CharSequence> environmentAdapter = ArrayAdapter.createFromResource(
                this, R.array.environment_options, android.R.layout.simple_spinner_item);
        environmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEnvironment.setAdapter(environmentAdapter);

        // Set up plant type spinner
        ArrayAdapter<CharSequence> plantTypeAdapter = ArrayAdapter.createFromResource(
                this, R.array.plant_type_options, android.R.layout.simple_spinner_item);
        plantTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlantType.setAdapter(plantTypeAdapter);

        // Button to choose image
        Button btnChooseImage = findViewById(R.id.btnChooseImage);
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });

        // Button to save plant
        Button btnSavePlant = findViewById(R.id.btnSavePlant);
        btnSavePlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePlant();
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                // Set the selected image to ImageView
                imagePlant.setImageURI(imageUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void savePlant() {
        String plantName = editTextPlantName.getText().toString();
        String environment = spinnerEnvironment.getSelectedItem().toString();
        String type = spinnerPlantType.getSelectedItem().toString();

        // Save plant details in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putString("plantName", plantName)
                .putString("environment", environment)
                .putString("type", type)
                .putString("imageUri", imageUri.toString())
                .apply();

        finish();
    }

}