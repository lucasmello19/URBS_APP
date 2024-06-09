package com.example.urbs.ui.Line;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urbs.data.model.LineResponse;
import com.example.urbs.ui.Line.FavoritesManager;
import com.example.urbs.R;

import java.util.List;

public class LineAdapter extends RecyclerView.Adapter<LineAdapter.ViewHolder> {

    private List<LineResponse> lineList;
    private OnItemClickListener listener;
    private FavoritesManager favoritesManager;

    // Interface para lidar com eventos de clique
    public interface OnItemClickListener {
        void onItemClick(LineResponse line);
    }

    // Método para definir o ouvinte de clique
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public LineAdapter(Context context, List<LineResponse> lineList) {
        this.lineList = lineList;
        this.favoritesManager = new FavoritesManager(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_line, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        LineResponse line = lineList.get(position);
        holder.textView.setText(line.getNOME());

        // Atualiza o ícone de favorito
        updateFavoriteIcon(holder.favoriteButton, favoritesManager.isFavorite(line.getCOD()));

        // Define o clique no item da lista
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(lineList.get(adapterPosition));
                }
            }
        });

        // Define o clique no botão de favorito
        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    LineResponse selectedLine = lineList.get(adapterPosition);
                    if (favoritesManager.isFavorite(selectedLine.getCOD())) {
                        favoritesManager.removeFavorite(selectedLine.getCOD());
                    } else {
                        favoritesManager.addFavorite(selectedLine.getCOD());
                    }
                    updateFavoriteIcon(holder.favoriteButton, favoritesManager.isFavorite(selectedLine.getCOD()));
                }
            }
        });
    }

    private void updateFavoriteIcon(ImageButton button, boolean isFavorite) {
        if (isFavorite) {
            button.setImageResource(R.drawable.heart_on); // Ícone de favorito selecionado
        } else {
            button.setImageResource(R.drawable.heart_off); // Ícone de favorito não selecionado
        }
    }

    @Override
    public int getItemCount() {
        return lineList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageButton favoriteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text1);
            favoriteButton = itemView.findViewById(R.id.favorite_button);
        }
    }
}
