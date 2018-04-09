package com.application.pradyotprakash.newattendanceappadmin;

import android.support.annotation.NonNull;

public class ClassId {
    public String subjectId;

    public <T extends ClassId> T withId(@NonNull final String id) {
        this.subjectId = id;
        return (T) this;
    }
}
