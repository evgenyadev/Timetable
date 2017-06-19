package com.example.invisible.timetable.tablestruct;

import java.util.ArrayList;

/**
 * Класс Lesson содержит в себе информацию о студентах существующих в данном уроке
 * Может добавить студента и вернуть список всех студентов существующих в уроке
 */
public class Lesson {

    private final ArrayList<Timetable.StudentStat> students;

    //region Константы уроков
    public static final int FIRST = 0;
    public static final int LAST = 11;
    //endregion

    Lesson() {
        this.students = new ArrayList<>();
    }

    public void addStudent(Timetable.StudentStat student) {
        students.add(student);
    }

    public ArrayList<Timetable.StudentStat> getStudents() {
        return this.students;
    }
}
