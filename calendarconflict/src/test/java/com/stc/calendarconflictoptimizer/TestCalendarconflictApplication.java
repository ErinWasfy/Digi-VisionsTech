package com.stc.calendarconflictoptimizer;

import org.springframework.boot.SpringApplication;

public class TestCalendarconflictApplication {

	public static void main(String[] args) {
		SpringApplication.from(CalendarConflictApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
