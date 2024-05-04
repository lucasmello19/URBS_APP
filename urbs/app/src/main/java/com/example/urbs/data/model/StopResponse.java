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

    @SerializedName("table")
    private List<TableEntry> tableEntries; // Lista para os dados da tabela

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

    public List<TableEntry> getTableEntries() {
        return tableEntries;
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
        tableEntries = in.createTypedArrayList(TableEntry.CREATOR); // Ler a lista de parcelables
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
        dest.writeTypedList(tableEntries); // Escrever a lista de parcelables
    }

    // Classe para representar os dados da tabela
    public static class TableEntry implements Parcelable {
        @SerializedName("HORA")
        private String hora;

        @SerializedName("PONTO")
        private String ponto;

        @SerializedName("DIA")
        private String dia;

        @SerializedName("TABELA")
        private String tabela;

        @SerializedName("ADAPT")
        private String adapt;

        @SerializedName("COD")
        private String cod;

        public String getHora() {
            return hora;
        }

        public void setHora(String hora) {
            this.hora = hora;
        }

        public String getPonto() {
            return ponto;
        }

        public void setPonto(String ponto) {
            this.ponto = ponto;
        }

        public String getDia() {
            return dia;
        }

        public void setDia(String dia) {
            this.dia = dia;
        }

        public String getTabela() {
            return tabela;
        }

        public void setTabela(String tabela) {
            this.tabela = tabela;
        }

        public String getAdapt() {
            return adapt;
        }

        public void setAdapt(String adapt) {
            this.adapt = adapt;
        }

        public String getCod() {
            return cod;
        }

        public void setCod(String cod) {
            this.cod = cod;
        }

        protected TableEntry(Parcel in) {
            hora = in.readString();
            ponto = in.readString();
            dia = in.readString();
            tabela = in.readString();
            adapt = in.readString();
            cod = in.readString();
        }

        public static final Creator<TableEntry> CREATOR = new Creator<TableEntry>() {
            @Override
            public TableEntry createFromParcel(Parcel in) {
                return new TableEntry(in);
            }

            @Override
            public TableEntry[] newArray(int size) {
                return new TableEntry[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(hora);
            dest.writeString(ponto);
            dest.writeString(dia);
            dest.writeString(tabela);
            dest.writeString(adapt);
            dest.writeString(cod);
        }
    }
}
