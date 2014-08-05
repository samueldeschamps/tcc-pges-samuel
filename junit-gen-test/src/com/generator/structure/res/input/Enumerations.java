package com.generator.structure.res.input;

public class Enumerations {

	public static String weekDayToStr(WeekDay weekDay) {
		switch (weekDay) {
		case MONDAY:
			return "Monday";
		case TUESDAY:
			return "Tuesday";
		case WEDNESDAY:
			return "Wednesday";
		case THURSDAY:
			return "Thursday";
		case FRIDAY:
			return "Friday";
		case SATURDAY:
			return "Saturday";
		case SUNDAY:
			return "Sunday";
		}
		return null;
	}
	
	public static enum WeekDay {
		MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
	}

}
