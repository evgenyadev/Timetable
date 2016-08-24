package com.example.invisible.timetable;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Этот активити могут вызывать классы GeneralSchedule и StudentSchedule
 * В этом активити производится заполнение либо общего расписания занятий в случае с GeneralSchedule
 * либо индивидуального расписания студента в случае StudentSchedule.
 */

public class EditDaySchedule extends AppCompatActivity implements View.OnClickListener {

    //region Used colors
    private static final int COLOR_RED = Color.RED; // красный
    private static final int COLOR_GREEN = Color.argb(255, 102, 153, 0); // зеленый как в xml файле
    //endregion

    //region Declare buttons
    Button btn_lesson_1_1, btn_lesson_1_2;
    Button btn_lesson_2_1, btn_lesson_2_2;
    Button btn_lesson_3_1, btn_lesson_3_2;
    Button btn_lesson_4_1, btn_lesson_4_2;
    Button btn_lesson_5_1, btn_lesson_5_2;
    Button btn_lesson_6_1, btn_lesson_6_2;
    //endregion
    TextView tvStudentInfo;
    DBHelper dbHelper;
    Student currentStudent;

    String extra_name; // имя передает вызывающее активити
    String extra_day;  // день ~~~


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_day_schedule);

        setTitle("Edit day");

        dbHelper = new DBHelper(this);

        //region Find buttons by id
        btn_lesson_1_1 = (Button) findViewById(R.id.btn_lesson_1_1);
        btn_lesson_1_2 = (Button) findViewById(R.id.btn_lesson_1_2);
        btn_lesson_2_1 = (Button) findViewById(R.id.btn_lesson_2_1);
        btn_lesson_2_2 = (Button) findViewById(R.id.btn_lesson_2_2);
        btn_lesson_3_1 = (Button) findViewById(R.id.btn_lesson_3_1);
        btn_lesson_3_2 = (Button) findViewById(R.id.btn_lesson_3_2);
        btn_lesson_4_1 = (Button) findViewById(R.id.btn_lesson_4_1);
        btn_lesson_4_2 = (Button) findViewById(R.id.btn_lesson_4_2);
        btn_lesson_5_1 = (Button) findViewById(R.id.btn_lesson_5_1);
        btn_lesson_5_2 = (Button) findViewById(R.id.btn_lesson_5_2);
        btn_lesson_6_1 = (Button) findViewById(R.id.btn_lesson_6_1);
        btn_lesson_6_2 = (Button) findViewById(R.id.btn_lesson_6_2);
        //endregion

        //region Set listener to buttons
        btn_lesson_1_1.setOnClickListener(this);
        btn_lesson_1_2.setOnClickListener(this);
        btn_lesson_2_1.setOnClickListener(this);
        btn_lesson_2_2.setOnClickListener(this);
        btn_lesson_3_1.setOnClickListener(this);
        btn_lesson_3_2.setOnClickListener(this);
        btn_lesson_4_1.setOnClickListener(this);
        btn_lesson_4_2.setOnClickListener(this);
        btn_lesson_5_1.setOnClickListener(this);
        btn_lesson_5_2.setOnClickListener(this);
        btn_lesson_6_1.setOnClickListener(this);
        btn_lesson_6_2.setOnClickListener(this);
        //endregion

        //region получение и вывод информации из вызывающего активити и бд
        tvStudentInfo = (TextView) findViewById(R.id.tvStudentEdit);
        Intent intent = getIntent();

        // имя и день
        extra_name = intent.getStringExtra("name");
        extra_day = intent.getStringExtra("day");

        currentStudent = dbHelper.fetchStudentData(extra_name); // найти в бд студента по имени
        Log.d("debug", "Loaded id = " + currentStudent._id + "; name = " + currentStudent.name);

        String strStudentInfo = currentStudent.toString() + ", " + dayToRus(extra_day);
        tvStudentInfo.setText(strStudentInfo);
        //endregion

        String db_data = currentStudent.getDaySchedule(extra_day); // получить расписание студента за текущий день
        paintButtons(db_data); // вывод расписания в виде раскраски кнопок в красный\зеленый
    }

    private void switchLessonStatus(int lesson) {
        char[] chData = currentStudent.getDaySchedule(extra_day).toCharArray(); // строку в массив бит
        chData[lesson] = chData[lesson] == '0' ? '1' : '0';                     // инверт значение бита в позиции lesson
        String value = String.valueOf(chData);                                  // биты обратно в строку
        currentStudent.setDaySchedule(extra_day, value);                  // обновить измененные данные в объекте этого класса
        dbHelper.updateDayValue(currentStudent._id, extra_day, value);     // отправить новые данные в базу данных
    }

    private void switchButtonColor(View v) {  // switch color from red to green and conversely
        int viewColor = ((ColorDrawable) v.getBackground()).getColor();
        if (viewColor == COLOR_RED)
            v.setBackgroundColor(COLOR_GREEN);
        else
            v.setBackgroundColor(COLOR_RED);
    }

    private void paintButtons(String data) {
        // раскрашивает 12 кнопок в зависимости от переданных бит
        // пример data = "010100110100"
        char chData[] = data.toCharArray();
        int changeColor;

        changeColor = chData[Lesson.LESSON_1_1] == '0' ? COLOR_GREEN : COLOR_RED;
        btn_lesson_1_1.setBackgroundColor(changeColor);

        changeColor = chData[Lesson.LESSON_1_2] == '0' ? COLOR_GREEN : COLOR_RED;
        btn_lesson_1_2.setBackgroundColor(changeColor);

        changeColor = chData[Lesson.LESSON_2_1] == '0' ? COLOR_GREEN : COLOR_RED;
        btn_lesson_2_1.setBackgroundColor(changeColor);

        changeColor = chData[Lesson.LESSON_2_2] == '0' ? COLOR_GREEN : COLOR_RED;
        btn_lesson_2_2.setBackgroundColor(changeColor);

        changeColor = chData[Lesson.LESSON_3_1] == '0' ? COLOR_GREEN : COLOR_RED;
        btn_lesson_3_1.setBackgroundColor(changeColor);

        changeColor = chData[Lesson.LESSON_3_2] == '0' ? COLOR_GREEN : COLOR_RED;
        btn_lesson_3_2.setBackgroundColor(changeColor);

        changeColor = chData[Lesson.LESSON_4_1] == '0' ? COLOR_GREEN : COLOR_RED;
        btn_lesson_4_1.setBackgroundColor(changeColor);

        changeColor = chData[Lesson.LESSON_4_2] == '0' ? COLOR_GREEN : COLOR_RED;
        btn_lesson_4_2.setBackgroundColor(changeColor);

        changeColor = chData[Lesson.LESSON_5_1] == '0' ? COLOR_GREEN : COLOR_RED;
        btn_lesson_5_1.setBackgroundColor(changeColor);

        changeColor = chData[Lesson.LESSON_5_2] == '0' ? COLOR_GREEN : COLOR_RED;
        btn_lesson_5_2.setBackgroundColor(changeColor);

        changeColor = chData[Lesson.LESSON_6_1] == '0' ? COLOR_GREEN : COLOR_RED;
        btn_lesson_6_1.setBackgroundColor(changeColor);

        changeColor = chData[Lesson.LESSON_6_2] == '0' ? COLOR_GREEN : COLOR_RED;
        btn_lesson_6_2.setBackgroundColor(changeColor);
    }

    private String dayToRus(String day) {
        String rusDay = "";
        switch (day) {
            case "monday":
                rusDay = "Понедельник";
                break;
            case "tuesday":
                rusDay = "Вторник";
                break;
            case "wednesday":
                rusDay = "Среда";
                break;
            case "thursday":
                rusDay = "Четверг";
                break;
            case "friday":
                rusDay = "Пятница";
                break;
            case "saturday":
                rusDay = "Суббота";
                break;
        }
        return rusDay;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_lesson_1_1:
                // Поменять в базе данных бит с 0 на 1 или наоборот
                switchLessonStatus(Lesson.LESSON_1_1);
                // Сменить цвет кнопки на противоположный
                switchButtonColor(v);
                break;
            case R.id.btn_lesson_1_2:
                switchLessonStatus(Lesson.LESSON_1_2);
                switchButtonColor(v);
                break;
            case R.id.btn_lesson_2_1:
                switchLessonStatus(Lesson.LESSON_2_1);
                switchButtonColor(v);
                break;
            case R.id.btn_lesson_2_2:
                switchLessonStatus(Lesson.LESSON_2_2);
                switchButtonColor(v);
                break;
            case R.id.btn_lesson_3_1:
                switchLessonStatus(Lesson.LESSON_3_1);
                switchButtonColor(v);
                break;
            case R.id.btn_lesson_3_2:
                switchLessonStatus(Lesson.LESSON_3_2);
                switchButtonColor(v);
                break;
            case R.id.btn_lesson_4_1:
                switchLessonStatus(Lesson.LESSON_4_1);
                switchButtonColor(v);
                break;
            case R.id.btn_lesson_4_2:
                switchLessonStatus(Lesson.LESSON_4_2);
                switchButtonColor(v);
                break;
            case R.id.btn_lesson_5_1:
                switchLessonStatus(Lesson.LESSON_5_1);
                switchButtonColor(v);
                break;
            case R.id.btn_lesson_5_2:
                switchLessonStatus(Lesson.LESSON_5_2);
                switchButtonColor(v);
                break;
            case R.id.btn_lesson_6_1:
                switchLessonStatus(Lesson.LESSON_6_1);
                switchButtonColor(v);
                break;
            case R.id.btn_lesson_6_2:
                switchLessonStatus(Lesson.LESSON_6_2);
                switchButtonColor(v);
                break;
        }
    }
}
