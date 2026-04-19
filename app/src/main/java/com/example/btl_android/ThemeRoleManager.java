package com.example.btl_android;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public final class ThemeRoleManager {

    public static final String ROLE_PATIENT = "patient";
    public static final String ROLE_DOCTOR = "doctor";

    private static final String PREF_NAME = "role_theme_prefs";
    private static final String KEY_PATIENT_DARK = "patient_dark_mode";
    private static final String KEY_DOCTOR_DARK = "doctor_dark_mode";

    private ThemeRoleManager() {
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    private static String keyForRole(String role) {
        if (ROLE_DOCTOR.equals(role)) {
            return KEY_DOCTOR_DARK;
        }
        return KEY_PATIENT_DARK;
    }

    public static boolean isRoleDarkMode(Context context, String role) {
        return prefs(context).getBoolean(keyForRole(role), false);
    }

    public static void setRoleDarkMode(Context context, String role, boolean isDark) {
        prefs(context)
                .edit()
                .putBoolean(keyForRole(role), isDark)
                .apply();
    }

    public static void applyRoleTheme(Context context, String role) {
        boolean isDark = isRoleDarkMode(context, role);
        AppCompatDelegate.setDefaultNightMode(
                isDark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }
}
