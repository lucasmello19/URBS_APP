package com.example.urbs.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LineResponse implements Parcelable {
    private String COD;
    private String NOME;
    private String SOMENTE_CARTAO;
    private String CATEGORIA_SERVICO;
    private String NOME_COR;

    // Construtor vazio
    public LineResponse() {
    }

    // Getters e Setters

    public String getCOD() {
        return COD;
    }

    public void setCOD(String COD) {
        this.COD = COD;
    }

    public String getNOME() {
        return NOME;
    }

    public void setNOME(String NOME) {
        this.NOME = NOME;
    }

    public String getSOMENTE_CARTAO() {
        return SOMENTE_CARTAO;
    }

    public void setSOMENTE_CARTAO(String SOMENTE_CARTAO) {
        this.SOMENTE_CARTAO = SOMENTE_CARTAO;
    }

    public String getCATEGORIA_SERVICO() {
        return CATEGORIA_SERVICO;
    }

    public void setCATEGORIA_SERVICO(String CATEGORIA_SERVICO) {
        this.CATEGORIA_SERVICO = CATEGORIA_SERVICO;
    }

    public String getNOME_COR() {
        return NOME_COR;
    }

    public void setNOME_COR(String NOME_COR) {
        this.NOME_COR = NOME_COR;
    }

    // Implementação dos métodos da interface Parcelable

    protected LineResponse(Parcel in) {
        COD = in.readString();
        NOME = in.readString();
        SOMENTE_CARTAO = in.readString();
        CATEGORIA_SERVICO = in.readString();
        NOME_COR = in.readString();
    }

    public static final Creator<LineResponse> CREATOR = new Creator<LineResponse>() {
        @Override
        public LineResponse createFromParcel(Parcel in) {
            return new LineResponse(in);
        }

        @Override
        public LineResponse[] newArray(int size) {
            return new LineResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(COD);
        dest.writeString(NOME);
        dest.writeString(SOMENTE_CARTAO);
        dest.writeString(CATEGORIA_SERVICO);
        dest.writeString(NOME_COR);
    }
}
