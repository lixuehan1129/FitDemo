package com.example.fitdemo.Personal;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.fitdemo.Adapter.ClassSelectAdapter;
import com.example.fitdemo.AutoProject.AppConstants;
import com.example.fitdemo.AutoProject.JDBCTools;
import com.example.fitdemo.AutoProject.SharePreferences;
import com.example.fitdemo.R;
import com.example.fitdemo.Utils.Class_select;
import com.example.fitdemo.Utils.DateUtils;
import com.example.fitdemo.Utils.StatusBarUtils;
import com.mysql.jdbc.Connection;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/12/15.
 */

public class PersonClassActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ClassSelectAdapter classSelectAdapter;
    private List<Class_select> class_selects = new ArrayList<>();
    private Class_select classSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_class);
        StatusBarUtils.setWindowStatusBarColor(PersonClassActivity.this, R.color.colorWhite);
        initView();
    }

    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.person_class_mainTool);
        toolbar.setTitle("课程");
        back(toolbar);

      //  TextView textView = (TextView) findViewById(R.id.person_class_tv);
      //  textView.setText("查看添加的课程");

        recyclerView = (RecyclerView) findViewById(R.id.person_class_rv);
        classSelectAdapter = new ClassSelectAdapter(class_selects);
        recyclerView.setAdapter(classSelectAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(PersonClassActivity.this));

        connectClass();

    }

    private void connectClass(){
        new Thread(){
            public void run(){
                try {
                    Connection conn = JDBCTools.getConnection();
                    if(conn != null) {
                        class_selects = new ArrayList<>();
                        Statement stmt = conn.createStatement();
                        String sql = "SELECT yu_bid FROM yu WHERE yu_user = " +
                                SharePreferences.getString(PersonClassActivity.this, AppConstants.USER_PHONE) +
                                " AND yu_time > " +
                                (DateUtils.IntTime(0)-1) +
                                " ORDER BY yu_time DESC";
                        ResultSet resultSet = stmt.executeQuery(sql);
                        while (resultSet.next()){
                            int yuBid = resultSet.getInt("yu_bid");
                            String sqlBid = "SELECT * FROM appoint WHERE appoint_bid = " +
                                    yuBid +
                                    " LIMIT 1";
                            Statement stmt1 = conn.createStatement();
                            ResultSet resultSetAppoint = stmt1.executeQuery(sqlBid);
                            if (resultSetAppoint.first()){
                                int bid = resultSetAppoint.getInt("appoint_bid");
                                int week = resultSetAppoint.getInt("appoint_week");
                                int time = resultSetAppoint.getInt("appoint_time");

                                String name = resultSetAppoint.getString("appoint_name");
                                String coach = resultSetAppoint.getString("appoint_coach");
                                String cover = resultSetAppoint.getString("appoint_cover");
                                int place = resultSetAppoint.getInt("appoint_place");

                                System.out.println("序列   "+bid);
                                String rTime;
                                if(time == 1){
                                    rTime = "08:30-10:00";
                                }else {
                                    rTime = "14:30-16:00";
                                }
                                classSelect = new Class_select(name+bid, coach,rTime,cover,
                                        0,place,bid,week);
                                class_selects.add(classSelect);
                            }
                            resultSetAppoint.close();
                            stmt1.close();

                        }
                        Message message = new Message();
                        message.what = 117;
                        handler.sendMessage(message);


                        resultSet.close();
                        JDBCTools.releaseConnection(stmt,conn);
                    }
                }catch (java.sql.SQLException e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what){
                case 117:{
                    if(class_selects.size() != 0){
                        for(int i = 0; i<class_selects.size(); i++){
                            classSelectAdapter.addDataAt(0,class_selects.get(i));
                        }
                    }
//                    if(classSelect != null){
//                        System.out.println("序列2    "+classSelect.getBid());
//                        classSelectAdapter.addDataAt(0,classSelect);
//                    }
                    break;
                }
                default:
                    break;
            }
            return false;
        }
    });

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
