package com.example.invisible.timetable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class StudentTest {

    private Student student1;

    @Before
    public void setUp() throws Exception {
        student1 = new Student();
        student1._id = 1;
        student1.name = "TestName";
        student1.course = 4;
        student1.specialityHours = 3;
        student1.practiceHours = 2;
        student1.setDaySchedule(DBHelper.KEY_MON, "101101110000");
    }

    @After
    public void tearDown() throws Exception {
        student1 = null;
    }

    @Test
    public void testGetDaySchedule() throws Exception {
        // тест геттера
        assertEquals("101101110000", student1.getDaySchedule(DBHelper.KEY_MON));
        assertEquals("000000000000", student1.getDaySchedule(DBHelper.KEY_TUE));
        assertEquals("000000000000", student1.getDaySchedule(DBHelper.KEY_SAT));
        try {
            // симуляция некорректных входных данных
            student1.getDaySchedule("errorday");
        } catch (IllegalArgumentException e) {
            // проверка ожидаемого исключения
            assertThat(e.getMessage(), is("There's no Day in case like 'errorday'"));
        }
    }

    @Test
    public void testSetDaySchedule() throws Exception {
        // тест сеттера
        student1.setDaySchedule("monday", "101101110111");
        assertEquals("101101110111", student1.getDaySchedule("monday"));
        try {
            // симуляция некорректных данных
            student1.setDaySchedule("nothing1", "011001");
        } catch (IllegalArgumentException e) {
            // ожидаемое исключение
            assertThat(e.getMessage(), is("Length of 'value' should be 12"));
        }
        try {
            student1.setDaySchedule("nothing1", "011001010101");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("There's no Day in case like 'nothing1'"));
        }
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("TestName, 4 курс", student1.toString());
    }
}