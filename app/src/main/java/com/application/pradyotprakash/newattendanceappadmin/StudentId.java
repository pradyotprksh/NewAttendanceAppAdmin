package com.application.pradyotprakash.newattendanceappadmin;

import android.support.annotation.NonNull;

/**
 * Created by pradyotprakash on 21/02/18.
 */

public class StudentId {
    public String studentId;

    public <T extends StudentId> T withId(@NonNull final String id) {
        this.studentId = id;
        return (T) this;
    }
}
