package com.stc.calendarconflictoptimizer.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class Conflict {
  public String event1;
  public String event2;
  public ZonedDateTime overlapStart;
  public ZonedDateTime overlapEnd;

  public Conflict(String event1, String event2, ZonedDateTime overlapStart, ZonedDateTime overlapEnd) {
    this.event1 = event1;
    this.event2 = event2;
    this.overlapStart = overlapStart;
    this.overlapEnd = overlapEnd;
  }

  public String getEvent1() {
    return event1;
  }

  public void setEvent1(String event1) {
    this.event1 = event1;
  }

  public String getEvent2() {
    return event2;
  }

  public void setEvent2(String event2) {
    this.event2 = event2;
  }

  public ZonedDateTime getOverlapStart() {
    return overlapStart;
  }

  public void setOverlapStart(ZonedDateTime overlapStart) {
    this.overlapStart = overlapStart;
  }

  public ZonedDateTime getOverlapEnd() {
    return overlapEnd;
  }

  public void setOverlapEnd(ZonedDateTime overlapEnd) {
    this.overlapEnd = overlapEnd;
  }
}
