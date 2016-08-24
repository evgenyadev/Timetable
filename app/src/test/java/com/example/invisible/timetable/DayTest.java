package com.example.invisible.timetable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DayTest {

    private Day dayMon;
    private Day daySat;

    @Before
    public void setUp() throws Exception {
        dayMon = new Day(DBHelper.KEY_MON);
        daySat = new Day(DBHelper.KEY_SAT);
    }

    @After
    public void tearDown() throws Exception {
        dayMon = null;
        daySat = null;
    }

    @Test
    public void testConstantShouldHaveClearValues() throws Exception {
        assertEquals(0, Day.MONDAY);
        assertEquals(3, Day.THURSDAY);
        assertEquals(5, Day.SATURDAY);
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals("Name should be monday", "monday", dayMon.getName());
        assertEquals("Name should be saturday", "saturday", daySat.getName());
    }

    @Test
    public void testGetLesson() throws Exception {
        assertNotNull(daySat.getLesson(Lesson.LESSON_1_1));
        assertNotNull(daySat.getLesson(Lesson.LESSON_6_2));
        assertEquals("Class in Day should be Lesson.class", Lesson.class, dayMon.getLesson(Lesson.LESSON_1_1).getClass());
    }
}