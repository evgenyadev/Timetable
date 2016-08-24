package com.example.invisible.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Активити редактирует выбранного студента
 * В частности редактирование индивидуального расписания каждого студента
 */

public class StudentSchedule extends AppCompatActivity implements View.OnClickListener {

    Button btn_mon, btn_tue, btn_wed, btn_thu, btn_fri, btn_sat;
    TextView tvStudentEdit, tvSpecHour, tvPracHour;
    DBHelper dbHelper;
    Student currentStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_schedule);

        setTitle("Student info");

        dbHelper = new DBHelper(this);

        btn_mon = (Button) findViewById(R.id.btn_mon);
        btn_tue = (Button) findViewById(R.id.btn_tue);
        btn_wed = (Button) findViewById(R.id.btn_wed);
        btn_thu = (Button) findViewById(R.id.btn_thu);
        btn_fri = (Button) findViewById(R.id.btn_fri);
        btn_sat = (Button) findViewById(R.id.btn_sat);
        tvStudentEdit = (TextView) findViewById(R.id.tvStudentEdit);
        tvSpecHour = (TextView) findViewById(R.id.tvSpecHour);
        tvPracHour = (TextView) findViewById(R.id.tvPracHour);

        btn_mon.setOnClickListener(this);
        btn_tue.setOnClickListener(this);
        btn_wed.setOnClickListener(this);
        btn_thu.setOnClickListener(this);
        btn_fri.setOnClickListener(this);
        btn_sat.setOnClickListener(this);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        currentStudent = dbHelper.fetchStudentData(name);
        Log.d("debug", "StudentSchedule loaded student id = " + currentStudent._id + "; name = " + currentStudent.name);

        String generalInfo = currentStudent.toString();
        tvStudentEdit.setText(generalInfo);
        String specInfo = "Часов спец. в неделю: " + currentStudent.specialityHours;
        tvSpecHour.setText(specInfo);
        String pracInfo = "Часов пед.пр. в неделю: " + currentStudent.practiceHours;
        tvPracHour.setText(pracInfo);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this, EditDaySchedule.class);
        String day = "";

        switch (v.getId()) {
            case R.id.btn_mon:
                day = DBHelper.KEY_MON;
                break;
            case R.id.btn_tue:
                day = DBHelper.KEY_TUE;
                break;
            case R.id.btn_wed:
                day = DBHelper.KEY_WED;
                break;
            case R.id.btn_thu:
                day = DBHelper.KEY_THU;
                break;
            case R.id.btn_fri:
                day = DBHelper.KEY_FRI;
                break;
            case R.id.btn_sat:
                day = DBHelper.KEY_SAT;
                break;
        }

        intent.putExtra("name", currentStudent.name);
        intent.putExtra("day", day);
        startActivity(intent);
    }
}
