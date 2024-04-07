package com.example.urbs.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class ShapeResponse implements Parcelable {

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

    // Métodos necessários para implementar Parcelable

    protected ShapeResponse(Parcel in) {
        shp = in.readString();
        cod = in.readString();
        coord = new ArrayList<>();
        in.readList(coord, Double.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shp);
        dest.writeString(cod);
        dest.writeList(coord);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShapeResponse> CREATOR = new Creator<ShapeResponse>() {
        @Override
        public ShapeResponse createFromParcel(Parcel in) {
            return new ShapeResponse(in);
        }

        @Override
        public ShapeResponse[] newArray(int size) {
            return new ShapeResponse[size];
        }
    };
}
