package com.sky.chowder;

import com.sky.utils.TextUtil;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        Assert.assertEquals("88.00", TextUtil.formatDou().format(88));
        Assert.assertEquals("空指针", TextUtil.getText(null));
    }
}