package com.example.myapplication.util;

import android.annotation.SuppressLint;

public class TimeGenerator {
    /*
     * 时间毫秒转换为 mm:ss格式
     * */
    @SuppressLint("DefaultLocale")
    public String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return hours > 0 ? String.format("%02dh%02dm%02ds", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }

}
