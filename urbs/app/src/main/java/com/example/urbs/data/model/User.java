package com.example.urbs.data.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("cellphone")
    private String cellphone;
    @SerializedName("password")
    private String password;
    @SerializedName("cpf")
    private String cpf;

    public User(String name, String email, String birthday, String cellphone, String password, String cpf) {
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.cellphone = cellphone;
        this.password = password;
        this.cpf = cpf;
    }

    // Getters e setters
}
