package ie.dcu.potpal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.ViewHolder> {

    private ArrayList<Plant> plants = new ArrayList<>();
    private Context context;

    public PlantAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plant, parent, false);
        ViewHolder holder = new ViewHolder(view);

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                showConfirmationDialog(position);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.plantNameTextView.setText(plants.get(position).getScientificName());
        holder.waterTextView.setText(plants.get(position).getWater());
        holder.sunlightTextView.setText(plants.get(position).getSunlight());
        Glide.with(context).asBitmap().load(plants.get(position).getPhotoFilePath()).into(holder.plantImageView);
        holder.lastWateredTextView.setText("Last watered: " + plants.get(position).getLastWatered());
    }

    @Override
    public int getItemCount() {
        return plants.size();
    }

    public void setPlants(ArrayList<Plant> plants) {
        this.plants = plants;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView plantNameTextView;
        private TextView waterTextView;
        private TextView sunlightTextView;
        private ImageView plantImageView;
        private ImageButton removeButton;
        private ImageButton waterButton;
        private TextView lastWateredTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            plantImageView = itemView.findViewById(R.id.plantImageView);
            plantNameTextView = itemView.findViewById(R.id.plantNameTextView);
            sunlightTextView = itemView.findViewById(R.id.sunlightTextView);
            waterTextView = itemView.findViewById(R.id.waterTextView);
            removeButton = itemView.findViewById(R.id.removeButton);
            waterButton = itemView.findViewById(R.id.waterButton);
            lastWateredTextView = itemView.findViewById(R.id.lastWateredTextView);

            waterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    waterButton.setColorFilter(ContextCompat.getColor(context, R.color.blue));
                    updateLastWatered(position);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Plant plant = plants.get(position);
                    Intent intent = new Intent(context, PlantProfileActivity.class);
                    intent.putExtra("plant", plant);
                    context.startActivity(intent);
                }
            });
        }

        private void updateLastWatered(int position) {
            Plant plant = plants.get(position);
            SharedPreferences sharedPreferences = context.getSharedPreferences("ChosenPlants", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            // Update last watered time
            editor.putString(plant.getId() + "_lastWatered", getCurrentDateTime());
            String timestampsString = sharedPreferences.getString(plant.getId() + "_timestamps", "");
            StringBuilder newTimestampsString = new StringBuilder(timestampsString);
            if (!timestampsString.isEmpty()) {
                newTimestampsString.append("\n");
            }
            newTimestampsString.append(getCurrentDateTime());
            editor.putString(plant.getId() + "_timestamps", newTimestampsString.toString());
            editor.apply();
            lastWateredTextView.setText("Last watered: " + getCurrentDateTime());
        }

        private String getCurrentDateTime() {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            return sdf.format(new Date());
        }
    }




    private void showConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to remove " + plants.get(position).getScientificName() + " from your garden?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Plant removedPlant = plants.remove(position);
                notifyItemRemoved(position);
                removePlantFromSharedPreferences(removedPlant);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

        // Add this log statement
        Log.d("PlantAdapter", "Remove button clicked for plant at position: " + position);
    }

    private void removePlantFromSharedPreferences(Plant plant) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ChosenPlants", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Integer plantId = plant.getId();
        Log.d("PlantAdapter", "Removing plant with ID: " + plantId);
        editor.remove(plantId + "_id");
        editor.remove(plantId + "_latinName");
        editor.remove(plantId + "_type");
        editor.remove(plantId + "_sunlight");
        editor.remove(plantId + "_water");
        editor.remove(plantId + "_commonNames");
        editor.remove(plantId + "_photoFilePath");
        editor.remove(plantId + "_lastWatered");
        editor.apply();
    }
}
