package com.x94.justwidgetdaily.AsyncTask;

import android.util.Log;

import com.x94.justwidgetdaily.Base.Config;
import com.x94.justwidgetdaily.Model.DailyScriptResult;
import com.x94.justwidgetdaily.Model.HttpResult;
import com.x94.justwidgetdaily.Util.TimeUtil;

public class DailyScriptAsyncTask extends GetAsyncTask
{
    private final String splitScriptHeaderA = "class=\\\\\"themeScrp\\\\\">";
    private final String splitScriptHeaderB = "<a href=";
    private final String splitScriptFromA = "class=\\\\\"b\\\\\">";
    private final String splitScriptFromB = "</p>\\\\r\\\\n";
    private final String remove0 = "</a>";
    private final String remove1 = "<em>";
    private final String remove2 = "</em>";
    private DailyScriptResult dailyScriptResult = new DailyScriptResult();
    private char punctuation = ' ';

    public DailyScriptAsyncTask(final String dailyScriptUrl)
    {
        url = Config.WOL_JW_URL + dailyScriptUrl + TimeUtil.getTodayScriptUrlDate();
    }

    @Override
    protected void onSuccess(final HttpResult res)
    {
        Log.d(Config.TAG, "DailyScriptAsyncTask onSuccess");
        delegate.onAsyncTaskSuccess(parseReturnValue(res));
    }

    private DailyScriptResult parseReturnValue(final HttpResult res)
    {
        getScriptHeader(res.responseBody);
        getScriptFrom(res.responseBody);
        return dailyScriptResult;
    }

    private void getScriptHeader(String resBody)
    {
        dailyScriptResult.scriptHeader = resBody.split(splitScriptHeaderA)[1].split(splitScriptHeaderB)[0]
                .replace(remove0, "").replace(remove1, "").replace(remove2, "").trim();
        punctuation = dailyScriptResult.scriptHeader.charAt(dailyScriptResult.scriptHeader.length()-1);
        dailyScriptResult.scriptHeader = dailyScriptResult.scriptHeader.substring(0, dailyScriptResult.scriptHeader.length()-2);
        Log.d(Config.TAG, "scriptHeader:" + dailyScriptResult.scriptHeader);
    }

    private void getScriptFrom(String resBody)
    {
        dailyScriptResult.scriptFrom = punctuation + resBody.split(splitScriptFromA)[1].split(splitScriptFromB)[0]
                .replace(remove0, "").replace(remove1, "").replace(remove2, "");
        Log.d(Config.TAG, "scriptFrom:" + dailyScriptResult.scriptFrom);
    }
}
