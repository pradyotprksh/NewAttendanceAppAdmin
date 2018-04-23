package com.application.pradyotprakash.newattendanceappadmin;

public class ClassList extends ClassId {

    private String classValue;
    private String classTeacher;
    private String semester;
    private String branch;

    public ClassList(String classValue, String classTeacher, String semester, String branch) {
        this.classValue = classValue;
        this.classTeacher = classTeacher;
        this.semester = semester;
        this.branch = branch;
    }

    public ClassList() {
    }

    public String getClassValue() {
        return classValue;
    }

    public void setClassValue(String classValue) {
        this.classValue = classValue;
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
}
