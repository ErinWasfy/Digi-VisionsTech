/* 1- Find unbooked properties
*/
SELECT *
FROM properties p
WHERE NOT EXISTS (
    SELECT 1
    FROM bookings b
    WHERE b.property_id = p.id
);
/*
2- Detect Overlapping Bookings
*/

SELECT b1.id, b2.id, b1.property_id
FROM bookings b1
JOIN bookings b2
  ON b1.property_id = b2.property_id
 AND b1.id < b2.id
 AND (b1.start_date, b1.end_date) OVERLAPS (b2.start_date, b2.end_date);

/*****************************************/
/*
3- Top 5 Users by Distinct Properties Booked
*/
SELECT u.id, u.name, COUNT(DISTINCT b.property_id) AS property_count
FROM users u
JOIN bookings b ON u.id = b.user_id
GROUP BY u.id, u.name
ORDER BY property_count DESC
LIMIT 5;

/*
Recommended schema improvements for large datasets can be applied through
partitioning -- Here's an example of partitioning below
*/
/*Lets assume that the booking table will grow over time*/
CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    property_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL
) PARTITION BY RANGE (start_date);

/* then we create another partition for setting a range in this table like this figure below*/
CREATE TABLE bookings_2025 PARTITION OF bookings
FOR VALUES FROM ('2025-01-01') TO ('2026-01-01');
