package com.application.pradyotprakash.newattendanceappadmin;

import android.support.annotation.NonNull;

public class SubjectId {
    public String subjectId;

    public <T extends SubjectId> T withId(@NonNull final String id) {
        this.subjectId = id;
        return (T) this;
    }
}
