package com.application.pradyotprakash.newattendanceappadmin;

/**
 * Created by pradyotprakash on 21/02/18.
 */

public class Students extends StudentId {

    String usn;
    String image;
    String branch;

    public Students(String name, String image, String branch) {
        this.usn = name;
        this.image = image;
        this.branch = branch;
    }

    public Students() {

    }

    public String getUsn() {
        return usn;
    }

    public void setUsn(String usn) {
        this.usn = usn;
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
