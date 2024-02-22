package ie.dcu.potpal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

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
        holder.commonNamesTextView.setText(plants.get(position).getCommonNames());
        holder.plantTypeTextView.setText(plants.get(position).getType());
        Glide.with(context).asBitmap().load(plants.get(position).getPhotoFilePath()).into(holder.plantImageView);
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
        private TextView commonNamesTextView;
        private TextView plantTypeTextView;
        private ImageView plantImageView;
        private ImageButton removeButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            plantImageView = itemView.findViewById(R.id.plantImageView);
            plantNameTextView = itemView.findViewById(R.id.plantNameTextView);
            plantTypeTextView = itemView.findViewById(R.id.plantTypeTextView);
            commonNamesTextView = itemView.findViewById(R.id.commonNamesTextView);
            removeButton = itemView.findViewById(R.id.removeButton);

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
    }

    private void removePlantFromSharedPreferences(Plant plant) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ChosenPlants", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(plant.getScientificName()); // Assuming latinName is the key used to store the plant
        editor.apply();
    }
}
