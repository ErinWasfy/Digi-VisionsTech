package com.stc.calendarconflictoptimizer.model.response;

import com.stc.calendarconflictoptimizer.model.Conflict;
import com.stc.calendarconflictoptimizer.model.FreeSlot;


import java.util.List;


public record OutputResponse(List<Conflict> conflicts, List<FreeSlot> freeSlots) {
}
