package ua.azbest.csstatservice.model;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    private static final String ACTIVE_PICTURE = "active_picture";

    public static void setActivePicture(Context context, int id) {
        final SharedPreferences settings = context.getSharedPreferences(ACTIVE_PICTURE, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putInt(ACTIVE_PICTURE, id);
        prefEditor.apply();
    }

}
