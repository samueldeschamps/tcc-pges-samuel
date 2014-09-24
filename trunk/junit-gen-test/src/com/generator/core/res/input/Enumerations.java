package com.generator.core.res.input;

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

	public static WeekDay strToWeekDay(String str) {
		WeekDay[] values = allWeekDays();
		for (WeekDay val : values) {
			if (weekDayToStr(val).equals(str)) {
				return val;
			}
		}
		return null;
	}

	// I had to make this because JavaCompiler didn't undestand WeekDay.values().
	public static WeekDay[] allWeekDays() {
		return new WeekDay[] { WeekDay.MONDAY, WeekDay.TUESDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY, WeekDay.FRIDAY,
				WeekDay.SATURDAY, WeekDay.SUNDAY };
	}

	public static enum WeekDay {
		MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;
	}

}
