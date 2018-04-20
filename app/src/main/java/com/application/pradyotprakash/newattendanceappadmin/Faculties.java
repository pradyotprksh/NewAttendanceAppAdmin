package com.application.pradyotprakash.newattendanceappadmin;

public class Faculties extends FacultyId{

    String name;
    String id;
    String image;
    String branch;

    public Faculties(String name, String id, String image, String branch) {
        this.name = name;
        this.id = id;
        this.image = image;
        this.branch = branch;
    }

    public Faculties() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
