package com.example.invisible.timetable;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Активити всех студентов, добавляет, удаляет, редактирует существующих
 */

public class StudentsAll extends AppCompatActivity implements View.OnClickListener {

    final int DIALOG_DEL = 1;

    DBHelper dbHelper;

    Button btn_to_add_activity;
    Button btn_edit;
    Button btn_delete;

    Spinner spinnerStudents;
    DialogInterface.OnClickListener dListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    String name = spinnerStudents.getSelectedItem().toString(); // взять имя
                    dbHelper.removeStudent(name); // удалить из бд по имени
                    Toast.makeText(StudentsAll.this, spinnerStudents.getSelectedItem().toString() + " удален.", Toast.LENGTH_SHORT).show();
                    fillSpinnerItems(); // обновить список
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_all);

        setTitle("All students");

        dbHelper = new DBHelper(this);

        btn_to_add_activity = (Button) findViewById(R.id.btn_to_add_activity);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        spinnerStudents = (Spinner) findViewById(R.id.spinnerStudents);

        fillSpinnerItems();

        btn_to_add_activity.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
    }

    private void fillSpinnerItems() {
        // запрос имен учеников
        ArrayList<String> names = dbHelper.fetchAllStudentNames();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(names);
        spinnerStudents.setAdapter(adapter);

        // Если в списке нет элементов - отключить кнопки удаления и редактирования
        if (spinnerStudents.getCount() == 0) {
            btn_delete.setEnabled(false);
            btn_edit.setEnabled(false);
        } else {
            btn_delete.setEnabled(true);
            btn_edit.setEnabled(true);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DEL) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Внимание!");
            adb.setMessage("");
            adb.setPositiveButton("ДА", dListener);
            adb.setNegativeButton("НЕТ", dListener);
            adb.setIcon(android.R.drawable.ic_notification_clear_all);
            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        if (id == DIALOG_DEL) {
            ((AlertDialog) dialog).setMessage("Удалить " + spinnerStudents.getSelectedItem().toString() + " ?");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_to_add_activity:
                Intent intentAddStudent = new Intent(this, StudentAdd.class);
                startActivity(intentAddStudent);
                break;
            case R.id.btn_edit:
                Intent intent = new Intent(this, StudentSchedule.class);
                intent.putExtra("name", spinnerStudents.getSelectedItem().toString());
                startActivity(intent);
                break;
            case R.id.btn_delete:
                showDialog(DIALOG_DEL);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillSpinnerItems();
    }
}
