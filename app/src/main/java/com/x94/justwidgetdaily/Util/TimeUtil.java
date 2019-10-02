package com.x94.justwidgetdaily.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeUtil
{
    public static String getTodayScriptUrlDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String dateString = sdf.format(Calendar.getInstance().getTime());
        return dateString;
    }

    public static String getTimeyyyyMMddmm()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = sdf.format(Calendar.getInstance().getTime());
        return dateString;
    }
}
