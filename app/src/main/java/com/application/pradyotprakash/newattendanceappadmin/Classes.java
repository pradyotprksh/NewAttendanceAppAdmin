package com.application.pradyotprakash.newattendanceappadmin;

/**
 * Created by pradyotprakash on 22/02/18.
 */

public class Classes {
    String classValue, semester, branch;

    public Classes(String classValue, String semester, String branch) {
        this.classValue = classValue;
        this.semester = semester;
        this.branch = branch;
    }

    public Classes() {

    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getClassValue() {
        return classValue;
    }

    public void setClassValue(String classValue) {
        this.classValue = classValue;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
