package com.example.urbs.data.model;

import com.google.gson.annotations.SerializedName;

public class VehicleResponse {
    @SerializedName("COD")
    private String cod;

    @SerializedName("REFRESH")
    private String refresh;

    @SerializedName("LAT")
    private String lat;

    @SerializedName("LON")
    private String lon;

    @SerializedName("CODIGOLINHA")
    private String codigoLinha;

    @SerializedName("ADAPT")
    private String adapt;

    @SerializedName("TIPO_VEIC")
    private String tipoVeiculo;

    @SerializedName("TABELA")
    private String tabela;

    @SerializedName("SITUACAO")
    private String situacao;

    @SerializedName("SITUACAO2")
    private String situacao2;

    @SerializedName("SENT")
    private String sent;

    @SerializedName("TCOUNT")
    private int tCount;

    @SerializedName("SENTIDO")
    private String sentido;

    // Getters e Setters
    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getCodigoLinha() {
        return codigoLinha;
    }

    public void setCodigoLinha(String codigoLinha) {
        this.codigoLinha = codigoLinha;
    }

    public String getAdapt() {
        return adapt;
    }

    public void setAdapt(String adapt) {
        this.adapt = adapt;
    }

    public String getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setTipoVeiculo(String tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }

    public String getTabela() {
        return tabela;
    }

    public void setTabela(String tabela) {
        this.tabela = tabela;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getSituacao2() {
        return situacao2;
    }

    public void setSituacao2(String situacao2) {
        this.situacao2 = situacao2;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

    public int gettCount() {
        return tCount;
    }

    public void settCount(int tCount) {
        this.tCount = tCount;
    }

    public String getSentido() {
        return sentido;
    }

    public void setSentido(String sentido) {
        this.sentido = sentido;
    }
}
