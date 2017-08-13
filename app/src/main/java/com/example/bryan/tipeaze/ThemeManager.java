package com.example.bryan.tipeaze;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bryan on 8/1/2017.
 */

public class ThemeManager {

    public static final String THEME_FOLDER = "user_themes.txt";

    public static final int DEF_THEME = UserThemes.DEFAULT_THEME;

    public static final String THEME_KEY = "KEY";

    public static int getTheme(Context context){

        int themeId = DEF_THEME;

        final SharedPreferences preferences = getPreferences(context);

       if(preferences != null)
           themeId = preferences.getInt(THEME_KEY, DEF_THEME);

    return themeId;
    }


    public static void changeTheme(int themeId, Context context){
        final SharedPreferences preferences = getPreferences(context);


        if(preferences!=null)
            preferences.edit().putInt(THEME_KEY, themeId).commit();


    }


    private static SharedPreferences getPreferences(Context context){
        return context.getSharedPreferences(THEME_FOLDER, Context.MODE_PRIVATE);
    }




    public static class UserThemes{
        public static final int DEFAULT_THEME = R.style.DefaultTheme;
        public static final int CRIMSON = R.style.Crimson;
    }


}
