package com.example.invisible.timetable;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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

    private final int DIALOG_EXIT = 2;

    private final DialogInterface.OnClickListener exitDlgListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    finish();
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

        //Find elements by id
        Button btnToStudentsActivity = (Button) findViewById(R.id.btnToStudentsActivity);
        Button btnToScheduleActivity = (Button) findViewById(R.id.btnToScheduleActivity);
        Button btnShowTimetable = (Button) findViewById(R.id.btnShowTimetable);
        Button btnToSenseiSchedule = (Button) findViewById(R.id.btnToSenseiSchedule);

        //Add listeners
        btnToScheduleActivity.setOnClickListener(this);
        btnToStudentsActivity.setOnClickListener(this);
        btnShowTimetable.setOnClickListener(this);
        btnToSenseiSchedule.setOnClickListener(this);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_EXIT) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Выйти?");
            adb.setMessage("Закрыть приложение?");
            adb.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
            adb.setPositiveButton("Да", exitDlgListener);
            adb.setNegativeButton("Нет", exitDlgListener);
            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    @Override
    public void onBackPressed() {
        showDialog(DIALOG_EXIT);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            // Общее расписание
            case R.id.btnToScheduleActivity:
                Intent intentSchedule = new Intent(this, GeneralSchedule.class);
                startActivity(intentSchedule);
                break;
            // Все студенты и доб./ред./уд. новых
            case R.id.btnToStudentsActivity:
                Intent intentStudents = new Intent(this, StudentsAll.class);
                startActivity(intentStudents);
                break;
            // Sensei schedule
            case R.id.btnToSenseiSchedule:
                Intent intentSensei = new Intent(this, StudentSchedule.class);
                intentSensei.putExtra("name", "Сенсей");
                startActivity(intentSensei);
                break;
            // Генерация расписания
            case R.id.btnShowTimetable:
                Intent intentShowTimetable = new Intent(this, ShowTimetable.class);
                startActivity(intentShowTimetable);
        }
    }
}
