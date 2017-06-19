package com.example.invisible.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.invisible.timetable.database.DBHelper;
import com.example.invisible.timetable.tablestruct.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Активити редактирует выбранного студента
 * В частности редактирование индивидуального расписания каждого студента
 */

public class StudentSchedule extends AppCompatActivity implements View.OnClickListener {

    private static final int[] BUTTON_IDS = {R.id.btn_mon, R.id.btn_tue, R.id.btn_wed,
            R.id.btn_thu, R.id.btn_fri, R.id.btn_sat};
    private List<Button> buttons;
    private TextView tvSpecHour;
    private DBHelper dbHelper;
    private Student currentStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_schedule);

        setTitle("Student info");

        dbHelper = new DBHelper(this);

        buttons = new ArrayList<>();
        for (int btn_id : BUTTON_IDS) {
            Button btn = (Button) findViewById(btn_id);
            assert btn != null;
            btn.setOnClickListener(this);
            buttons.add(btn);
        }

        TextView tvStudentEdit = (TextView) findViewById(R.id.tvStudentEdit);
        tvSpecHour = (TextView) findViewById(R.id.tvSpecHour);
        TextView tvPracHour = (TextView) findViewById(R.id.tvPracHour);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        currentStudent = dbHelper.fetchStudentData(name);
        Log.d("debug", "StudentSchedule.onCreate() loaded student id = " + currentStudent.get_id() + "; name = " + currentStudent.getName());

        String generalInfo = currentStudent.toString();
        tvStudentEdit.setText(generalInfo);

        // не отображать количество специальностей для преподавателя
        if (name.equals("Сенсей"))
            tvSpecHour.setVisibility(View.GONE);

        String specInfo = "Часов спец. в неделю: " + currentStudent.getSpecialityHours();
        tvSpecHour.setText(specInfo);
        //       String pracInfo = "Часов пед.пр. в неделю: " + currentStudent.practiceHours;
        //       tvPracHour.setText(pracInfo);
    }

    public void onSpecViewClick(View v) {
        currentStudent.increaseSpecialityHours();
        if (currentStudent.getSpecialityHours() >= 5)
            currentStudent.setSpecialityHours(1);
        dbHelper.updateSpecHours(currentStudent.get_id(), currentStudent.getSpecialityHours());
        String specInfo = "Часов спец. в неделю: " + currentStudent.getSpecialityHours();
        tvSpecHour.setText(specInfo);
        Toast.makeText(this, "Кол-во часов спец. изменено на " + currentStudent.getSpecialityHours(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this, EditDaySchedule.class);
        String day = "";

        for (Button btn : buttons)
            if (btn == v) {
                day = Arrays.asList(DBHelper.DAYS).get(buttons.indexOf(btn));
                break;
            }
        intent.putExtra("name", currentStudent.getName());
        intent.putExtra("day", day);
        startActivity(intent);
    }
}
