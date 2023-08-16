package org.tvapp.utils;


import android.content.Context;

public class DisplayUtils {
    public static int getScreenWidth(Context context,int percent) {
        int width = context.getResources().getDisplayMetrics().widthPixels;
        return (width * percent) / 100;
    }
    public static int getScreenHeight(Context context,int percent) {
        int height = context.getResources().getDisplayMetrics().heightPixels;
        return (height * percent) / 100;
    }
}
