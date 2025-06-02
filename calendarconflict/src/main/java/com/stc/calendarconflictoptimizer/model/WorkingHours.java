package com.stc.calendarconflictoptimizer.model;



/* Working hours class
 */

public class WorkingHours {
    public String start;
    public String end;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public WorkingHours(String start, String end) {
        this.start = start;
        this.end = end;
    }
}
