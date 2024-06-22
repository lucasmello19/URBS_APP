package com.example.urbs.data.model;

import java.util.List;

public class CoordinatorResponse {
    private List<Double> coordinates;

    // Construtor padrão
    public CoordinatorResponse() {
    }

    // Construtor com coordenadas
    public CoordinatorResponse(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    // Getter para coordinates
    public List<Double> getCoordinates() {
        return coordinates;
    }

    // Setter para coordinates
    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}
