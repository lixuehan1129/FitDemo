package com.example.fitdemo.Step;

import android.content.Context;
import android.content.Intent;

import com.example.fitdemo.Personal.PersonExamActivity;
import com.example.fitdemo.Sport.SportFragment;
import com.today.step.lib.BaseClickBroadcast;

public class MyReceiver extends BaseClickBroadcast {

    private static final String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        TSApplication tsApplication = (TSApplication) context.getApplicationContext();
        if (!tsApplication.isForeground()) {
            Intent mainIntent = new Intent(context, PersonExamActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainIntent);
        } else {

        }
    }
}
