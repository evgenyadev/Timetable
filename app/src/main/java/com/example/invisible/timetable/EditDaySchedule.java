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

import com.example.invisible.timetable.database.DBHelper;
import com.example.invisible.timetable.tablestruct.Lesson;
import com.example.invisible.timetable.tablestruct.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Этот активити могут вызывать классы GeneralSchedule и StudentSchedule
 * В этом активити производится заполнение либо общего расписания занятий в случае с GeneralSchedule
 * либо индивидуального расписания студента в случае StudentSchedule.
 */

public class EditDaySchedule extends AppCompatActivity implements View.OnClickListener {

    private List<Button> buttons;
    private static final int[] BUTTON_IDS = {
            R.id.btn_lesson_1_1,
            R.id.btn_lesson_1_2,
            R.id.btn_lesson_2_1,
            R.id.btn_lesson_2_2,
            R.id.btn_lesson_3_1,
            R.id.btn_lesson_3_2,
            R.id.btn_lesson_4_1,
            R.id.btn_lesson_4_2,
            R.id.btn_lesson_5_1,
            R.id.btn_lesson_5_2,
            R.id.btn_lesson_6_1,
            R.id.btn_lesson_6_2
    };

    private static final String[] DAYS_RUS = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};

    //region Used colors
    private static final int COLOR_RED = Color.RED; // красный
    private static final int COLOR_GREEN = Color.argb(255, 102, 153, 0); // зеленый как в xml файле
    //endregion

    private DBHelper dbHelper;
    private Student currentStudent;

    private String extra_day;  // день ~~~


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_day_schedule);

        setTitle("Edit day");

        dbHelper = new DBHelper(this);

        buttons = new ArrayList<>();
        for (int btn_id : BUTTON_IDS) {
            Button btn = (Button) findViewById(btn_id);
            assert btn != null;
            btn.setOnClickListener(this);
            buttons.add(btn);
        }

        //region получение и вывод информации из вызывающего активити и бд
        TextView tvStudentInfo = (TextView) findViewById(R.id.tvStudentEdit);
        Intent intent = getIntent();

        // имя и день
        String extra_name = intent.getStringExtra("name");
        extra_day = intent.getStringExtra("day");

        currentStudent = dbHelper.fetchStudentData(extra_name); // найти в бд студента по имени
        Log.d("debug", "EditDaySchedule.onCreate() Loaded id = " + currentStudent.get_id() + "; name = " + currentStudent.getName() +
                "; day = " + extra_day);


        String strStudentInfo = currentStudent.toString() + ", " + dayToRus(extra_day);
        tvStudentInfo.setText(strStudentInfo);
        //endregion

        String db_data = currentStudent.getDaySchedule(extra_day); // получить расписание студента за текущий день
        paintButtons(db_data); // вывод расписания в виде раскраски кнопок в красный\зеленый
    }

    private void switchLessonStatus(int lesson_id) {
        char[] chData = currentStudent.getDaySchedule(extra_day).toCharArray(); // строку в массив бит
        chData[lesson_id] = chData[lesson_id] == '0' ? '1' : '0';                     // инверт значение бита в позиции lesson
        String value = String.valueOf(chData);                                  // биты обратно в строку
        currentStudent.setDaySchedule(extra_day, value);                  // обновить измененные данные в объекте этого класса
        dbHelper.updateDayValue(currentStudent.get_id(), extra_day, value);     // отправить новые данные в базу данных
    }

    private void switchButtonColor(View v) {  // switch color from red to green and conversely
        int viewColor = ((ColorDrawable) v.getBackground()).getColor();
        v.setBackgroundColor(viewColor == COLOR_RED ? COLOR_GREEN : COLOR_RED);
    }

    private void paintButtons(String data) {
        // раскрашивает 12 кнопок в зависимости от переданных бит
        // пример data = "010100110100"

        char chData[] = data.toCharArray();
        int changeColor;

        for (int lsn_id = Lesson.FIRST; lsn_id <= Lesson.LAST; lsn_id++) {
            changeColor = chData[lsn_id] == '0' ? COLOR_GREEN : COLOR_RED;
            buttons.get(lsn_id).setBackgroundColor(changeColor);
        }
    }

    private String dayToRus(String day) {
        int index = Arrays.asList(DBHelper.DAYS).indexOf(day);
        return DAYS_RUS[index];
    }

    @Override
    public void onClick(View v) {

        for (Button vb : buttons) {
            if ( vb == v ) {
                switchLessonStatus(buttons.indexOf(vb));
                switchButtonColor(v);
                return;
            }
        }
        Log.d("debug", "EditDaySchedule.onClick: there're no matched buttons in the list.");
    }
}
