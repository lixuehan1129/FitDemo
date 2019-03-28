package com.example.fitdemo.Sport;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fitdemo.R;
import com.example.fitdemo.Step.StepActivity;
import com.example.fitdemo.ViewHelper.BaseFragment;
import com.today.step.lib.ISportStepInterface;
import com.today.step.lib.TodayStepManager;
import com.today.step.lib.TodayStepService;

import static com.example.fitdemo.Step.TSApplication.getApplication;


/**
 * Created by 最美人间四月天 on 2018/11/26.
 */

public class SportFragment extends BaseFragment {

    private static String TAG = "SportFragment";

    private static final int REFRESH_STEP_WHAT = 0;

    //循环取当前时刻的步数中间的间隔时间
    private long TIME_INTERVAL_REFRESH = 50000;

    private Handler mDelayHandler = new Handler(new TodayStepCounterCall());
    private int mStepSum;

    private ServiceConnection serviceConnection;

    private ISportStepInterface iSportStepInterface;

    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11, tv12, tv13, tv14;

    @Override
    public void onStart(){
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.sportfragment, null);
        initView(view); //界面
        return view;
    }

    @SuppressLint("NewApi")
    private void initView(View view){
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.sportFragment_mainTool);
        toolbar.setTitle("运动数据");

        //运动界面textView
        tv1 = (TextView) view.findViewById(R.id.sportFragment_data1);
        tv2 = (TextView) view.findViewById(R.id.sportFragment_data2);
        tv3 = (TextView) view.findViewById(R.id.sportFragment_data3);
        tv4 = (TextView) view.findViewById(R.id.sportFragment_data4);
        tv5 = (TextView) view.findViewById(R.id.sportFragment_data5);
        tv6 = (TextView) view.findViewById(R.id.sportFragment_data6);
        tv7 = (TextView) view.findViewById(R.id.sportFragment_data7);
        tv8 = (TextView) view.findViewById(R.id.sportFragment_data8);
        tv9 = (TextView) view.findViewById(R.id.sportFragment_data9);
        tv10 = (TextView) view.findViewById(R.id.sportFragment_data10);
        tv11 = (TextView) view.findViewById(R.id.sportFragment_data11);
        tv12 = (TextView) view.findViewById(R.id.sportFragment_data12);
        tv13 = (TextView) view.findViewById(R.id.sportFragment_data13);
        tv14 = (TextView) view.findViewById(R.id.sportFragment_data14);

        getStep();
        setData();

    }

    private void setData(){
        tv1.setText("165");
        tv2.setText("127");
        tv3.setText("15");
        tv4.setText("85");
        tv7.setText("1.7");
        tv8.setText("12");
        tv9.setText("0");
        tv10.setText("8");
        tv11.setText("2.3");
        tv12.setText("35");
        tv13.setText("30");
        tv14.setText("130");
    }

    private void getAllStep(){
        //获取所有步数列表
        if (null != iSportStepInterface) {
            try {
                String stepArray = iSportStepInterface.getTodaySportStepArray();
                Log.e(TAG, "stepArray : " + stepArray);
                //   tv6.setText("" + stepArray);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    public void getStep() {
        //初始化计步模块
        TodayStepManager.startTodayStepService(getApplication());
        //开启计步Service，同时绑定Activity进行aidl通信
        Intent intent = new Intent(getActivity(), TodayStepService.class);
        getActivity().startService(intent);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                //Activity和Service通过aidl进行通信
                iSportStepInterface = ISportStepInterface.Stub.asInterface(service);
                try {
                    mStepSum = iSportStepInterface.getCurrentTimeSportStep();
                    updateStepCount();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                mDelayHandler.sendEmptyMessageDelayed(REFRESH_STEP_WHAT, TIME_INTERVAL_REFRESH);

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    class TodayStepCounterCall implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_STEP_WHAT: {
                    //每隔500毫秒获取一次计步数据刷新UI
                    if (null != iSportStepInterface) {
                        int step = 0;
                        try {
                            step = iSportStepInterface.getCurrentTimeSportStep();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        if (mStepSum != step) {
                            mStepSum = step;
                            updateStepCount();
                        }
                    }
                    mDelayHandler.sendEmptyMessageDelayed(REFRESH_STEP_WHAT, TIME_INTERVAL_REFRESH);

                    break;
                }
            }
            return false;
        }
    }

    private void updateStepCount() {
        Log.e(TAG, "updateStepCount : " + mStepSum);
        tv5.setText("" + mStepSum);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(serviceConnection != null){
            getActivity().unbindService(serviceConnection);
        }


    }

}
