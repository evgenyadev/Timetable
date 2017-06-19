package com.example.invisible.timetable;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.invisible.timetable.database.DBHelper;
import com.example.invisible.timetable.utils.MenuFix;

import java.util.ArrayList;

/**
 * Активити всех студентов, добавляет, удаляет, редактирует существующих
 */

public class StudentsAll extends AppCompatActivity implements View.OnClickListener {

    private final int DIALOG_DEL = 1;
    private final int DIALOG_DEL_ALL = 2;

    private DBHelper dbHelper;

    private Button btn_edit;
    private Button btn_delete;

    private Spinner spinnerStudents;

    private final DialogInterface.OnClickListener delListener = new DialogInterface.OnClickListener() {
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

    private final DialogInterface.OnClickListener delAllListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    dbHelper.wipeStudents();
                    Toast.makeText(StudentsAll.this, "Все ученики удалены.", Toast.LENGTH_SHORT).show();
                    fillSpinnerItems();
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_students_all, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_wipe_students:
                showDialog(DIALOG_DEL_ALL);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_all);

        setTitle("All students");

        dbHelper = new DBHelper(this);
        MenuFix.show3Dots(this);

        Button btn_to_add_activity = (Button) findViewById(R.id.btn_to_add_activity);
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
        ArrayList<String> names = dbHelper.fetchAllStudentsNames();

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
            adb.setPositiveButton("ДА", delListener);
            adb.setNegativeButton("ОТМЕНА", delListener);
            adb.setIcon(android.R.drawable.ic_notification_clear_all);
            return adb.create();
        }
        if (id == DIALOG_DEL_ALL) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Внимание!");
            adb.setMessage("Удалить всех учеников?");
            adb.setPositiveButton("ДА", delAllListener);
            adb.setNegativeButton("ОТМЕНА", delAllListener);
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
        // восстановить позицию в выпадающем списке
        int prePos = spinnerStudents.getSelectedItemPosition(); // запомнить текущую позицию
        int preCount = spinnerStudents.getCount();              // общее кол-во в списке
        fillSpinnerItems();
        int postCount = spinnerStudents.getCount();             // общее количество после действий
        if (preCount == postCount)                              // если не добавился новый -
            spinnerStudents.setSelection(prePos);               // - вернуть текущий элем.
    }
}
