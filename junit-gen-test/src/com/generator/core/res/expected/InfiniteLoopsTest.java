package com.generator.core.res.expected;

import org.junit.Assert;
import org.junit.Test;
import com.generator.core.res.input.InfiniteLoops;

public class InfiniteLoopsTest {

    /**
     * Coverage: 42,86%
     */
    @Test
    public void testBeEternal_1() {
        int actual = InfiniteLoops.beEternal(1);
        Assert.assertEquals(2, actual);
    }

    /**
     * Coverage: 42,86%
     */
    @Test
    public void testBeEternal_2() {
        int actual = InfiniteLoops.beEternal(-1);
        Assert.assertEquals(0, actual);
    }

    /**
     * FIXME: Infinite loop detected! (Took more than 3 seconds)
     */
    @Test
    public void testBeEternal_3() {
        InfiniteLoops.beEternal(0);
    }
}
