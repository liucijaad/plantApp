package ie.dcu.potpal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PlantPagerAdapter extends RecyclerView.Adapter<PlantPagerAdapter.PlantViewHolder> {

    private Context context;
    private ArrayList<ArrayList<String>> plantInfo;
    private String photoFilePath;
    private String commonNames;

    public PlantPagerAdapter(Context context, ArrayList<String> firstPlant, ArrayList<String> secondPlant, ArrayList<String> thirdPlant, String photoFilePath) {
        this.context = context;
        this.plantInfo = new ArrayList<>();
        this.plantInfo.add(firstPlant);
        this.plantInfo.add(secondPlant);
        this.plantInfo.add(thirdPlant);
        this.photoFilePath = photoFilePath;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_plant_pager, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        ArrayList<String> currentPlant = plantInfo.get(position);
        holder.plantNameTextView.setText(currentPlant.get(0));

        // Join common names with comma if present
        if (currentPlant.size() > 4) {
            StringBuilder commonNamesStringBuilder = new StringBuilder();
            for (int i = 1; i < currentPlant.size() - 3; i++) {
                commonNamesStringBuilder.append(currentPlant.get(i));
                if (i < currentPlant.size() - 4) {
                    commonNamesStringBuilder.append(", ");
                }
            }
            commonNames = commonNamesStringBuilder.toString();
            holder.commonNamesTextView.setText(commonNames);
        } else if (currentPlant.size() == 4) {
            holder.commonNamesTextView.setText("No common names");
        } else {
            holder.commonNamesTextView.setText("");
        }

        if (position == 0) {
            holder.leftArrow.setVisibility(View.GONE);
            holder.rightArrow.setVisibility(View.VISIBLE);
        } else if (position == getItemCount() - 1) {
            holder.leftArrow.setVisibility(View.VISIBLE);
            holder.rightArrow.setVisibility(View.GONE);
        } else {
            holder.leftArrow.setVisibility(View.VISIBLE);
            holder.rightArrow.setVisibility(View.VISIBLE);
        }

        // Set click listener for the select plant button
        holder.selectPlantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start a new activity and pass the chosen plant information as extras
                Intent intent = new Intent(context, ChosenPlantActivity.class);
                intent.putExtra("photoFilePath", photoFilePath);
                intent.putExtra("commonNames", commonNames);
                intent.putStringArrayListExtra("ChosenPlant", currentPlant);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });

        ImageView plantImageView = holder.itemView.findViewById(R.id.plantImageView);
        String imageUrl = currentPlant.get(currentPlant.size() - 3);
        if (!imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .into(plantImageView);
        }
    }

    @Override
    public int getItemCount() {
        return plantInfo.size();
    }

    public static class PlantViewHolder extends RecyclerView.ViewHolder {
        TextView plantNameTextView;
        TextView commonNamesTextView;
        Button selectPlantButton;
        ImageView leftArrow;
        ImageView rightArrow;


        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            plantNameTextView = itemView.findViewById(R.id.plantNameTextView);
            commonNamesTextView = itemView.findViewById(R.id.commonNamesTextView);
            selectPlantButton = itemView.findViewById(R.id.selectPlantButton);
            leftArrow = itemView.findViewById(R.id.leftArrow);
            rightArrow = itemView.findViewById(R.id.rightArrow);
        }
    }
}