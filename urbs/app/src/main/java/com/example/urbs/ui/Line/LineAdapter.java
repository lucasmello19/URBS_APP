package com.example.urbs.ui.Line;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urbs.data.model.LineResponse;

import java.util.List;

public class LineAdapter extends RecyclerView.Adapter<LineAdapter.ViewHolder> {

    private List<LineResponse> lineList;
    private OnItemClickListener listener;

    // Interface para lidar com eventos de clique
    public interface OnItemClickListener {
        void onItemClick(LineResponse line);
    }

    // Método para definir o ouvinte de clique
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public LineAdapter(List<LineResponse> lineList) {
        this.lineList = lineList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        LineResponse line = lineList.get(position);
        holder.textView.setText(line.getNOME());

        // Define o clique no item da lista
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION && listener != null) {
                    // Obtém o objeto LineResponse associado ao clique e chama o método onItemClick do ouvinte
                    listener.onItemClick(lineList.get(adapterPosition));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lineList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
