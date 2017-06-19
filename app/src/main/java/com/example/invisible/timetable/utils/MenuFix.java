package com.example.invisible.timetable.utils;

import android.content.Context;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;

public final class MenuFix {

    // показать 3 точки меню в активити (для устройств с аппаратной кнопкой меню)
    public static void show3Dots(Context context) {
        try {
            ViewConfiguration config = ViewConfiguration.get(context);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
