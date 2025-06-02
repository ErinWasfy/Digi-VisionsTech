package com.stc.calendarconflictoptimizer.model.request;

import com.stc.calendarconflictoptimizer.model.Event;
import com.stc.calendarconflictoptimizer.model.WorkingHours;


import java.util.List;

public class InputData {

    public WorkingHours workingHours;
    public String timeZone;
    public List<Event> events;

    public String getTimeZone() {
        return timeZone;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public WorkingHours getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(WorkingHours workingHours) {
        this.workingHours = workingHours;
    }
}
