package com.example.urbs.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShapeResponse {
    @SerializedName("SHP")
    private String shp;

    @SerializedName("COD")
    private String cod;

    @SerializedName("COORD")
    private List<Double> coord;

    public String getShp() {
        return shp;
    }

    public void setShp(String shp) {
        this.shp = shp;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public List<Double> getCoord() {
        return coord;
    }

    public void setCoord(List<Double> coord) {
        this.coord = coord;
    }
}
