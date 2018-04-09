package com.application.pradyotprakash.newattendanceappadmin;

import android.support.annotation.NonNull;

public class FacultyId {
    public String facultyId;

    public <T extends FacultyId> T withId(@NonNull final String id) {
        this.facultyId = id;
        return (T) this;
    }
}
