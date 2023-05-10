package tests;

import static org.junit.Assert.*;

import components.TimeFormat;
import org.junit.Test;

public class TimeFormatTester {

    @Test
    public void constructorTest1() {
        TimeFormat t = new TimeFormat();
        assertEquals(t.getHours(), 0);
        assertEquals(t.getMinutes(), 0);
        assertEquals(t.getSeconds(), 0);
        assertEquals(t.getMilliseconds(), 0);
    }

    @Test
    public void constructorTest2() {
        TimeFormat t = new TimeFormat(4357800);
        assertEquals(t.getHours(), 1);
        assertEquals(t.getMinutes(), 12);
        assertEquals(t.getSeconds(), 37);
        assertEquals(t.getMilliseconds(), 80);
    }

    @Test
    public void constructorTest3() {
        TimeFormat t = new TimeFormat(1, 5, 3, 7);
        assertEquals(t.getHours(), 1);
        assertEquals(t.getMinutes(), 5);
        assertEquals(t.getSeconds(), 3);
        assertEquals(t.getMilliseconds(), 7);
    }

    @Test
    public void constructorTest4() {
        TimeFormat t = new TimeFormat(5, 3, 7);
        assertEquals(t.getHours(), 0);
        assertEquals(t.getMinutes(), 5);
        assertEquals(t.getSeconds(), 3);
        assertEquals(t.getMilliseconds(), 7);
    }

    @Test
    public void constructorTest5() {
        TimeFormat t = new TimeFormat(10, 7);
        assertEquals(t.getHours(), 0);
        assertEquals(t.getMinutes(), 0);
        assertEquals(t.getSeconds(), 10);
        assertEquals(t.getMilliseconds(), 7);
    }

    @Test
    public void compareToTest() {
        TimeFormat t1 = new TimeFormat(1, 1, 1, 1);
        TimeFormat t2 = new TimeFormat(0, 2, 2, 2);
        TimeFormat t3 = new TimeFormat(2, 0, 0, 0);
        assertTrue(t1.compareTo(t2) > 0);
        assertTrue(t1.compareTo(t3) < 0);
        assertTrue(t2.compareTo(t1) < 0);
        assertTrue(t2.compareTo(t3) < 0);
    }

    @Test
    public void toStringTest() {
        TimeFormat hundredMeter = new TimeFormat(9, 58);
        TimeFormat twoHundredMeter = new TimeFormat(19, 19);
        TimeFormat randomTwoDigit = new TimeFormat(12, 3);
        TimeFormat mile = new TimeFormat(3, 43, 13);
        TimeFormat marathon = new TimeFormat(2, 1, 9, 0);
        assertEquals(hundredMeter.toString(), "9.58");
        assertEquals(twoHundredMeter.toString(), "19.19");
        assertEquals(randomTwoDigit.toString(), "12.03");
        assertEquals(mile.toString(), "3:43.13");
        assertEquals(marathon.toString(), "2:01:09.00");
    }
}
