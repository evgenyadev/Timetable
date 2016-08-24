package com.example.invisible.timetable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Класс вывода таблицы данных студентов полученных из бд
 */

public class ShowTimetable extends AppCompatActivity {
    TableLayout tlShowTimetable;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_timetable);

        tlShowTimetable = (TableLayout) findViewById(R.id.tlShowTimetable);

        dbHelper = new DBHelper(this);

        ArrayList<Student> students = dbHelper.fetchAllStudents();
        if (students.isEmpty())
            Toast.makeText(ShowTimetable.this, "Необходимо добавить студентов", Toast.LENGTH_SHORT).show();

        // Скопировать всю информацию из бд в таблицу
        Timetable rawtable = new Timetable(students);
        // Создать окончательную таблицу
        Timetable timetable = rawtable.makeFinalTimetable();

        // Вывод таблицы
        for (int row = Lesson.LESSON_1_1; row <= Lesson.LESSON_6_2; row++) {
            TableRow tlRow = new TableRow(this);
            for (int column = Day.MONDAY; column <= Day.SATURDAY; column++) {
                TextView tv = new TextView(this);
                // раскрасить таблицу серой сеткой
                if (column % 2 == 0 && row % 2 == 0)
                    tv.setBackgroundColor(getResources().getColor(R.color.colorColumn1));
                if (column % 2 != 0 && row % 2 != 0)
                    tv.setBackgroundColor(getResources().getColor(R.color.colorColumn1));

                tv.setText(timetable.getStringCell(column, row));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                tv.setGravity(Gravity.LEFT);
                tlRow.addView(tv);
            }
            tlShowTimetable.addView(tlRow);
        }
    }
}
