package com.x94.justwidgetdaily.Service;

import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.RemoteViews;
import com.x94.justwidgetdaily.AsyncTask.AsyncTaskResultDelegate;
import com.x94.justwidgetdaily.AsyncTask.DailyScriptAsyncTask;
import com.x94.justwidgetdaily.AsyncTask.DailyScriptUrlAsyncTask;
import com.x94.justwidgetdaily.Base.Config;
import com.x94.justwidgetdaily.Model.DailyScriptResult;
import com.x94.justwidgetdaily.R;
import com.x94.justwidgetdaily.Util.LanguageUtil;
import com.x94.justwidgetdaily.Widget.JwdWidgetProvider;

public class WidgetService extends JobService
{
    @Override
    public boolean onStartJob(JobParameters params)
    {
        Log.d(Config.TAG, "WidgetService onStartJob");
        getDailyScriptUrl(LanguageUtil.getSystemLanguageCode());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params)
    {
        Log.d(Config.TAG, "WidgetService onStopJob");
        return false;
    }

    private void getDailyScriptUrl(final String languageCode)
    {
        DailyScriptUrlAsyncTask task = new DailyScriptUrlAsyncTask(languageCode);
        task.setDelegate(new AsyncTaskResultDelegate() {
            @Override
            public void onAsyncTaskSuccess(Object... params)
            {
                getDailyScript((String)params[0]);
            }

            @Override
            public void onAsyncTaskError(int resultCode, String errorMessage)
            {
                //updateWidget(errorMessage, ""+resultCode);
            }
        });
        task.execute();
    }

    private void getDailyScript(final String dailyScriptUrl)
    {
        DailyScriptAsyncTask task = new DailyScriptAsyncTask(dailyScriptUrl);
        task.setDelegate(new AsyncTaskResultDelegate() {
            @Override
            public void onAsyncTaskSuccess(Object... params)
            {
                DailyScriptResult res = (DailyScriptResult)params[0];
                updateWidget(res.scriptHeader, res.scriptFrom);
            }

            @Override
            public void onAsyncTaskError(int resultCode, String errorMessage)
            {
                //updateWidget(errorMessage, ""+resultCode);
            }
        });
        task.execute();
    }

    private void updateWidget(final String scriptHeader, final String scriptFrom)
    {
        PackageManager pm = getPackageManager();
        String packageName = Config.JW_PACKAGE_NAME;
        Intent launchIntent = pm.getLaunchIntentForPackage(packageName);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);

        // Get the layout for the App Widget and attach an on-click listener
        // to the button
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.widget_layout);
        views.setOnClickPendingIntent(R.id.widget_button, pendingIntent);
        views.setTextViewText(R.id.scriptHeader_text, scriptHeader);
        views.setTextViewText(R.id.scriptFrom_text, scriptFrom);

        // Tell the AppWidgetManager to perform an update on the current app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        appWidgetManager.updateAppWidget(new ComponentName(this, JwdWidgetProvider.class), views);
    }
}
