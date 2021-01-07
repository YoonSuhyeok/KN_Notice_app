package com.example.noticekangwon;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeSet {
    public static final String LIGHT_MODE = "light";
    public static final String DARK_MODE = "dark";

    public static void applyTheme(String themeColor) {
        switch (themeColor) {
            case LIGHT_MODE:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;

            case DARK_MODE:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
    }
}
