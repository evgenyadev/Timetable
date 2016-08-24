package com.example.invisible.timetable;

import java.util.ArrayList;

/**
 * Класс Lesson содержит в себе информацию о студентах существующих в данном уроке
 * Может добавить студента и вернуть список всех студентов существующих в уроке
 */
public class Lesson {

    //region Константы уроков
    public static final int LESSON_1_1 = 0;
    public static final int LESSON_1_2 = 1;
    public static final int LESSON_2_1 = 2;
    public static final int LESSON_2_2 = 3;
    public static final int LESSON_3_1 = 4;
    public static final int LESSON_3_2 = 5;
    public static final int LESSON_4_1 = 6;
    public static final int LESSON_4_2 = 7;
    public static final int LESSON_5_1 = 8;
    public static final int LESSON_5_2 = 9;
    public static final int LESSON_6_1 = 10;
    public static final int LESSON_6_2 = 11;
    //endregion

    int lessonId;
    private ArrayList<Student> students;

    public Lesson(int id) {
        this.lessonId = id;
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        // добавить в урок студента
        students.add(student);
    }

    public ArrayList<Student> getStudents() {
        // вернуть список студентов текущего урока
        return this.students;
    }
}
