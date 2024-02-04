package ie.dcu.potpal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.plantNameTextView.setText(plants.get(position).getPlantName());
        holder.environmentTextView.setText(plants.get(position).getEnvironment());
        holder.plantTypeTextView.setText(plants.get(position).getType());
        Glide.with(context).asBitmap().load(plants.get(position).getImageUri()).into(holder.plantImageView);
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
        private TextView environmentTextView;
        private TextView plantTypeTextView;
        private ImageView plantImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            plantImageView = itemView.findViewById(R.id.plantImageView);
            plantNameTextView = itemView.findViewById(R.id.plantNameTextView);
            plantTypeTextView = itemView.findViewById(R.id.plantTypeTextView);
            environmentTextView = itemView.findViewById(R.id.environmentTextView);

        }
    }
}
