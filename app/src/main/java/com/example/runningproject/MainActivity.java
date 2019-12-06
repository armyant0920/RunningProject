package com.example.runningproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    public static LinearLayout square;
    public static TextView numText;
    public static TextView secText;
    public static TextView minText;
    public static TextView goalNumber;
    public static TextView percentageText;
    public static SeekBar seekBar;
    public static int sec,min;    //時間變數
    public static int count,goalnum;
    public static double percentage;

    static Timer timer;
    Button reset,setGoal;
    ImageButton playButton;
    public static Boolean pause=true;
    int runPic;
    Drawable d1=Drawable.createFromPath("@android:drawable/sample1");

    private View.OnClickListener buttonlistener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            { case R.id.Reset:
                resetEvent();
                break;
                case R.id.PlayButton  :
                    pauseEvent();
                    break;
                case R.id.setGoal  :
                    setEvent();
                    break;
            }
        }
    };

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //主畫面物件設定
        square = findViewById(R.id.square);
        numText=findViewById(R.id.numText);
        secText=findViewById(R.id.secText);
        minText=findViewById(R.id.minText);
        goalNumber=findViewById(R.id.goalNumber);
        percentageText=findViewById(R.id.percentageText);
        seekBar=findViewById(R.id.seekBar);
        seekBar.setClickable(false);
        seekBar.setFocusable(false);
        seekBar.setEnabled(false);

        Intent i = new Intent(this, ShakeService.class);
        startService(i);
        //Start Service

        timer = new Timer();//時間函示初始化
        //這邊開始跑時間執行緒
        final  TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //秒數設定
                        if(pause==false){
                            sec++;
                            if(sec%60==0){
                                min++;
                                minText.setText(String.valueOf(min));
                            }
                            sec=sec%60;
                            secText.setText(String.valueOf(sec));
                        }
                    }
                });
            }
        };
        timer.schedule(task, 1000, 1000);//時間在幾毫秒過後開始以多少毫秒執行

        //按鈕統一設定
        setGoal=findViewById(R.id.setGoal);
        reset=findViewById(R.id.Reset);

        playButton=findViewById(R.id.PlayButton);
        reset.setOnClickListener(buttonlistener);
        playButton.setOnClickListener(buttonlistener);
        setGoal.setOnClickListener(buttonlistener);

        //歸零設定


       // gifSetting();


    }

    private void gifSetting(){
        //GIF設定
        GifImageView Runner = findViewById(R.id.Runner);
        try {
            GifDrawable gifDrawable1 = new GifDrawable(getResources(), runPic);
            Runner.setImageDrawable(gifDrawable1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //GIF設定結束
    }
    private void setEvent(){

        count=0;
        sec=0;
        min=0;
        percentageText.setText("0");
        numText.setText(String.valueOf(count));
        secText.setText(String.valueOf(count));
        minText.setText(String.valueOf(count));
        final View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.option,null);


        View.OnClickListener picListener= new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.pic1:
                        runPic=R.drawable.sample1;
                        gifSetting();

                        break;
                    case R.id.pic2:
                        runPic=R.drawable.sample2;
                        gifSetting();

                        break;

                }

            }
        };
        //設定圖片點擊監聽
        ImageView pic1,pic2;
        pic1=view.findViewById(R.id.pic1);
        pic2=view.findViewById(R.id.pic2);
        pic1.setOnClickListener(picListener);
        pic2.setOnClickListener(picListener);

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("設定")
                .setView(view)
                .setPositiveButton("check", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = (EditText) view.findViewById(R.id.setGoal);
                        String goal = editText.getText().toString();
                        if(TextUtils.isEmpty(goal)){
                            Toast.makeText(getApplicationContext(),"尚未設定目標", Toast.LENGTH_SHORT).show();
                        } else {
                            goalnum=Integer.parseInt(goal);
                            goalNumber.setText(String.valueOf(goalnum));

                        }
                    }
                })
                .show();



    }
    private void resetEvent(){
        count=0;
        sec=0;
        min=0;
        percentage=0;
        percentageText.setText("0");
        seekBar.setProgress(0);
        numText.setText(String.valueOf(count));
        secText.setText(String.valueOf(count));
        minText.setText(String.valueOf(count));
    }

    private void pauseEvent(){
        if(pause ==true)
        {if(goalnum==0)
        {Toast.makeText(this,"必須有目標值才能開始",Toast.LENGTH_SHORT).show();}
            else{pause=false;
            playButton.setImageResource(android.R.drawable.ic_media_pause);}
        }
        else {
            pause=true;

            playButton.setImageResource(android.R.drawable.ic_media_play);

        }
        Log.d("pause",String.valueOf(pause));
        Log.d("Picpath1",String.valueOf(playButton.getDrawable()));

    }
}