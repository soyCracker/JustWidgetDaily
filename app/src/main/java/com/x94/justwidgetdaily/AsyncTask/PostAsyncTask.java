package com.x94.justwidgetdaily.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.x94.justwidgetdaily.Base.Config;
import com.x94.justwidgetdaily.Model.HttpResult;
import com.x94.justwidgetdaily.Util.HttpUtil;

public abstract class PostAsyncTask extends AsyncTask<Void, Void, Void>
{
    String url = "";
    String body = "";
    String contentType = "application/json; charset=utf-8";
    final int timeout = 30;
    String languageCode = "en-US";
    HttpResult res = null;
    AsyncTaskResultDelegate delegate;
    boolean isSuccess = false;

    public void setDelegate(AsyncTaskResultDelegate delegate)
    {
        this.delegate = delegate;
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        Log.d(Config.TAG, "PostAsyncTask doInBackground");
        doPost();
        return null;
    }

    private void doPost()
    {
        Log.d(Config.TAG, "PostAsyncTask doPost");
        Log.d(Config.TAG, "post req url:"+url);
        Log.d(Config.TAG, "post req body:"+body);
        res = HttpUtil.post(url, body, contentType, timeout, languageCode);
        Log.d(Config.TAG, "post res code:"+res.responseCode);
        Log.d(Config.TAG, "post res body:"+res.responseBody);
        Log.d(Config.TAG, "PostAsyncTask doPost res length:"+res.responseBody.length());
        if(res.isHandshakeOk)
        {
            isSuccess = true;
        }
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);
        if (delegate == null) return;
        Log.d(Config.TAG, "PostAsyncTask onPostExecute");
        Log.d(Config.TAG, "PostAsyncTask res:"+res.responseBody);
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
