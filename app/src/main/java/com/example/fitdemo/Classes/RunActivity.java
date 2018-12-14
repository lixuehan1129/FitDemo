package com.example.fitdemo.Classes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitdemo.Adapter.ClassActivityAdapter;
import com.example.fitdemo.Adapter.ClassVideoAdapter;
import com.example.fitdemo.R;
import com.example.fitdemo.Subscribe.VideoPlayActivity;
import com.example.fitdemo.Utils.StatusBarUtils;
import com.example.fitdemo.AutoProject.Tip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/11/30.
 */

public class RunActivity extends AppCompatActivity {

    private RecyclerView recyclerView1, recyclerView2, recyclerView3;
    private ImageView imageView1, imageView2;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_activity);
        StatusBarUtils.setWindowStatusBarColor(RunActivity.this, R.color.colorWhite);
        initView();
    }

    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.class_activity_mainTool);
        toolbar.setTitle("跑步课程");
        back(toolbar);

        recyclerView1 = (RecyclerView) findViewById(R.id.class_activity_rv1);
        recyclerView2 = (RecyclerView) findViewById(R.id.class_activity_rv2);
        recyclerView3 = (RecyclerView) findViewById(R.id.class_activity_rv3);
        imageView1 = (ImageView) findViewById(R.id.class_activity_iv1);
        imageView2 = (ImageView) findViewById(R.id.class_activity_iv2);
        textView = (TextView) findViewById(R.id.class_activity_tv1);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(linearLayoutManager1);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView2.setLayoutManager(linearLayoutManager2);

        recyclerView3.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView3.setNestedScrollingEnabled(false);

        setData1();
        setData2();
        setData3();
        setClick();
    }

    private void setClick(){
        imageView1.setImageResource(R.mipmap.ic_touxiang41);
        imageView2.setImageResource(R.mipmap.ic_touxiang51);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tip.showTip(RunActivity.this,"一");
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tip.showTip(RunActivity.this,"二");
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RunActivity.this, HuDongActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setData1(){
        ArrayList<String> introduce = new ArrayList<>();
        ArrayList<Integer> image = new ArrayList<>();
        for(int i = 0;i < 4;i++){
            introduce.add("当前主播"+i);
        }
        image.add(R.mipmap.ic_run1111);
        image.add(R.mipmap.ic_run011);
        image.add(R.mipmap.ic_run21);
        image.add(R.mipmap.ic_run041);

        initData1(introduce,image);

    }

    private void setData2(){
        ArrayList<String> introduce = new ArrayList<>();
        ArrayList<Integer> image = new ArrayList<>();
        for(int i = 0;i < 4;i++){
            introduce.add("推荐课程"+i);
        }
        image.add(R.mipmap.ic_run031);
        image.add(R.mipmap.ic_run011);
        image.add(R.mipmap.ic_run21);
        image.add(R.mipmap.ic_run051);

        initData2(introduce,image);

    }

    private void setData3(){
        ArrayList<String> introduce = new ArrayList<>();
        ArrayList<Integer> image = new ArrayList<>();
        for(int i = 0;i < 4;i++){
            introduce.add("慢跑课程总览"+i);
        }
        image.add(R.mipmap.ic_run041);
        image.add(R.mipmap.ic_run011);
        image.add(R.mipmap.ic_run21);
        image.add(R.mipmap.ic_run051);

        initData3(introduce,image);

    }

    private void initData1(ArrayList<String> introduce,ArrayList<Integer> image){

        List<ClassActivityAdapter.Class_Activity> class_activities1 = new ArrayList<>();

        for (int i = 0; i<introduce.size(); i++){
            ClassActivityAdapter newData = new ClassActivityAdapter(class_activities1);
            ClassActivityAdapter.Class_Activity class_activity = newData.new Class_Activity(introduce.get(i),image.get(i));
            class_activities1.add(class_activity);
        }

        ClassActivityAdapter classActivityAdapter1 = new ClassActivityAdapter(class_activities1);
        recyclerView1.setAdapter(classActivityAdapter1);
        classActivityAdapter1.setOnItemClickListener(new ClassActivityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(RunActivity.this, BroadcastActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initData2(ArrayList<String> introduce,ArrayList<Integer> image){

        List<ClassActivityAdapter.Class_Activity> class_activities2 = new ArrayList<>();

        for (int i = 0; i<introduce.size(); i++){
            ClassActivityAdapter newData = new ClassActivityAdapter(class_activities2);
            ClassActivityAdapter.Class_Activity class_activity = newData.new Class_Activity(introduce.get(i),image.get(i));
            class_activities2.add(class_activity);
        }

        ClassActivityAdapter classActivityAdapter2 = new ClassActivityAdapter(class_activities2);
        recyclerView2.setAdapter(classActivityAdapter2);
        classActivityAdapter2.setOnItemClickListener(new ClassActivityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(RunActivity.this, VideoPlayActivity.class);
                intent.putExtra("video_add",position);
                startActivity(intent);
            }
        });

    }

    private void initData3(ArrayList<String> introduce,ArrayList<Integer> image){
        List<ClassVideoAdapter.Class_Video> class_videos = new ArrayList<>();
        for(int i = 0; i < introduce.size(); i++){
            ClassVideoAdapter newData = new ClassVideoAdapter(class_videos);
            ClassVideoAdapter.Class_Video class_video = newData.new Class_Video(introduce.get(i),image.get(i));
            class_videos.add(class_video);
        }
        ClassVideoAdapter classVideoAdapter = new ClassVideoAdapter(class_videos);
        recyclerView3.setAdapter(classVideoAdapter);
        classVideoAdapter.setOnItemClickListener(new ClassVideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(RunActivity.this, VideoPlayActivity.class);
                intent.putExtra("video_add",position);
                startActivity(intent);
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
