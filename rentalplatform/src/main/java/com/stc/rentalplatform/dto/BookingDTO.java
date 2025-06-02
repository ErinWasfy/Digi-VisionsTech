package com.stc.rentalplatform.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;
import java.time.LocalDate;

@Data
public class BookingDTO {
    @NotNull(message = "Property ID is required")
    private Long propertyId;
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    @NotNull(message = "End date is required")
    private LocalDate endDate;
}
