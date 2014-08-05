package com.generator.structure.res.expected;

import org.junit.Assert;
import org.junit.Test;
import com.generator.structure.res.input.Enumerations;
import com.generator.structure.res.input.Enumerations.WeekDay;

public class EnumerationsTest {

    /**Coverage: 33,33%*/
    @Test
    public void testweekDayToStr_1() {
        String actual = Enumerations.weekDayToStr(WeekDay.MONDAY);
        Assert.assertEquals("Monday", actual);
    }

    /**Coverage: 33,33%*/
    @Test
    public void testweekDayToStr_2() {
        String actual = Enumerations.weekDayToStr(WeekDay.TUESDAY);
        Assert.assertEquals("Tuesday", actual);
    }

    /**Coverage: 33,33%*/
    @Test
    public void testweekDayToStr_3() {
        String actual = Enumerations.weekDayToStr(WeekDay.WEDNESDAY);
        Assert.assertEquals("Wednesday", actual);
    }

    /**Coverage: 33,33%*/
    @Test
    public void testweekDayToStr_4() {
        String actual = Enumerations.weekDayToStr(WeekDay.THURSDAY);
        Assert.assertEquals("Thursday", actual);
    }

    /**Coverage: 33,33%*/
    @Test
    public void testweekDayToStr_5() {
        String actual = Enumerations.weekDayToStr(WeekDay.FRIDAY);
        Assert.assertEquals("Friday", actual);
    }
}
