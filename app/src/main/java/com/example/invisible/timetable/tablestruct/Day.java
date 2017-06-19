package com.example.invisible.timetable.tablestruct;

import java.util.ArrayList;

/**
 * Класс Day хранит в себе информацию об уроках существующих в нем
 */
public class Day {

    //region Константы дней
    public static final int FIRST = 0;
    public static final int LAST = 5;
    //endregion

    private final String sId;
    private final ArrayList<Lesson> lessons;

    public Day(String sId) {
        this.sId = sId;
        this.lessons = new ArrayList<>();
        // создать структуру дня, в процессе работы структура статична, 12 уроков
        for (int i = Lesson.FIRST; i <= Lesson.LAST; i++) {
            this.lessons.add(new Lesson());
        }
    }

    public Lesson getLesson(int lessonId) {
        return this.lessons.get(lessonId);
    }

    public String getName() {
        return this.sId;
    }
}
