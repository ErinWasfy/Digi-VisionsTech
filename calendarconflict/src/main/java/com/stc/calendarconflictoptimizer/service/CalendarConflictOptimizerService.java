package com.stc.calendarconflictoptimizer.service;

import com.stc.calendarconflictoptimizer.model.*;
import com.stc.calendarconflictoptimizer.model.response.OutputResponse;
import com.stc.calendarconflictoptimizer.model.request.InputData;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;


@Service
public class CalendarConflictOptimizerService {

  @Autowired
  private InMemoryEventStore eventStore;

  public synchronized OutputResponse optimize(InputData input) {
    ZoneId zoneId = ZoneId.of(input.getTimeZone());

   List<Event> events = input.getEvents();
    // Check conflicts

    events.sort(Comparator.comparing(Event::getStartTime));

    List<Conflict> conflicts = findConflicts(events,input.getTimeZone());

    List<FreeSlot> freeSlots = findFreeSlots(events, input.getWorkingHours(), zoneId);

    return new OutputResponse(conflicts, freeSlots);
  }
  private List<Conflict> findConflicts(List<Event> events,String timeZone) {
    List<Conflict> result = new ArrayList<>();
    ZoneId zoneId = ZoneId.of(timeZone);
    // Convert events to list of EventTime objects with parsed ZonedDateTimes
    List<Event> parsedEvents = new ArrayList<>();
    for (Event event : events) {
      ZonedDateTime start =ZonedDateTime.of( OffsetDateTime.parse(event.getStartTime()).toLocalDate(),OffsetDateTime.parse(event.getStartTime()).toLocalTime(),zoneId);
      ZonedDateTime end = ZonedDateTime.of( OffsetDateTime.parse(event.getEndTime()).toLocalDate(),OffsetDateTime.parse(event.getEndTime()).toLocalTime(),zoneId);

      parsedEvents.add(new Event(event.getTitle(), start.toOffsetDateTime().toString(), end.toOffsetDateTime().toString()));
    }
    // Sort events by start time
    parsedEvents.sort(Comparator.comparing(e -> e.startTime));

    // Use a min-heap (priority queue) to track active events, sorted by end time
    PriorityQueue<Event> active = new PriorityQueue<Event>(Comparator.comparing(e -> ZonedDateTime.of( OffsetDateTime.parse(e.endTime).toLocalDate(), OffsetDateTime.parse(e.endTime).toLocalTime(),zoneId)));

    for (Event current : parsedEvents) {
      // eliminate non-overlapping events (those that ended before current started)
      while (!active.isEmpty() && (ZonedDateTime.of( OffsetDateTime.parse(active.peek().endTime).toLocalDate(), OffsetDateTime.parse(active.peek().endTime).toLocalTime(),zoneId).isBefore(ZonedDateTime.of( OffsetDateTime.parse(current.startTime).toLocalDate(), OffsetDateTime.parse(current.startTime).toLocalTime(),zoneId))|| ZonedDateTime.of( OffsetDateTime.parse(active.peek().endTime).toLocalDate(), OffsetDateTime.parse(active.peek().endTime).toLocalTime(),zoneId).isEqual(ZonedDateTime.of( OffsetDateTime.parse(current.startTime).toLocalDate(), OffsetDateTime.parse(current.startTime).toLocalTime(),zoneId)))) {

        active.poll();
      }

      // All remaining in heap are overlapping with current
      for (Event overlapping : active) {
        ZonedDateTime overlapStart = OffsetDateTime.parse(current.startTime).atZoneSameInstant(zoneId).isAfter(OffsetDateTime.parse(overlapping.startTime).atZoneSameInstant(zoneId)) ? OffsetDateTime.parse(current.startTime).atZoneSameInstant(zoneId) : OffsetDateTime.parse(overlapping.startTime).atZoneSameInstant(zoneId);
        ZonedDateTime overlapEnd = OffsetDateTime.parse(current.endTime).atZoneSameInstant(zoneId).isBefore(OffsetDateTime.parse(overlapping.endTime).atZoneSameInstant(zoneId)) ? OffsetDateTime.parse(current.endTime).atZoneSameInstant(zoneId): OffsetDateTime.parse(overlapping.endTime).atZoneSameInstant(zoneId);
        result.add(new Conflict(current.title, overlapping.title, overlapStart, overlapEnd));
      }

      // Add current to active events
      active.offer(current);
    }
    return result;
  }

  private List<FreeSlot> findFreeSlots(List<Event> events, WorkingHours workingHours, ZoneId zoneId) {
    List<FreeSlot> result = new ArrayList<>();
    LocalTime start=LocalTime.parse(workingHours.getStart());
    LocalTime end=LocalTime.parse(workingHours.getEnd());
    LocalDate date = OffsetDateTime.parse(events.get(0).getStartTime()).toLocalDate();
    ZonedDateTime dayStart = ZonedDateTime.of(date, start, zoneId);
    ZonedDateTime dayEnd = ZonedDateTime.of(date, end, zoneId);

    ZonedDateTime current = dayStart;

    for (Event e : events) {
      if(ZonedDateTime.of(OffsetDateTime.parse(e.getStartTime()).toLocalDate(),OffsetDateTime.parse(e.getStartTime()).toLocalTime(),zoneId).isAfter(current)){
       result.add(new FreeSlot(current,ZonedDateTime.of(OffsetDateTime.parse(e.getStartTime()).toLocalDate(),OffsetDateTime.parse(e.getStartTime()).toLocalTime(),zoneId)));
      }
       current=current.isAfter(ZonedDateTime.of(OffsetDateTime.parse(e.getEndTime()).toLocalDate(),OffsetDateTime.parse(e.getEndTime()).toLocalTime(),zoneId))?current:ZonedDateTime.of(OffsetDateTime.parse(e.getEndTime()).toLocalDate(),OffsetDateTime.parse(e.getEndTime()).toLocalTime(),zoneId);
    }

    if (current.isBefore(dayEnd)) {
      result.add(new FreeSlot(current, dayEnd));
    }

    return result;
  }

}
