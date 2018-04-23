package com.application.pradyotprakash.newattendanceappadmin;

import android.support.annotation.NonNull;

public class TimetableId {

    public String timetableId;

    public <T extends TimetableId> T withId(@NonNull final String id) {
        this.timetableId = id;
        return (T) this;
    }

}
