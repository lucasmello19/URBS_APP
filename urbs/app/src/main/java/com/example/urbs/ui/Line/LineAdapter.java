package com.example.urbs.ui.Line;

import android.content.Context;
import android.text.TextUtils;
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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_FAVORITE = 1;
    private static final int VIEW_TYPE_NORMAL = 2;

    private List<LineResponse> favoriteLines;
    private List<LineResponse> allLines;
    private List<LineResponse> filteredLines;
    private FavoritesManager favoritesManager;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(LineResponse line);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public LineAdapter(Context context, List<LineResponse> lineList) {
        this.favoritesManager = new FavoritesManager(context);
        this.allLines = new ArrayList<>(lineList);
        this.favoriteLines = new ArrayList<>();
        this.filteredLines = new ArrayList<>(lineList);
        separateLines(lineList);
    }

    private void separateLines(List<LineResponse> lineList) {
        favoriteLines.clear();
        for (LineResponse line : lineList) {
            if (favoritesManager.isFavorite(line.getCOD())) {
                favoriteLines.add(line);
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_HEADER) {
            View headerView = inflater.inflate(R.layout.item_line_header, parent, false);
            return new HeaderViewHolder(headerView);
        } else {
            View itemView = inflater.inflate(R.layout.item_line, parent, false);
            return new ViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            if (position == 0 && !favoriteLines.isEmpty()) {
                ((HeaderViewHolder) holder).headerText.setText("Linhas Favoritas");
            } else if (position == favoriteLines.size() + 1 && !filteredLines.isEmpty()) {
                ((HeaderViewHolder) holder).headerText.setText("Linhas Disponíveis");
            } else {
                ((HeaderViewHolder) holder).headerText.setText("");
            }
        } else {
            final LineResponse line;
            if (!favoriteLines.isEmpty() && position <= favoriteLines.size()) {
                line = favoriteLines.get(position - 1);
            } else {
                line = filteredLines.get(position - favoriteLines.size() - 2);
            }
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.textView.setText(line.getNOME());
            updateFavoriteIcon(viewHolder.favoriteButton, line.getCOD());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(line);
                    }
                }
            });
            viewHolder.favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleFavorite(line.getCOD());
                    updateFavoriteIcon(((ViewHolder) holder).favoriteButton, line.getCOD());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int favoriteCount = favoriteLines.isEmpty() ? 0 : favoriteLines.size() + 1; // +1 para o cabeçalho
        int otherCount = filteredLines.isEmpty() ? 0 : filteredLines.size() + 1; // +1 para o cabeçalho
        return favoriteCount + otherCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == favoriteLines.size() + 1) {
            return VIEW_TYPE_HEADER;
        } else if (position <= favoriteLines.size()) {
            return VIEW_TYPE_FAVORITE;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    private void updateFavoriteIcon(ImageButton button, String lineCode) {
        if (favoritesManager.isFavorite(lineCode)) {
            button.setImageResource(R.drawable.heart_on); // Ícone de favorito selecionado
        } else {
            button.setImageResource(R.drawable.heart_off); // Ícone de favorito não selecionado
        }
    }

    private void toggleFavorite(String lineCode) {
        if (favoritesManager.isFavorite(lineCode)) {
            favoritesManager.removeFavorite(lineCode);
        } else {
            favoritesManager.addFavorite(lineCode);
        }
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

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView headerText;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            headerText = itemView.findViewById(R.id.header_text);
        }
    }

    private int getActualPosition(int position) {
        int favoriteCount = favoriteLines.isEmpty() ? 0 : favoriteLines.size() + 1;
        return position - favoriteCount - 1;
    }

    public void filter(String query) {
        filteredLines.clear();
        if (TextUtils.isEmpty(query)) {
            filteredLines.addAll(allLines);
        } else {
            query = query.toLowerCase();
            for (LineResponse line : allLines) {
                if (line.getNOME().toLowerCase().contains(query)) {
                    filteredLines.add(line);
                }
            }
        }
        notifyDataSetChanged();
    }
}
