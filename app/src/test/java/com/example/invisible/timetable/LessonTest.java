package com.example.invisible.timetable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LessonTest {

    private Lesson lesson1;
    private Lesson lesson5;

    @Before
    public void setUp() throws Exception {
        lesson1 = new Lesson(Lesson.LESSON_1_1);
        lesson5 = new Lesson(Lesson.LESSON_3_1);
    }

    @After
    public void tearDown() throws Exception {
        lesson1 = null;
        lesson5 = null;
    }

    @Test
    public void testShouldHaveClearConstants() throws Exception {
        assertEquals(0, Lesson.LESSON_1_1);
        assertEquals(5, Lesson.LESSON_3_2);
        assertEquals(11, Lesson.LESSON_6_2);
    }

    @Test
    public void testAddStudent() throws Exception {
        assertTrue("Should be empty", lesson1.getStudents().isEmpty());
        lesson1.addStudent(new Student());
        assertEquals("Should have 1 record", 1, lesson1.getStudents().size());
        lesson1.getStudents().remove(0);
        assertTrue("Should be empty", lesson1.getStudents().isEmpty());
        lesson1.addStudent(new Student());
        lesson1.addStudent(new Student());
        assertEquals("Should have 2 records", 2, lesson1.getStudents().size());
    }

    @Test
    public void testGetStudents() throws Exception {
        lesson5.addStudent(new Student());
        assertEquals("Undefined", lesson5.getStudents().get(0).name);
        assertEquals(0, lesson5.getStudents().get(0).specialityHours);
        assertEquals(0, lesson5.getStudents().get(0).practiceHours);
        assertEquals(0, lesson5.getStudents().get(0).course);
        assertEquals("000000000000", lesson5.getStudents().get(0).schedule.get(DBHelper.KEY_MON));
    }
}