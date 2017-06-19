package com.example.invisible.timetable;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.invisible.timetable.database.DBHelper;
import com.example.invisible.timetable.utils.MenuFix;

import java.util.ArrayList;

/**
 * Активити общего расписания
 * Представляет из себя таблицу всех дней недели для всех курсов
 * Вызывает EditDaySchedule для редактирования каждого дня отдельно
 */

public class GeneralSchedule extends AppCompatActivity implements View.OnClickListener {

    private final int DIALOG_CLEAR = 1;
    private DBHelper dbHelper;

    private SparseArray<ArrayList<Button>> buttons;
    private static final int BUTTON_IDS[][] = {
            {R.id.btn_general_1_1, R.id.btn_general_1_2, R.id.btn_general_1_3, R.id.btn_general_1_4, R.id.btn_general_1_5, R.id.btn_general_1_6},
            {R.id.btn_general_2_1, R.id.btn_general_2_2, R.id.btn_general_2_3, R.id.btn_general_2_4, R.id.btn_general_2_5, R.id.btn_general_2_6},
            {R.id.btn_general_3_1, R.id.btn_general_3_2, R.id.btn_general_3_3, R.id.btn_general_3_4, R.id.btn_general_3_5, R.id.btn_general_3_6},
            {R.id.btn_general_4_1, R.id.btn_general_4_2, R.id.btn_general_4_3, R.id.btn_general_4_4, R.id.btn_general_4_5, R.id.btn_general_4_6}
    };

    private final DialogInterface.OnClickListener clearDlgListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    dbHelper.wipeGeneralSchedule();
                    Toast.makeText(GeneralSchedule.this, "Общее расписание очищено.", Toast.LENGTH_SHORT).show();
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_schedule);

        setTitle("General schedule");

        dbHelper = new DBHelper(this);

        MenuFix.show3Dots(this);

        buttons = new SparseArray<>();
        for (int course = 1; course <= BUTTON_IDS.length; course++) {
            ArrayList<Button> btnsCourse = new ArrayList<>();
            for (int btn_id : BUTTON_IDS[course-1]) {
                Button btn = (Button) findViewById(btn_id);
                assert btn != null;
                btn.setOnClickListener(this);
                btnsCourse.add(btn);
            }
            buttons.put(course, btnsCourse);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_CLEAR) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Внимание!");
            adb.setMessage("Полностью очистить общее расписание?");
            adb.setIcon(android.R.drawable.ic_notification_clear_all);
            adb.setPositiveButton("ДА", clearDlgListener);
            adb.setNegativeButton("ОТМЕНА", clearDlgListener);
            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_general_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_wipe_schedule:
                showDialog(DIALOG_CLEAR);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        String name = "Общее расписание";
        String vDay = "";

        dayfound:
        for (int course = 1; course <= BUTTON_IDS.length; course++) {
            //noinspection SuspiciousMethodCalls
            if (!buttons.get(course).contains(v))
                continue;
            for (int dayOfWeek = 0; dayOfWeek < DBHelper.DAYS.length; dayOfWeek++) {
                if (buttons.get(course).get(dayOfWeek) == v) {
                    name += course;
                    vDay = DBHelper.DAYS[dayOfWeek];
                    break dayfound;
                }
            }
        }

        // создать интент
        Intent intent = new Intent(this, EditDaySchedule.class);
        intent.putExtra("name", name);
        intent.putExtra("day", vDay);
        startActivity(intent);
    }
}
