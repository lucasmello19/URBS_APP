package com.example.urbs.ui.Line;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class FavoritesManager {
    private static final String PREFS_NAME = "favorites_prefs";
    private static final String FAVORITES_KEY = "favorites";

    private SharedPreferences sharedPreferences;

    public FavoritesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void addFavorite(String lineId) {
        Set<String> favorites = getFavorites();
        favorites.add(lineId);
        saveFavorites(favorites);
    }

    public void removeFavorite(String lineId) {
        Set<String> favorites = getFavorites();
        favorites.remove(lineId);
        saveFavorites(favorites);
    }

    public boolean isFavorite(String lineId) {
        Set<String> favorites = getFavorites();
        return favorites.contains(lineId);
    }

    private Set<String> getFavorites() {
        // Retorna um Set<String> vazio se não houver favoritos salvos
        Set<String> favorites = sharedPreferences.getStringSet(FAVORITES_KEY, new HashSet<String>());
        return new HashSet<>(favorites);
    }

    private void saveFavorites(Set<String> favorites) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(FAVORITES_KEY, favorites);
        editor.apply();
    }
}
