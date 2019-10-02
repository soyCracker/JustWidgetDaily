package com.x94.justwidgetdaily.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;
import com.x94.justwidgetdaily.Base.Config;
import com.x94.justwidgetdaily.Model.HttpResult;
import com.x94.justwidgetdaily.Util.HttpUtil;

public abstract class GetAsyncTask extends AsyncTask<Void, Void, Void>
{
    String url = "";
    HttpResult res;
    final int timeout = 30;
    String languageCode = "en-US";
    AsyncTaskResultDelegate delegate;
    boolean isSuccess = false;

    public void setDelegate(AsyncTaskResultDelegate delegate)
    {
        this.delegate = delegate;
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        doGet();
        return null;
    }

    private void doGet()
    {
        Log.d(Config.TAG, "GetAsyncTask doPost");
        Log.d(Config.TAG, "get req url:"+url);
        res = HttpUtil.get(url, languageCode, timeout);
        Log.d(Config.TAG, "get res code:"+res.responseCode);
        Log.d(Config.TAG, "get res body:"+res.responseBody);
        isSuccess = true;
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);
        if (delegate == null) return;
        if(isSuccess)
        {
            onSuccess(res);
        }
        else
        {
            delegate.onAsyncTaskError(res.responseCode, res.responseBody);
        }
    }

    protected abstract void onSuccess(final HttpResult res);
}
