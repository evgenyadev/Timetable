package com.example.invisible.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Активити общего расписания
 * Представляет из себя таблицу всех дней недели для всех курсов
 * Вызывает EditDaySchedule для редактирования каждого дня отдельно
 */

public class GeneralSchedule extends AppCompatActivity implements View.OnClickListener {

    //region Declare buttons
    Button btn_1_1, btn_1_2, btn_1_3, btn_1_4, btn_1_5, btn_1_6;
    Button btn_2_1, btn_2_2, btn_2_3, btn_2_4, btn_2_5, btn_2_6;
    Button btn_3_1, btn_3_2, btn_3_3, btn_3_4, btn_3_5, btn_3_6;
    Button btn_4_1, btn_4_2, btn_4_3, btn_4_4, btn_4_5, btn_4_6;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_schedule);

        setTitle("General schedule");

        //region Find buttons by id
        btn_1_1 = (Button) findViewById(R.id.btn_1_1);
        btn_1_2 = (Button) findViewById(R.id.btn_1_2);
        btn_1_3 = (Button) findViewById(R.id.btn_1_3);
        btn_1_4 = (Button) findViewById(R.id.btn_1_4);
        btn_1_5 = (Button) findViewById(R.id.btn_1_5);
        btn_1_6 = (Button) findViewById(R.id.btn_1_6);
        btn_2_1 = (Button) findViewById(R.id.btn_2_1);
        btn_2_2 = (Button) findViewById(R.id.btn_2_2);
        btn_2_3 = (Button) findViewById(R.id.btn_2_3);
        btn_2_4 = (Button) findViewById(R.id.btn_2_4);
        btn_2_5 = (Button) findViewById(R.id.btn_2_5);
        btn_2_6 = (Button) findViewById(R.id.btn_2_6);
        btn_3_1 = (Button) findViewById(R.id.btn_3_1);
        btn_3_2 = (Button) findViewById(R.id.btn_3_2);
        btn_3_3 = (Button) findViewById(R.id.btn_3_3);
        btn_3_4 = (Button) findViewById(R.id.btn_3_4);
        btn_3_5 = (Button) findViewById(R.id.btn_3_5);
        btn_3_6 = (Button) findViewById(R.id.btn_3_6);
        btn_4_1 = (Button) findViewById(R.id.btn_4_1);
        btn_4_2 = (Button) findViewById(R.id.btn_4_2);
        btn_4_3 = (Button) findViewById(R.id.btn_4_3);
        btn_4_4 = (Button) findViewById(R.id.btn_4_4);
        btn_4_5 = (Button) findViewById(R.id.btn_4_5);
        btn_4_6 = (Button) findViewById(R.id.btn_4_6);
        //endregion

        //region Set listener to buttons
        btn_1_1.setOnClickListener(this);
        btn_1_2.setOnClickListener(this);
        btn_1_3.setOnClickListener(this);
        btn_1_4.setOnClickListener(this);
        btn_1_5.setOnClickListener(this);
        btn_1_6.setOnClickListener(this);
        btn_2_1.setOnClickListener(this);
        btn_2_2.setOnClickListener(this);
        btn_2_3.setOnClickListener(this);
        btn_2_4.setOnClickListener(this);
        btn_2_5.setOnClickListener(this);
        btn_2_6.setOnClickListener(this);
        btn_3_1.setOnClickListener(this);
        btn_3_2.setOnClickListener(this);
        btn_3_3.setOnClickListener(this);
        btn_3_4.setOnClickListener(this);
        btn_3_5.setOnClickListener(this);
        btn_3_6.setOnClickListener(this);
        btn_4_1.setOnClickListener(this);
        btn_4_2.setOnClickListener(this);
        btn_4_3.setOnClickListener(this);
        btn_4_4.setOnClickListener(this);
        btn_4_5.setOnClickListener(this);
        btn_4_6.setOnClickListener(this);
        //endregion
    }

    @Override
    public void onClick(View v) {
        final int vId = v.getId();
        int vCourse = 0;
        String vDay = "";

        //region Определить день недели
        if (vId == R.id.btn_1_1 || vId == R.id.btn_2_1 || vId == R.id.btn_3_1 || vId == R.id.btn_4_1)
            vDay = DBHelper.KEY_MON;
        if (vId == R.id.btn_1_2 || vId == R.id.btn_2_2 || vId == R.id.btn_3_2 || vId == R.id.btn_4_2)
            vDay = DBHelper.KEY_TUE;
        if (vId == R.id.btn_1_3 || vId == R.id.btn_2_3 || vId == R.id.btn_3_3 || vId == R.id.btn_4_3)
            vDay = DBHelper.KEY_WED;
        if (vId == R.id.btn_1_4 || vId == R.id.btn_2_4 || vId == R.id.btn_3_4 || vId == R.id.btn_4_4)
            vDay = DBHelper.KEY_THU;
        if (vId == R.id.btn_1_5 || vId == R.id.btn_2_5 || vId == R.id.btn_3_5 || vId == R.id.btn_4_5)
            vDay = DBHelper.KEY_FRI;
        if (vId == R.id.btn_1_6 || vId == R.id.btn_2_6 || vId == R.id.btn_3_6 || vId == R.id.btn_4_6)
            vDay = DBHelper.KEY_SAT;
        //endregion

        //region Определить курс
        if (vId == R.id.btn_1_1 || vId == R.id.btn_1_2 || vId == R.id.btn_1_3 || vId == R.id.btn_1_4 || vId == R.id.btn_1_5 || vId == R.id.btn_1_6)
            vCourse = 1;
        if (vId == R.id.btn_2_1 || vId == R.id.btn_2_2 || vId == R.id.btn_2_3 || vId == R.id.btn_2_4 || vId == R.id.btn_2_5 || vId == R.id.btn_2_6)
            vCourse = 2;
        if (vId == R.id.btn_3_1 || vId == R.id.btn_3_2 || vId == R.id.btn_3_3 || vId == R.id.btn_3_4 || vId == R.id.btn_3_5 || vId == R.id.btn_3_6)
            vCourse = 3;
        if (vId == R.id.btn_4_1 || vId == R.id.btn_4_2 || vId == R.id.btn_4_3 || vId == R.id.btn_4_4 || vId == R.id.btn_4_5 || vId == R.id.btn_4_6)
            vCourse = 4;
        //endregion

        // создать интент
        Intent intent = new Intent(this, EditDaySchedule.class);
        // определить имя в зависимости от выбранного курса, необходимо для бд
        String name = "Общее расписание";
        switch (vCourse) {
            case 1:
                name += "1"; // Общее расписание1
                break;
            case 2:
                name += "2"; // Общее расписание2
                break;
            case 3:
                name += "3"; // Общее расписание3
                break;
            case 4:
                name += "4"; // Общее расписание4
                break;
        }

        intent.putExtra("name", name);
        intent.putExtra("day", vDay);
        startActivity(intent);
    }
}
