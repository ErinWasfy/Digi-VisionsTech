package com.stc.calendarconflictoptimizer.model;

import com.stc.calendarconflictoptimizer.model.request.InputData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class InMemoryEventStore {

    private final Map<String, InputData> eventStore = new ConcurrentHashMap<>();
    private final Map<String, Object> locks = new ConcurrentHashMap<>();

    public void saveEvents(String userId, InputData inputData) {
        locks.putIfAbsent(userId, new Object());
        synchronized (locks.get(userId)) {
            eventStore.put(userId, inputData); // Defensive copy
        }
    }

    public InputData getData(String userId) {
        locks.putIfAbsent(userId, new Object());
        synchronized (locks.get(userId)) {
            return eventStore.get(userId);
        }
    }

    public void clearEvents(String userId) {
        locks.putIfAbsent(userId, new Object());
        synchronized (locks.get(userId)) {
            eventStore.remove(userId);
        }
    }


}
