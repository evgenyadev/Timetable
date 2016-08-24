package com.example.invisible.timetable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Класс базы данных
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "data";
    public static final String TABLE_SCHEDULE = "schedule";
    public static final int DATABASE_VERSION = 6;

    public static final String KEY_ID = "_id";          // уникальный номер
    public static final String KEY_NAME = "name";       // имя
    public static final String KEY_COURSE = "course";   // курс
    public static final String KEY_SPEC_HOURS = "speciality";   // часов специальности
    public static final String KEY_TEACHING_PRACTICE_HOURS = "practice";    // часов педпрактики
    public static final String KEY_MON = "monday";      // дни недели
    public static final String KEY_TUE = "tuesday";     // *
    public static final String KEY_WED = "wednesday";   // *
    public static final String KEY_THU = "thursday";   // *
    public static final String KEY_FRI = "friday";      // *
    public static final String KEY_SAT = "saturday";    // //

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_SCHEDULE + "(" + KEY_ID + " integer primary key, " +
                KEY_NAME + " text, " + KEY_COURSE + " integer, " + KEY_SPEC_HOURS + " integer, " +
                KEY_TEACHING_PRACTICE_HOURS + " integer, " + KEY_MON + " text," + KEY_TUE + " text, " +
                KEY_WED + " text, " + KEY_THU + " text, " + KEY_FRI + " text, " + KEY_SAT + " text" + ")");

        // Заполнить новосозданную таблицу данными

        for (int i = 1; i < 5; i++) {
            ContentValues cv = new ContentValues();
            cv.put(KEY_NAME, "Общее расписание" + i);
            cv.put(KEY_COURSE, i);
            cv.put(KEY_SPEC_HOURS, 0);
            cv.put(KEY_TEACHING_PRACTICE_HOURS, 0);
            cv.put(KEY_MON, "000000000000");
            cv.put(KEY_TUE, "000000000000");
            cv.put(KEY_WED, "000000000000");
            cv.put(KEY_THU, "000000000000");
            cv.put(KEY_FRI, "000000000000");
            cv.put(KEY_SAT, "000000000000");
            db.insert(TABLE_SCHEDULE, null, cv);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_SCHEDULE);
        onCreate(db);
    }

    // Получить всю информацию о студенте по уникальному имени
    public Student fetchStudentData(String name) {
        Student student = new Student();
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.query(TABLE_SCHEDULE, null, KEY_NAME + " = \"" + name + "\"", null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                student._id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                student.name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                student.course = cursor.getInt(cursor.getColumnIndex(KEY_COURSE));
                student.specialityHours = cursor.getInt(cursor.getColumnIndex(KEY_SPEC_HOURS));
                student.practiceHours = cursor.getInt(cursor.getColumnIndex(KEY_TEACHING_PRACTICE_HOURS));

                student.schedule.put(KEY_MON, cursor.getString(cursor.getColumnIndex(KEY_MON)));
                student.schedule.put(KEY_TUE, cursor.getString(cursor.getColumnIndex(KEY_TUE)));
                student.schedule.put(KEY_WED, cursor.getString(cursor.getColumnIndex(KEY_WED)));
                student.schedule.put(KEY_THU, cursor.getString(cursor.getColumnIndex(KEY_THU)));
                student.schedule.put(KEY_FRI, cursor.getString(cursor.getColumnIndex(KEY_FRI)));
                student.schedule.put(KEY_SAT, cursor.getString(cursor.getColumnIndex(KEY_SAT)));
            }
        } catch (Exception e) {
            Log.d("debug", "DBhelper.fetchStudentData(String): Error while trying get data from database");
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return student;
    }

    @Deprecated
    public Student fetchStudentData(int _id) {
        Student student = new Student();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.query(TABLE_SCHEDULE, null, KEY_ID + " = " + _id, null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                student._id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                student.name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                student.course = cursor.getInt(cursor.getColumnIndex(KEY_COURSE));
                student.specialityHours = cursor.getInt(cursor.getColumnIndex(KEY_SPEC_HOURS));
                student.practiceHours = cursor.getInt(cursor.getColumnIndex(KEY_TEACHING_PRACTICE_HOURS));

                student.schedule.put(KEY_MON, cursor.getString(cursor.getColumnIndex(KEY_MON)));
                student.schedule.put(KEY_TUE, cursor.getString(cursor.getColumnIndex(KEY_TUE)));
                student.schedule.put(KEY_WED, cursor.getString(cursor.getColumnIndex(KEY_WED)));
                student.schedule.put(KEY_THU, cursor.getString(cursor.getColumnIndex(KEY_THU)));
                student.schedule.put(KEY_FRI, cursor.getString(cursor.getColumnIndex(KEY_FRI)));
                student.schedule.put(KEY_SAT, cursor.getString(cursor.getColumnIndex(KEY_SAT)));
            }
        } catch (Exception e) {
            Log.d("debug", "DBhelper.fetchStudentData(int): Error while trying get data from database");
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return student;
    }

    // Получить всю информацию о всех студентах
    public ArrayList<Student> fetchAllStudents() {
        ArrayList<Student> allStudents = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.query(TABLE_SCHEDULE, null, KEY_ID + " > 4", null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Student student = new Student();

                    student._id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                    student.name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                    student.course = cursor.getInt(cursor.getColumnIndex(KEY_COURSE));
                    student.specialityHours = cursor.getInt(cursor.getColumnIndex(KEY_SPEC_HOURS));
                    student.practiceHours = cursor.getInt(cursor.getColumnIndex(KEY_TEACHING_PRACTICE_HOURS));

                    student.schedule.put(KEY_MON, cursor.getString(cursor.getColumnIndex(KEY_MON)));
                    student.schedule.put(KEY_TUE, cursor.getString(cursor.getColumnIndex(KEY_TUE)));
                    student.schedule.put(KEY_WED, cursor.getString(cursor.getColumnIndex(KEY_WED)));
                    student.schedule.put(KEY_THU, cursor.getString(cursor.getColumnIndex(KEY_THU)));
                    student.schedule.put(KEY_FRI, cursor.getString(cursor.getColumnIndex(KEY_FRI)));
                    student.schedule.put(KEY_SAT, cursor.getString(cursor.getColumnIndex(KEY_SAT)));

                    allStudents.add(student);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("debug", "DBhelper.fetchAllStudents(): Error while trying get data from database");
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return allStudents;
    }

    // Обновить информацию о свободных\занятых уроках в день по уникальному _id студента
    public void updateDayValue(int _id, String day, String value) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(day, value);
        try {
            database.update(TABLE_SCHEDULE, cv, KEY_ID + " = " + _id, null);
        } catch (Exception e) {
            Log.d("debug", "DBhelper.updateDayValue(int,String): error while trying update row");
        }
    }

    // Удалить студента из базы по уникальному имени
    public void removeStudent(String name) {
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            database.delete(TABLE_SCHEDULE, KEY_NAME + " = \"" + name + "\"", null);
        } catch (Exception e) {
            Log.d("debug", "DBhelper.removeStudent(int): error while trying delete row");
        }
    }

    // Получить список уникальных имен всех студентов в базе
    public ArrayList<String> fetchAllStudentNames() {
        ArrayList<String> names = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        // первые 4 записи в таблице это общее расписание занятий для всех курсов
        Cursor cursor = database.query(TABLE_SCHEDULE, new String[]{KEY_NAME}, KEY_ID + " > 4", null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                int indexName = cursor.getColumnIndex(KEY_NAME);
                do {
                    names.add(0, cursor.getString(indexName));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("debug", "DBhelper.fetchAllStudentsNames(): Error while trying get data from database");
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return names;
    }

    // Сбросить к дефолтной всю информацию об общем расписании
    public void wipeGeneralSchedule() {
        SQLiteDatabase database = getWritableDatabase();
        StringBuilder sql = new StringBuilder();
        //region UPDATE table_schedule SET mon = "000...",...,sat = "00..." WHERE _id < 5;
        sql.append("UPDATE ");
        sql.append(TABLE_SCHEDULE);
        sql.append(" SET ");
        sql.append(KEY_MON);
        sql.append(" = \"000000000000\", ");
        sql.append(KEY_TUE);
        sql.append(" = \"000000000000\", ");
        sql.append(KEY_WED);
        sql.append(" = \"000000000000\", ");
        sql.append(KEY_THU);
        sql.append(" = \"000000000000\", ");
        sql.append(KEY_FRI);
        sql.append(" = \"000000000000\", ");
        sql.append(KEY_SAT);
        sql.append(" = \"000000000000\"");
        sql.append(" WHERE ");
        sql.append(KEY_ID);
        sql.append(" < 5");
        //endregion
        try {
            database.execSQL(sql.toString());
        } catch (SQLException e) {
            Log.d("debug", "DBhelper.wipeGeneralSchedule(): Error while wiping schedule data.");
        }
    }

    // Добавить нового студента, скопировав расписание соответствующего курса из общего расписания
    public void insertStudent(String name, int course, int specHours, int pracHours) {
        SQLiteDatabase database = getWritableDatabase();
        StringBuilder sqlQuery = new StringBuilder();
        //region INSERT INTO schedule SELECT ?,name,course,specHours,pracHours,monday,tuesday... FROM schedule WHERE _id = course;
        sqlQuery.append("INSERT INTO ");
        sqlQuery.append(TABLE_SCHEDULE);
        sqlQuery.append(" SELECT");
        sqlQuery.append("?,\"");
        sqlQuery.append(name);
        sqlQuery.append("\",");
        sqlQuery.append(course);
        sqlQuery.append(",");
        sqlQuery.append(specHours);
        sqlQuery.append(",");
        sqlQuery.append(pracHours);
        sqlQuery.append(",");
        sqlQuery.append(KEY_MON);
        sqlQuery.append(",");
        sqlQuery.append(KEY_TUE);
        sqlQuery.append(",");
        sqlQuery.append(KEY_WED);
        sqlQuery.append(",");
        sqlQuery.append(KEY_THU);
        sqlQuery.append(",");
        sqlQuery.append(KEY_FRI);
        sqlQuery.append(",");
        sqlQuery.append(KEY_SAT);
        sqlQuery.append(" FROM ");
        sqlQuery.append(TABLE_SCHEDULE);
        sqlQuery.append(" WHERE ");
        sqlQuery.append(KEY_ID);
        sqlQuery.append(" = ");
        sqlQuery.append(course);
        //endregion
        try {
            database.execSQL(sqlQuery.toString());
        } catch (SQLException e) {
            Log.d("debug", "DBhelper.insertStudent(): Error while trying insert data with raw request.");
        }
    }
}
