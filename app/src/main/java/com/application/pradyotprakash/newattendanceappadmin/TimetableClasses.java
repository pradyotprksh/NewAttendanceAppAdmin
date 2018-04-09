package com.application.pradyotprakash.newattendanceappadmin;

/**
 * Created by pradyotprakash on 25/02/18.
 */

public class TimetableClasses {
    String classValue, classTeacher, semester, branch;

    public TimetableClasses(String classValue, String classTeacher, String semester, String branch) {
        this.classValue = classValue;
        this.classTeacher = classTeacher;
        this.semester = semester;
        this.branch = branch;
    }

    public TimetableClasses() {
    }

    public String getClassTeacher() {
        return classTeacher;
    }

    public void setClassTeacher(String classTeacher) {
        this.classTeacher = classTeacher;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
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
}
