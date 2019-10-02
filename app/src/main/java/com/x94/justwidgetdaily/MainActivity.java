package com.x94.justwidgetdaily;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.x94.justwidgetdaily.Base.Config;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(Config.TAG, "LOVE NEVER FAILS");
    }
}
