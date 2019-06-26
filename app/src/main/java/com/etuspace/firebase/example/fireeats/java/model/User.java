package com.etuspace.firebase.example.fireeats.java.model;



public class User {


    private String name;
    private String surname;
    private String email;
    private String position;
    private String group;
    private String firstlab;
    private String secondlab;
    private String thirdlab;
    private String fourlab;


    public User() {}

    public User(String name, String surname, String email, String position, String group, String firstlab,String secondlab,String thirdlab,String fourlab)
    {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.position = position;
        this.group = group;
        this.firstlab = firstlab;
        this.secondlab = secondlab;
        this.thirdlab = thirdlab;
        this.fourlab = fourlab;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPosition() {
        return position;
    }

    public String getGroup() {
        return group;
    }

    public String getFirstlab() {
        return firstlab;
    }

    public String getSecondlab() {
        return secondlab;
    }

    public String getThirdlab() {
        return thirdlab;
    }

    public String getFourlab() {
        return fourlab;
    }
}
