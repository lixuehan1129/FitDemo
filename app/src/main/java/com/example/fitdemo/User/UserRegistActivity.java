package com.example.fitdemo.User;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;


import com.example.fitdemo.AutoProject.AppConstants;
import com.example.fitdemo.AutoProject.JDBCTools;
import com.example.fitdemo.R;
import com.example.fitdemo.Utils.CountDownTimerUtils;
import com.example.fitdemo.Utils.DateUtils;
import com.example.fitdemo.Utils.PhoneUtils;
import com.example.fitdemo.Utils.StatusBarUtils;
import com.example.fitdemo.AutoProject.Tip;
import com.mob.MobSDK;
import com.mysql.jdbc.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.options.RegisterOptionalUserInfo;
import cn.jpush.im.api.BasicCallback;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by 最美人间四月天 on 2018/12/8.
 */

public class UserRegistActivity extends AppCompatActivity{

    boolean hasFocus_pre_password = false;
    boolean hasFocus_pre_password_again = false;

    private TextInputLayout name_l, password_l, again_1, yan_1;
    private TextInputEditText name, password, again, yan;
    private Button register;
    private TextView textView;

    private boolean flag;   // 操作是否成功

    private CountDownTimerUtils mCountDownTimerUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_regist);
        StatusBarUtils.setWindowStatusBarColor(UserRegistActivity.this, R.color.colorWhite);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); //不弹出输入法
        MobSDK.init(this);
        initView();
        problem_jiaodian();
    }

    private void initView(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.user_regist_mainTool);
        back(toolbar);

        name_l = (TextInputLayout)findViewById(R.id.user_regist_number_layout);
        password_l = (TextInputLayout)findViewById(R.id.user_regist_password_layout);
        again_1 = (TextInputLayout)findViewById(R.id.user_regist_again_layout);
        yan_1 = (TextInputLayout)findViewById(R.id.user_regist_yan_layout);
        name = (TextInputEditText) findViewById(R.id.user_regist_number);
        password = (TextInputEditText) findViewById(R.id.user_regist_password);
        again = (TextInputEditText) findViewById(R.id.user_regist_again);
        yan = (TextInputEditText) findViewById(R.id.user_regist_yan);

        register = (Button) findViewById(R.id.user_regist_button);
        textView = (TextView) findViewById(R.id.user_regist_get);
        mCountDownTimerUtils = new CountDownTimerUtils(textView,60000,1000);

        initEdit();
        upload();
    }

    private void initMob(){
        // 在尝试读取通信录时以弹窗提示用户（可选功能）
        SMSSDK.setAskPermisionOnReadContact(true);
        EventHandler eventHandler = new EventHandler(){       // 操作回调
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);     // 注册回调接口
    }


    //按键监听
    private void upload(){

        initMob();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().length() == 11){
                    if(PhoneUtils.isMobileNO(name.getText().toString())){
                        CheckPhone(name.getText().toString());
                    }else {
                        Tip.showTip(UserRegistActivity.this,"手机号格式错误");
                    }
                }else {
                    Tip.showTip(UserRegistActivity.this,"手机号格式错误");
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(yan.getText().toString().length() == 4  && password.getText().toString().length()  > 5){
                    if(password.getText().toString().equals(again.getText().toString()) ){
                        //获取验证码
                        SMSSDK.submitVerificationCode("86", name.getText().toString(), yan.getText().toString());
                        flag = false;
                    } else {
                        Tip.showTip(UserRegistActivity.this, "两次密码不同");
                    }
                }else {
                    Tip.showTip(UserRegistActivity.this, "验证码或密码格式不正确");
                }

            }
        });
    }


    private void CheckPhone(final String phone){
        new Thread(){
            public void run(){
                Looper.prepare();//用于toast
                try{
                    Connection conn = JDBCTools.getConnection();
                    if(conn != null){
                        //首先判断手机号是否存在
                        Statement stmt = conn.createStatement(); //根据返回的Connection对象创建 Statement对象
                        String sql = "SELECT user_phone FROM user WHERE user_phone = '" +
                                phone +
                                "'";
                        ResultSet resultSet = stmt.executeQuery(sql);
                        if(resultSet.next()){
                            Tip.showTip(UserRegistActivity.this,"手机号已存在");
                        }else {
                            mCountDownTimerUtils.start();
                            SMSSDK.getVerificationCode("86", name.getText().toString()); // 发送验证码给号码的 phone 的手机
                        }
                        resultSet.close();
                        JDBCTools.releaseConnection(stmt,conn);
                    }else {
                        Tip.showTip(UserRegistActivity.this,"请检查网络");
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }.start();
    }

    private void update(){
        final ProgressDialog progressDialog = ProgressDialog.show(UserRegistActivity.this,"","正在注册",true);
        new Thread(){
            public void run(){
                Looper.prepare();//用于toast
                try{
                    Connection conn = JDBCTools.getConnection();
                    if(conn != null){
                        //首先判断手机号是否存在
                        Statement stmt = conn.createStatement(); //根据返回的Connection对象创建 Statement对象
                        String sql = "INSERT INTO user (user_phone,user_password,user_create_time,user_level,user_name,user_sex,user_sort,user_picture) VALUES (?,?,?,?,?,?,?,?)";
                        PreparedStatement preparedStatement = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                        preparedStatement.setString(1,name.getText().toString());
                        preparedStatement.setString(2,password.getText().toString());
                        preparedStatement.setString(3, DateUtils.StringData());
                        preparedStatement.setInt(4,1);
                        preparedStatement.setString(5,"智慧体育云用户");
                        preparedStatement.setInt(6,0);
                        preparedStatement.setInt(7,0);
                        preparedStatement.setString(8,"http://ty.tipass.com/images/head/head_name(1).PNG");
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                        JDBCTools.releaseConnection(stmt,conn);
                        progressDialog.dismiss();

                        Im();
                      //  finish();
                    }else {
                        Tip.showTip(UserRegistActivity.this,"请检查网络");
                        progressDialog.dismiss();
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
                Looper.loop();
            }
        }.start();
    }

    private void Im(){
        RegisterOptionalUserInfo registerOptionalUserInfo = new RegisterOptionalUserInfo();
        registerOptionalUserInfo.setNickname("智慧体育云用户");
        JMessageClient.register(name.getText().toString(), AppConstants.IM_PASS, registerOptionalUserInfo, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                finish();
            }
        });
    }



    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;

            if (result == SMSSDK.RESULT_COMPLETE) {
                // 如果操作成功
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    // 校验验证码，返回校验的手机和国家代码
                    Tip.showTip(UserRegistActivity.this, "验证成功");
                    update();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    // 获取验证码成功，true为智能验证，false为普通下发短信
                    Tip.showTip(UserRegistActivity.this, "验证码已发送");
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    // 返回支持发送验证码的国家列表
                }
            } else {
                // 如果操作失败
                if (flag) {
                    Tip.showTip(UserRegistActivity.this, "获取验证码失败");
                } else {
                    ((Throwable) data).printStackTrace();
                    Tip.showTip(UserRegistActivity.this, "验证码错误");
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();  // 注销回调接口
    }


    @SuppressLint("ClickableViewAccessibility")
    private void initEdit(){

        name_l.setCounterEnabled(true);  //设置可以计数
        name_l.setCounterMaxLength(11); //计数的最大值

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus_pre_password && hasFocus){
                    ((ScrollView)findViewById(R.id.register_scrollview)).fullScroll(ScrollView.FOCUS_DOWN);
                    hasFocus_pre_password = hasFocus;
                    password.requestFocus();
                }
            }
        });

        again.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus_pre_password_again && hasFocus){
                    ((ScrollView)findViewById(R.id.register_scrollview)).fullScroll(ScrollView.FOCUS_DOWN);
                    hasFocus_pre_password_again = hasFocus;
                    again.requestFocus();
                }
            }
        });

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((ScrollView)findViewById(R.id.register_scrollview)).fullScroll(ScrollView.FOCUS_DOWN);
                password.requestFocus();
                return false;
            }
        });

        again.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((ScrollView)findViewById(R.id.register_scrollview)).fullScroll(ScrollView.FOCUS_DOWN);
                again.requestFocus();
                return false;
            }
        });

        /*
        * 手机号
        * */
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name_l.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {
                String number = name.getText().toString();
                if(number.length()<1){
                    name_l.setError("手机号");
                }
            }
        });

        /*
        * 密码输入监听
        * */
        password.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password_l.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {
                String password1 = password.getText().toString();
                String password_again = again.getText().toString();
                if(password.length()>5){
                    if(password_again.equals(password1)){
                        again_1.setErrorEnabled(false);
                    }else {
                        if(password_again.length()>0)
                        {
                            again_1.setError("两次密码输入不一致");
                        }
                    }
                }else {
                    password_l.setError("密码错误(不少于6位)");
                }
            }
        });

        /*
        * 再次输入密码监听
        * */
        again.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                again_1.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {
                String password1 = password.getText().toString();
                String password_again = password.getText().toString();
                if(!password_again.equals(password1)){
                    password_l.setError("两次密码输入不一致");
                }
            }
        });


        //验证码
        yan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus_pre_password_again==false && hasFocus){
                    ((ScrollView)findViewById(R.id.register_scrollview)).fullScroll(ScrollView.FOCUS_DOWN);
                    hasFocus_pre_password_again = hasFocus;
                    yan.requestFocus();
                }
            }
        });

        yan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((ScrollView)findViewById(R.id.register_scrollview)).fullScroll(ScrollView.FOCUS_DOWN);
                yan.requestFocus();
                return false;
            }
        });

        yan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                yan_1.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {
                String number = yan.getText().toString();
                if(number.length()<1){
                    yan_1.setError("验证码");
                }
            }
        });

    }

    /*
  * 点击空白区域 Edittext失去焦点 关闭输入法
  * */
    private void problem_jiaodian() {
        final ScrollView scrollView = (ScrollView) findViewById(R.id.register_scrollview);
        scrollView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                return false;
            }
        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
