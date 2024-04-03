package com.example.urbs.data.model;

public class LineResponse {
    private String COD;
    private String NOME;
    private String SOMENTE_CARTAO;
    private String CATEGORIA_SERVICO;
    private String NOME_COR;

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
}
