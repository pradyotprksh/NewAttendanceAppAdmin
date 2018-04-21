package com.application.pradyotprakash.newattendanceappadmin;

public class FacultySubjects {

    private String branch, semester, subjectCode, subjectName;
    private Double totalDays;

    public FacultySubjects() {
    }

    public FacultySubjects(String branch, String semester, String subjectCode, String subjectName, Double totalDays) {
        this.branch = branch;
        this.semester = semester;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.totalDays = totalDays;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Double getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(Double totalDays) {
        this.totalDays = totalDays;
    }
}
