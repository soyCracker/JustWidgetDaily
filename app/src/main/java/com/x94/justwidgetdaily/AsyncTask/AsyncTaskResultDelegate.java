package com.x94.justwidgetdaily.AsyncTask;

public interface AsyncTaskResultDelegate
{
    void onAsyncTaskSuccess(Object... params);
    void onAsyncTaskError(int resultCode, String errorMessage);
}
