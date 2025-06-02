package com.stc.rentalplatform.mapper;

import com.stc.rentalplatform.dto.BookingDTO;
import com.stc.rentalplatform.entity.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingDTO toDto(Booking booking);
    Booking toEntity(BookingDTO dto);
}
