package com.application.pradyotprakash.newattendanceappadmin;

public class ClassTeacher extends FacultyId{

    private String name, image, branch, classTeacherOf, classTeacherValue, id;

    public ClassTeacher() {
    }

    public ClassTeacher(String name, String image, String branch, String classTeacherOf, String classTeacherValue, String id) {
        this.name = name;
        this.image = image;
        this.branch = branch;
        this.classTeacherOf = classTeacherOf;
        this.classTeacherValue = classTeacherValue;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getClassTeacherOf() {
        return classTeacherOf;
    }

    public void setClassTeacherOf(String classTeacherOf) {
        this.classTeacherOf = classTeacherOf;
    }

    public String getClassTeacherValue() {
        return classTeacherValue;
    }

    public void setClassTeacherValue(String classTeacherValue) {
        this.classTeacherValue = classTeacherValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
