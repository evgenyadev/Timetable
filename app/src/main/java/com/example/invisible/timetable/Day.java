package com.example.invisible.timetable;

import java.util.ArrayList;

/**
 * Класс Day хранит в себе информацию об уроках существующих в нем
 */
public class Day {

    //region Константы дней
    public static final int MONDAY = 0;
    public static final int TUESDAY = 1;
    public static final int WEDNESDAY = 2;
    public static final int THURSDAY = 3;
    public static final int FRIDAY = 4;
    public static final int SATURDAY = 5;
    //endregion

    private String sId;
    private ArrayList<Lesson> lessons;

    public Day(String sId) {
        this.sId = sId;
        this.lessons = new ArrayList<>();
        // создать структуру дня, в процессе работы структура статична, 12 уроков
        for (int i = Lesson.LESSON_1_1; i <= Lesson.LESSON_6_2; i++) {
            this.lessons.add(new Lesson(i));
        }
    }

    public Lesson getLesson(int lessonId) {
        return this.lessons.get(lessonId);
    }

    public String getName() {
        return this.sId;
    }
}
