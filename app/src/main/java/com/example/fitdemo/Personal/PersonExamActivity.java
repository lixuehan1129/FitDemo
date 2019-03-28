package com.example.fitdemo.Personal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.fitdemo.R;
import com.example.fitdemo.Step.StepActivity;
import com.example.fitdemo.Utils.StatusBarUtils;
import com.today.step.lib.ISportStepInterface;
import com.today.step.lib.TodayStepManager;
import com.today.step.lib.TodayStepService;


/**
 * Created by 最美人间四月天 on 2018/12/15.
 */

public class PersonExamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_exam);
        StatusBarUtils.setWindowStatusBarColor(PersonExamActivity.this, R.color.colorWhite);
        initView();
    }

    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.person_exam_mainTool);
        toolbar.setTitle("健康体检");
        back(toolbar);

        TextView textView = (TextView) findViewById(R.id.person_exam_tv);
        textView.setText("个人健康情况");
    }


    //返回注销事件
    private void back(Toolbar toolbar){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
