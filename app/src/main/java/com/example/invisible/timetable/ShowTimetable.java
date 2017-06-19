package com.example.invisible.timetable;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.invisible.timetable.database.DBHelper;
import com.example.invisible.timetable.tablestruct.Day;
import com.example.invisible.timetable.tablestruct.Lesson;
import com.example.invisible.timetable.tablestruct.Student;
import com.example.invisible.timetable.tablestruct.Timetable;

import java.util.ArrayList;

/**
 * Класс вывода таблицы данных студентов полученных из бд
 */

public class ShowTimetable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_timetable);

        TableLayout tlShowTimetable = (TableLayout) findViewById(R.id.tlShowTimetable);

        DBHelper dbHelper = new DBHelper(this);

        // -- text color --
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        @ColorInt int textColor = typedValue.data;
        // -----------------

        ArrayList<Student> students = dbHelper.fetchAllStudentsData();
        Student sensei = dbHelper.fetchStudentData("Сенсей");
        if (students.isEmpty())
            Toast.makeText(ShowTimetable.this, "Необходимо добавить учеников", Toast.LENGTH_SHORT).show();

        // Скопировать всю информацию из бд в таблицу
        Timetable table = new Timetable(students, sensei);
        // Оставить только по одному студенту на один урок (убрать лишних)
        table.arrangeByPriority();

        //
        for (Student st : table.getNoRoomStudentsList()) {
            Toast.makeText(this, "Не хватило места для " + st.getName(), Toast.LENGTH_SHORT).show();
        }

        // Вывод таблицы
        for (int row = Lesson.FIRST; row <= Lesson.LAST; row++) {
            TableRow tlRow = new TableRow(this);
            for (int column = Day.FIRST; column <= Day.LAST; column++) {
                TextView tv = new TextView(this);
                // раскрасить таблицу серой сеткой
                if (column % 2 == 0 && row % 2 == 0)
                    tv.setBackgroundColor(getResources().getColor(R.color.LightGray));
                if (column % 2 != 0 && row % 2 != 0)
                    tv.setBackgroundColor(getResources().getColor(R.color.LightGray));

                tv.setText(table.getStringCell(column, row));
                tv.setTextColor(textColor);
                tv.setGravity(Gravity.LEFT);
                tlRow.addView(tv);
            }
            assert tlShowTimetable != null;
            tlShowTimetable.addView(tlRow);
        }
    }
}
