package ie.dcu.potpal;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;

public class AddPlantActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private PlantPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        String photoFilePath = getIntent().getStringExtra("photoFilePath");

        // Get plant information passed as extras
        ArrayList<String> firstPlant = getIntent().getStringArrayListExtra("FirstPlant");
        ArrayList<String> secondPlant = getIntent().getStringArrayListExtra("SecondPlant");
        ArrayList<String> thirdPlant = getIntent().getStringArrayListExtra("ThirdPlant");

        // Initialize ViewPager2 and set its adapter
        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new PlantPagerAdapter(this, firstPlant, secondPlant, thirdPlant, photoFilePath);
        viewPager.setAdapter(pagerAdapter);
    }
}