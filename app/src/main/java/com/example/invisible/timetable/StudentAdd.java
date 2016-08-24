package com.example.invisible.timetable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Активити добавления студента в базу данных
 * Студент после добавления имеет своё собственное расписание, скопированное из общего расписания
 * для его курса при добавлении, и дальнейшие изменения общего расписания не влияют на индивидуальное.
 */

public class StudentAdd extends AppCompatActivity implements View.OnClickListener {
    String[] spinData = {"0", "1", "2", "3", "4"};
    String[] spinDataCourse = {"I", "II", "III", "IV"};
    Spinner spinnerSpec;
    Spinner spinnerPractice;
    Spinner spinnerCourse;
    Button btn_student_add;
    EditText dtSurname;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_add);

        setTitle("Add student");

        dbHelper = new DBHelper(this);

        spinnerSpec = (Spinner) findViewById(R.id.spinnerCpec);
        spinnerPractice = (Spinner) findViewById(R.id.spinnerPractice);
        spinnerCourse = (Spinner) findViewById(R.id.spinnerCourse);
        btn_student_add = (Button) findViewById(R.id.btn_student_add);
        dtSurname = (EditText) findViewById(R.id.dtSurname);

        btn_student_add.setOnClickListener(this);

        ArrayAdapter<String> adapterHours = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinData);
        adapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapterCourse = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinDataCourse);
        adapterCourse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSpec.setAdapter(adapterHours);
        spinnerPractice.setAdapter(adapterHours);
        spinnerCourse.setAdapter(adapterCourse);

        spinnerSpec.setSelection(0);
        spinnerPractice.setSelection(0);
        spinnerCourse.setSelection(0);
    }

    @Override
    public void onClick(View v) {
        // если пустая строка имени - выйти
        if (TextUtils.isEmpty(dtSurname.getText().toString())) {
            dtSurname.setError("Введите фамилию.");
            return;
        }

        // если студент с таким именем уже создан - выйти
        ArrayList<String> names = dbHelper.fetchAllStudentNames();
        if (names.contains(dtSurname.getText().toString())) {
            dtSurname.setError("Студент с таким именем уже создан.");
            return;
        }

        String name = dtSurname.getText().toString();
        int course = (spinnerCourse.getSelectedItemPosition() + 1);
        int specHours = spinnerSpec.getSelectedItemPosition();
        int pracHours = spinnerPractice.getSelectedItemPosition();

        dbHelper.insertStudent(name, course, specHours, pracHours);

        // Вернуться в вызывающее активити
        finish();
    }
}