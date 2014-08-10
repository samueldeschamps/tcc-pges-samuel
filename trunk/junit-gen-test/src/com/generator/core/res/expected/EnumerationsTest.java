package com.generator.core.res.expected;

import org.junit.Assert;
import org.junit.Test;
import com.generator.core.res.input.Enumerations;
import com.generator.core.res.input.Enumerations.WeekDay;

public class EnumerationsTest {

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
