package com.example.anuj_ilm.connect_2;

/**
 * Created by Anuj-ILM on 1/6/2018.
 */

public class FacultyInfo {

    private String name , designation ,  imageName;

    public FacultyInfo(String name, String designation,  String imageName) {
        this.name = name;
        this.designation = designation;
        this.imageName = imageName ;
    }

    public FacultyInfo()
    {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getName() {
        return name;
    }

    public String getDesignation() {
        return designation;
    }

    public String getImageName() {
        return imageName;
    }
}
