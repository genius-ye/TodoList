package com.genius.todolist;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.genius.utils.Logger;

import java.io.File;

/**
 * Created by 112 on 2016/3/15.
 */
public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    /** 应用存储路径 **/
    public static final String APP_DIR=Environment.getExternalStorageDirectory()+ File.separator+"TodoList";
    public static final String BACKUP_FILE_PATH= APP_DIR+File.separator+"backup.backup";

    public static SharedPreferences sharedPreferences;

    /**
     * 屏幕的高
     **/
    public static float SCREEN_HEIGHT;
    /**
     * 屏幕的宽
     **/
    public static float SCREEN_WIDTH;

    @Override
    public void onCreate() {
        super.onCreate();

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm= (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display= wm.getDefaultDisplay();
//        SCREEN_HEIGHT=display.getHeight();
//        SCREEN_WIDTH=display.getWidth();
        wm.getDefaultDisplay().getMetrics(dm);
        SCREEN_WIDTH = dm.widthPixels ;
        SCREEN_HEIGHT = dm.heightPixels ;
        Logger.d(TAG, SCREEN_WIDTH + "*" + SCREEN_HEIGHT);

        sharedPreferences = getSharedPreferences("todolist", MODE_PRIVATE);

        File file = new File(APP_DIR);
        if(!file.exists())
        {
            file.mkdir();
        }
    }
}
