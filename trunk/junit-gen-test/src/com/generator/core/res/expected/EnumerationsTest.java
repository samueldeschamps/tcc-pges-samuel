package com.generator.core.res.expected;

import org.junit.Assert;
import org.junit.Test;
import com.generator.core.res.input.Enumerations;
import com.generator.core.res.input.Enumerations.WeekDay;

public class EnumerationsTest {

    /**
     * Coverage: 100,00%
     */
    @Test
    public void testAllWeekDays_1() {
        WeekDay[] actual = Enumerations.allWeekDays();
        WeekDay[] expected = new WeekDay[] { WeekDay.MONDAY, WeekDay.TUESDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY, WeekDay.FRIDAY, WeekDay.SATURDAY, WeekDay.SUNDAY };
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Coverage: 92,59%
     */
    @Test
    public void testStrToWeekDay_1() {
        WeekDay actual = Enumerations.strToWeekDay(null);
        Assert.assertEquals(null, actual);
    }

    /**
     * Coverage: 92,59%
     */
    @Test
    public void testStrToWeekDay_2() {
        WeekDay actual = Enumerations.strToWeekDay("Sunday");
        Assert.assertEquals(WeekDay.SUNDAY, actual);
    }

    /**
     * Coverage: 92,59%
     */
    @Test
    public void testStrToWeekDay_3() {
        WeekDay actual = Enumerations.strToWeekDay("Saturday");
        Assert.assertEquals(WeekDay.SATURDAY, actual);
    }

    /**
     * Coverage: 92,59%
     */
    @Test
    public void testStrToWeekDay_4() {
        WeekDay actual = Enumerations.strToWeekDay("Friday");
        Assert.assertEquals(WeekDay.FRIDAY, actual);
    }

    /**
     * Coverage: 92,59%
     */
    @Test
    public void testStrToWeekDay_5() {
        WeekDay actual = Enumerations.strToWeekDay("Thursday");
        Assert.assertEquals(WeekDay.THURSDAY, actual);
    }

    /**
     * Coverage: 92,59%
     */
    @Test
    public void testStrToWeekDay_6() {
        WeekDay actual = Enumerations.strToWeekDay("Wednesday");
        Assert.assertEquals(WeekDay.WEDNESDAY, actual);
    }

    /**
     * Coverage: 92,59%
     */
    @Test
    public void testStrToWeekDay_7() {
        WeekDay actual = Enumerations.strToWeekDay("Tuesday");
        Assert.assertEquals(WeekDay.TUESDAY, actual);
    }

    /**
     * Coverage: 88,89%
     */
    @Test
    public void testStrToWeekDay_8() {
        WeekDay actual = Enumerations.strToWeekDay("Monday");
        Assert.assertEquals(WeekDay.MONDAY, actual);
    }

    /**
     * Coverage: 33,33%
     */
    @Test
    public void testWeekDayToStr_1() {
        String actual = Enumerations.weekDayToStr(WeekDay.MONDAY);
        Assert.assertEquals("Monday", actual);
    }

    /**
     * Coverage: 33,33%
     */
    @Test
    public void testWeekDayToStr_2() {
        String actual = Enumerations.weekDayToStr(WeekDay.TUESDAY);
        Assert.assertEquals("Tuesday", actual);
    }

    /**
     * Coverage: 33,33%
     */
    @Test
    public void testWeekDayToStr_3() {
        String actual = Enumerations.weekDayToStr(WeekDay.WEDNESDAY);
        Assert.assertEquals("Wednesday", actual);
    }

    /**
     * Coverage: 33,33%
     */
    @Test
    public void testWeekDayToStr_4() {
        String actual = Enumerations.weekDayToStr(WeekDay.THURSDAY);
        Assert.assertEquals("Thursday", actual);
    }

    /**
     * Coverage: 33,33%
     */
    @Test
    public void testWeekDayToStr_5() {
        String actual = Enumerations.weekDayToStr(WeekDay.FRIDAY);
        Assert.assertEquals("Friday", actual);
    }

    /**
     * Coverage: 33,33%
     */
    @Test
    public void testWeekDayToStr_6() {
        String actual = Enumerations.weekDayToStr(WeekDay.SATURDAY);
        Assert.assertEquals("Saturday", actual);
    }

    /**
     * Coverage: 33,33%
     */
    @Test
    public void testWeekDayToStr_7() {
        String actual = Enumerations.weekDayToStr(WeekDay.SUNDAY);
        Assert.assertEquals("Sunday", actual);
    }
}
