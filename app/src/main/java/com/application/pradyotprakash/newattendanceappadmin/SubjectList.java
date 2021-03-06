package com.application.pradyotprakash.newattendanceappadmin;

public class SubjectList extends SubjectId{

    private String subjectName, branch, semester, subjectCode;

    public SubjectList() {
    }

    public SubjectList(String subjectName, String branch, String semester, String subjectCode) {

        this.subjectName = subjectName;
        this.branch = branch;
        this.semester = semester;
        this.subjectCode = subjectCode;
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

}
