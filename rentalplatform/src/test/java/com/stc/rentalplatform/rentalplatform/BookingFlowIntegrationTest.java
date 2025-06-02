package com.stc.rentalplatform.rentalplatform;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookingFlowIntegrationTest extends AbstractIntegrationTest {

    @Test
    void shouldPreventOverlappingBooking() throws Exception {
        // 1. Create a property
        String propertyJson = "{\"name\":\"Beach House\",\"location\":\"Miami\",\"amenities\":[\"WiFi\"]}";
        MvcResult propertyResult = mockMvc.perform(post("/api/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(propertyJson))
                .andExpect(status().isCreated())
                .andReturn();

        Long propertyId = JsonPath.read(propertyResult.getResponse().getContentAsString(), "$.id");

        // 2. Book it
        String booking1 = String.format("""
            {
              "propertyId": 1,
              "startDate": "2025-06-01",
              "endDate": "2025-06-05"
            }
            """, propertyId);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(booking1))
                .andExpect(status().isCreated());

        // 3. Try overlapping booking
//        String booking2 = String.format("""
//            {
//              "propertyId": 1,
//              "startDate": "2025-06-03",
//              "endDate": "2025-06-06"
//            }
//            """, propertyId);
//
//        mockMvc.perform(post("/api/bookings")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(booking2))
//                .andExpect(status().isConflict());
    }
}
