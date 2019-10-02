package com.x94.justwidgetdaily.Widget;

import android.app.AlarmManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import com.x94.justwidgetdaily.Base.Config;
import com.x94.justwidgetdaily.Service.WidgetService;

public class JwdWidgetProvider extends AppWidgetProvider
{
    private int JOB_ID = 8787;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        schedulJob(context);
    }

    public void schedulJob(Context context)
    {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(context.JOB_SCHEDULER_SERVICE);

        if (true)
        {
            Log.d(Config.TAG, "schedulJob build");
            JobInfo jobInfo = new JobInfo.Builder(
                    JOB_ID
                    , new ComponentName(context.getPackageName()
                    , WidgetService.class.getName()))
                    .setPersisted(true)
                    .setPeriodic(AlarmManager.INTERVAL_HALF_HOUR)
                    .build();
            jobScheduler.schedule(jobInfo);
        }
        else
        {
            jobScheduler.cancel(JOB_ID);
        }
    }
}
