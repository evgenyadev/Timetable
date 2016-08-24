package com.example.invisible.timetable;

import java.util.HashMap;

/**
 * Класс Student содержит всю информацию о студенте
 * уник. _id в базе не повторяется
 * уник. name через UI добавить одинаковое имя в базу нельзя, только вручную
 * курс,количество часов специальностей в неделю,количество часов пед. практики (в процессе разработки)
 * инфо о занятых\свободных часах на каждый день недели
 */

public class Student {
    // Массив названий дней недели
    final String[] daysKey = {DBHelper.KEY_MON, DBHelper.KEY_TUE, DBHelper.KEY_WED
            , DBHelper.KEY_THU, DBHelper.KEY_FRI, DBHelper.KEY_SAT};
    final String dayValue = "000000000000"; // 12 bit

    public int _id;
    public String name;
    public int course;
    public int specialityHours;
    public int practiceHours;
    HashMap<String, String> schedule;

    public Student() {
        // инициализация стд. значениями
        this._id = 0;
        this.name = "Undefined";
        this.course = 0;
        this.specialityHours = 0;
        this.practiceHours = 0;

        // инициализация инд. расписания нулями
        schedule = new HashMap<>();
        for (String elem : daysKey) {
            schedule.put(elem, dayValue);
        }
    }

    public String getDaySchedule(String day) throws IllegalArgumentException {
        for (String elem : daysKey) {
            if (elem.equals(day))
                return schedule.get(day);
        }
        throw new IllegalArgumentException("There's no Day in case like \'" + day + "\'");
    }

    public void setDaySchedule(String day, String value) throws IllegalArgumentException {
        if (value.length() != 12)
            throw new IllegalArgumentException("Length of \'value\' should be 12");

        for (String elem : daysKey) {
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
