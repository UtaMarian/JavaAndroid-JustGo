package com.example.login.Model;

import java.io.Serializable;

public class User implements Serializable {
    private  String id;
    private String email;
    private String name;
    private String token;

    public User(){}

    public User(String id,String email,String token,String name){
        this.email=email;
        this.token=token;
        this.name=name;
        this.id=id;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
