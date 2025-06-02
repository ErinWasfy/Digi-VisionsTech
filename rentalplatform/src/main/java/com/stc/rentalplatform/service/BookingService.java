package com.stc.rentalplatform.service;

import com.stc.rentalplatform.dto.BookingDTO;
import com.stc.rentalplatform.entity.Booking;
import com.stc.rentalplatform.entity.Property;
import com.stc.rentalplatform.exception.BookingConflictException;
import com.stc.rentalplatform.repository.BookingRepository;
import com.stc.rentalplatform.repository.PropertyRepository;
import com.stc.rentalplatform.tenant.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

public class BookingService {
    @Autowired
    BookingRepository bookingRepo;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public BookingService(BookingRepository bookingRepo) {
        this.bookingRepo = bookingRepo;
    }

    private static final long LOCK_TIMEOUT = 10; // seconds

    public Booking createBooking(Booking booking) throws BookingConflictException {
        String lockKey = "lock:property:" + booking.getPropertyId();
        String lockValue = UUID.randomUUID().toString();
        Boolean acquired = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, lockValue, Duration.ofSeconds(LOCK_TIMEOUT));
        if (Boolean.TRUE.equals(acquired))
        {

            try {
                List<Booking> conflicts = bookingRepo.findConflictingBookings(
                        booking.getPropertyId(), booking.getStartDate(), booking.getEndDate());

                if (!conflicts.isEmpty()) {
                    throw new BookingConflictException("Booking conflicts with existing bookings.");
                }
                booking.setTenantId(TenantContext.getTenant());
                return bookingRepo.save(booking);
            }
            finally {
            String currentLockValue = (String) redisTemplate.opsForValue().get(lockKey);
            if (lockValue.equals(currentLockValue)) {
                redisTemplate.delete(lockKey);
            }
        }
        }
     else return null;
    }
    public List<Booking> getAll() {
        return bookingRepo.findAll();
    }
    public void deleteById(Long id) {
         bookingRepo.deleteById(id);
    }
}
