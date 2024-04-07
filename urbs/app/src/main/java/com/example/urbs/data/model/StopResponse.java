package com.example.urbs.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class StopResponse implements Parcelable {
    @SerializedName("NOME")
    private String nome;

    @SerializedName("NUM")
    private String num;

    @SerializedName("SEQ")
    private String seq;

    @SerializedName("GRUPO")
    private String grupo;

    @SerializedName("SENTIDO")
    private String sentido;

    @SerializedName("TIPO")
    private String tipo;

    @SerializedName("ITINERARY_ID")
    private String itineraryId;

    @SerializedName("COORD")
    private List<Object> coord;

    public String getNome() {
        return nome;
    }

    public String getNum() {
        return num;
    }

    public String getSeq() {
        return seq;
    }

    public String getGrupo() {
        return grupo;
    }

    public String getSentido() {
        return sentido;
    }

    public String getTipo() {
        return tipo;
    }

    public String getItineraryId() {
        return itineraryId;
    }

    public List<Object> getCoord() {
        return coord;
    }

    // Constructor
    public StopResponse() {
        // Default constructor
    }

    protected StopResponse(Parcel in) {
        nome = in.readString();
        num = in.readString();
        seq = in.readString();
        grupo = in.readString();
        sentido = in.readString();
        tipo = in.readString();
        itineraryId = in.readString();
        double[] coordArray = new double[in.readInt()];
        in.readDoubleArray(coordArray);
        coord = new ArrayList<>(coordArray.length);
        for (double value : coordArray) {
            coord.add(value);
        }
    }

    public static final Creator<StopResponse> CREATOR = new Creator<StopResponse>() {
        @Override
        public StopResponse createFromParcel(Parcel in) {
            return new StopResponse(in);
        }

        @Override
        public StopResponse[] newArray(int size) {
            return new StopResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(num);
        dest.writeString(seq);
        dest.writeString(grupo);
        dest.writeString(sentido);
        dest.writeString(tipo);
        dest.writeString(itineraryId);
        dest.writeInt(coord.size());
        double[] coordArray = new double[coord.size()];
        for (int i = 0; i < coord.size(); i++) {
            coordArray[i] = (double) coord.get(i);
        }
        dest.writeDoubleArray(coordArray);
    }
}
