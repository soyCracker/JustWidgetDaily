package com.x94.justwidgetdaily.Util;

import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

public class LanguageUtil
{
    public static String getSystemLanguageCode()
    {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            locale = LocaleList.getDefault().get(0);
        }
        else
        {
            locale = Locale.getDefault();
        }
        return locale.getLanguage() + "-" + locale.getCountry();
    }
}
