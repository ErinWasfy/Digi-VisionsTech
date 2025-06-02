package com.stc.calendarconflictoptimizer.rest;

import com.stc.calendarconflictoptimizer.model.InMemoryEventStore;
import com.stc.calendarconflictoptimizer.model.response.OutputResponse;
import com.stc.calendarconflictoptimizer.model.request.InputData;
import com.stc.calendarconflictoptimizer.service.CalendarConflictOptimizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calendar")
public class CalendarOptimizerRestController {
    @Autowired
    private CalendarConflictOptimizerService optimizerService;
    @Autowired
    private InMemoryEventStore inMemoryEventStore;

    public CalendarOptimizerRestController(CalendarConflictOptimizerService optimizerService, InMemoryEventStore inMemoryEventStore) {
        this.optimizerService = optimizerService;
        this.inMemoryEventStore = inMemoryEventStore;
    }

    @PostMapping("/optimize")
    public ResponseEntity<OutputResponse> optimizeCalendar(@RequestBody InputData input, @RequestParam String userId) {
        inMemoryEventStore.saveEvents(userId,input);
        return ResponseEntity.ok(optimizerService.optimize(inMemoryEventStore.getData(userId)));
    }
}
