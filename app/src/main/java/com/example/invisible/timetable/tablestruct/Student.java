package com.example.invisible.timetable.tablestruct;

import com.example.invisible.timetable.database.DBHelper;

import java.util.HashMap;

/**
 * Класс Student содержит всю информацию о студенте
 * уник. _id в базе не повторяется
 * уник. name через UI добавить одинаковое имя в базу нельзя, только вручную
 * курс,количество часов специальностей в неделю,количество часов пед. практики (в процессе разработки)
 * инфо о занятых\свободных часах на каждый день недели
 */

public class Student {

    private String name;
    private int _id;
    private int course;
    private int specialityHours;
    private int practiceHours;
    private final HashMap<String, String> schedule;

    public Student() {
        // инициализация стд. значениями
        this._id = 0;
        this.name = "undefined";
        this.course = 0;
        this.specialityHours = 0;
        this.practiceHours = 0;

        // инициализация инд. расписания нулями
        this.schedule = new HashMap<>();
        for (String elem : DBHelper.DAYS) {
            String DAY_VALUE = "000000000000";
            this.schedule.put(elem, DAY_VALUE);
        }
    }

    Student(String _name) {
        // инициализация стд. значениями
        this();
        this.name = _name;
    }
    // конструктор копирования
    Student(Student _student) {
        this.name = _student.name;
        this._id = _student._id;
        this.course = _student.course;
        this.specialityHours = _student.specialityHours;
        this.practiceHours = _student.practiceHours;
        this.schedule = _student.schedule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public int getSpecialityHours() {
        return specialityHours;
    }

    public void setSpecialityHours(int specialityHours) {
        this.specialityHours = specialityHours;
    }

    public void increaseSpecialityHours() {
        this.specialityHours++;
    }

    public void setPracticeHours(int practiceHours) {
        this.practiceHours = practiceHours;
    }

    public String getDaySchedule(String day) throws IllegalArgumentException {
        for (String elem : DBHelper.DAYS) {
            if (elem.equals(day))
                return this.schedule.get(day);
        }
        throw new IllegalArgumentException("There's no Day in case like \'" + day + "\'");
    }

    public void setDaySchedule(String day, String value) throws IllegalArgumentException {
        if (value.length() != 12)
            throw new IllegalArgumentException("Length of \'value\' should be 12");

        for (String elem : DBHelper.DAYS) {
            if (elem.equals(day)) {
                this.schedule.put(day, value);
                return;
            }
        }
        throw new IllegalArgumentException("There's no Day in case like \'" + day + "\'");
    }

    @Override
    public String toString() {
        String textRet = "";

        if (this.name.contains("Общее расписание")) // Убрать для вывода цифру в конце. например в "Общее расписание1"
            textRet += "Общее расписание";
        else
            textRet += this.name;

        textRet += ", " + this.course + " курс"; // Добавить инфо о № курса

        return textRet;
    }
}
