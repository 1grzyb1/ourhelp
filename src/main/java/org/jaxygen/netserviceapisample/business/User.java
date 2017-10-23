package org.jaxygen.netserviceapisample.business;

import java.io.StringReader;

public class User {
    public String name;
    public String surname;
    public String password;
    public String repassword;
    public String login;
    public String email;
    public int exp;
    public String fb;



    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }



    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int building;
    public int writing;
    public int learning;

    public int getBuilding() {
        return building;
    }

    public void setBuilding(int building) {
        this.building = building;
    }

    public int getWriting() {
        return writing;
    }

    public void setWriting(int writing) {
        this.writing = writing;
    }

    public int getLearning() {
        return learning;
    }

    public void setLearning(int learning) {
        this.learning = learning;
    }

    public String getFb() {
        return fb;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }
}
