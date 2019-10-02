package com.x94.justwidgetdaily.AsyncTask;

import android.util.Log;

import com.x94.justwidgetdaily.Base.Config;
import com.x94.justwidgetdaily.Model.HttpResult;

public class DailyScriptUrlAsyncTask extends GetAsyncTask
{
    private final String urlSplitStrA = "<a class=\"todayNav\" href=\"/";
    private final String urlSplitStrB = "\"><span class=\"icon\">";
    private final String slashStr = "/";

    public DailyScriptUrlAsyncTask(final String languageCode)
    {
        url = Config.WOL_JW_URL;
        this.languageCode = languageCode;
    }

    @Override
    protected void onSuccess(final HttpResult res)
    {
        Log.d(Config.TAG, "DailyScriptUrlAsyncTask onSuccess");
        delegate.onAsyncTaskSuccess(parseReturnValue(res));
    }

    private String parseReturnValue(final HttpResult res)
    {
        String result = slashStr;
        String[] tempArray = res.responseBody.split(urlSplitStrA)[1].split(urlSplitStrB)[0].split(slashStr);
        for(int i=1;i<tempArray.length;i++)
        {
            result += (tempArray[i] + slashStr);
        }
        return result;
    }
}
