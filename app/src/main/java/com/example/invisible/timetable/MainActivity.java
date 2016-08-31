package com.example.invisible.timetable;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Суть:            составление расписания уроков для преподавателя в училище - трудоемкий процесс.
 *                  При количестве учеников 10 и более занимает порядка 2-3 дней.
 *                  А незначительные изменения в расписании вынуждают переделывать заного часть проделанной работы.
 *                  В целом этот процесс напоминает игру "Судоку".
 *                  C помощью этого приложения время составления расписания сокращается от нескольких дней до 10 минут.
 *
 * Задача:          написать программу, которая будет автоматически составлять расписание
 *                  для преподавателя училища.
 *
 * Входящие данные: общее расписание учебного заведения для всех курсов;
 *                  индивидуальное расписание - занятость каждого студента отдельно;
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final int DIALOG_CLEAR = 1;

    //Buttons
    Button btnToStudentsActivity, btnClearGeneralSchedule, btnToScheduleActivity, btnShowTimetable;

    DBHelper dbHelper;
    DialogInterface.OnClickListener dListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    dbHelper.wipeGeneralSchedule();
                    Toast.makeText(MainActivity.this, "Общее расписание очищено.", Toast.LENGTH_SHORT).show();
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        //Find elements by id
        btnToStudentsActivity = (Button) findViewById(R.id.btnToStudentsActivity);
        btnClearGeneralSchedule = (Button) findViewById(R.id.btnClearGeneralSchedule);
        btnToScheduleActivity = (Button) findViewById(R.id.btnToScheduleActivity);
        btnShowTimetable = (Button) findViewById(R.id.btnShowTimetable);

        //Add listeners
        btnToScheduleActivity.setOnClickListener(this);
        btnClearGeneralSchedule.setOnClickListener(this);
        btnToStudentsActivity.setOnClickListener(this);
        btnShowTimetable.setOnClickListener(this);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_CLEAR) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Внимание!");
            adb.setMessage("Полностью очистить общее расписание?");
            adb.setIcon(android.R.drawable.ic_notification_clear_all);
            adb.setPositiveButton("Да", dListener);
            adb.setNegativeButton("Нет", dListener);
            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            // Общее расписание
            case R.id.btnToScheduleActivity:
                Intent intentSchedule = new Intent(this, GeneralSchedule.class);
                startActivity(intentSchedule);
                break;
            // Очистить общее расписание
            case R.id.btnClearGeneralSchedule:
                showDialog(DIALOG_CLEAR);
                break;
            // Все студенты и доб./ред./уд. новых
            case R.id.btnToStudentsActivity:
                Intent intentStudents = new Intent(this, StudentsAll.class);
                startActivity(intentStudents);
                break;
            // Генерация расписания
            case R.id.btnShowTimetable:
                Intent intentShowTimetable = new Intent(this, ShowTimetable.class);
                startActivity(intentShowTimetable);
        }
    }
}
