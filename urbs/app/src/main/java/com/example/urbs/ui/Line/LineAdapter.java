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

import java.util.ArrayList;
import java.util.List;

public class LineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_FAVORITE = 1;
    private static final int VIEW_TYPE_NORMAL = 2;

    private List<LineResponse> favoriteLines;
    private List<LineResponse> otherLines;
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
        separateLines(lineList);
    }

    private void separateLines(List<LineResponse> lineList) {
        favoriteLines = new ArrayList<>();
        otherLines = new ArrayList<>();
        for (LineResponse line : lineList) {
            if (favoritesManager.isFavorite(line.getCOD())) {
                favoriteLines.add(line);
            } else {
                otherLines.add(line);
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
            } else if (position == favoriteLines.size() + 1 && !otherLines.isEmpty()) {
                ((HeaderViewHolder) holder).headerText.setText("Linhas Disponíveis");
            } else {
                ((HeaderViewHolder) holder).headerText.setText("");
            }
        } else {
            final LineResponse line;
            if (!favoriteLines.isEmpty() && position <= favoriteLines.size()) {
                line = favoriteLines.get(position - 1);
            } else {
                line = otherLines.get(position - favoriteLines.size() - 2);
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
        int favoriteCount = favoriteLines.isEmpty() ? 0 : favoriteLines.size() + 1; // +1 for header
        int otherCount = otherLines.isEmpty() ? 0 : otherLines.size() + 1; // +1 for header
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
            button.setImageResource(R.drawable.heart_on); // Selected favorite icon
        } else {
            button.setImageResource(R.drawable.heart_off); // Unselected favorite icon
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
}
