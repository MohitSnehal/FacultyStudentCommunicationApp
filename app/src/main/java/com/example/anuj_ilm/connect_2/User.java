package com.example.anuj_ilm.connect_2;

/**
 * Created by Anuj-ILM on 12/19/2017.
 */


public class User {
    private String name, year,section, branch, profileimageurl;

    public User()
    {

    }

    public User(String name, String year, String section, String branch,String profileimageurl) {
        this.name = name;
        this.year = year;
        this.section = section;
        this.branch = branch;
        this.profileimageurl = profileimageurl;
    }

    public String getProfileimageurl() {
        return profileimageurl;
    }

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }

    public String getSection() {
        return section;
    }

    public String getBranch() {
        return branch;
    }
}
