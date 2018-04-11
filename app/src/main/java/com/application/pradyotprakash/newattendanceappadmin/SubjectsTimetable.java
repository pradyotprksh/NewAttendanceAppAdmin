package com.application.pradyotprakash.newattendanceappadmin;

public class SubjectsTimetable {

    private String subjectName, subjectCode, subjectTeacher, from, to, weekDay;

    public SubjectsTimetable() {
    }

    public SubjectsTimetable(String subjectName, String subjectCode, String subjectTeacher, String from, String to, String weekDay) {
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.subjectTeacher = subjectTeacher;
        this.from = from;
        this.to = to;
        this.weekDay = weekDay;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectTeacher() {
        return subjectTeacher;
    }

    public void setSubjectTeacher(String subjectTeacher) {
        this.subjectTeacher = subjectTeacher;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }
}
