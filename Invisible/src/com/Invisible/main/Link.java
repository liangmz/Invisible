package com.Invisible.main;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.Invisible.socket.*;
import org.w3c.dom.ProcessingInstruction;

/**
 * Created by 001 on 2014/10/24.
 */
public class Link extends Activity {

    private I_Socket server = null;

    private SensorManager mManage = null;
    private Sensor mSensor = null;
    private SensorEventListener mListener = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.link);
        bind();
    }

    private void connection() {
        String ip = this.getIntent().getExtras().getString("ip");
        int port = this.getIntent().getExtras().getInt("port");

        try {
            server = new I_Socket(ip,port);
            server.start();
        }catch (Exception Ex){
            System.out.print(Ex.toString());
            server.Stop();
            finish();
        }
    }

    private void bind(){
        Button btn_exit = (Button)findViewById(R.id.btn_link_exit);//Exit 键
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                server.Stop();
                finish();
            }
        });

        Button btn_left = (Button)findViewById(R.id.btn_link_left);//11
        Button btn_right = (Button)findViewById(R.id.btn_link_right);//12
        Button btn_middle = (Button)findViewById(R.id.btn_link_middle);//13
        Button btn_space = (Button)findViewById(R.id.btn_link_space);//14
        Button btn_enter = (Button)findViewById(R.id.btn_link_enter);//15

        btn_left.setOnClickListener(new MyOnClickListerner(11));
        btn_right.setOnClickListener(new MyOnClickListerner(12));
        btn_middle.setOnClickListener(new MyOnClickListerner(13));
        btn_space.setOnClickListener(new MyOnClickListerner(14));
        btn_enter.setOnClickListener(new MyOnClickListerner(15));

    }

    protected class MyOnClickListerner implements View.OnClickListener {
        public Integer code ;
        MessageUnit msg;
        public  MyOnClickListerner(int code){
            this.code = code;
            msg = new MessageUnit();
            msg.msgType = 1;
            msg.clickCode = code;
        }

        @Override
        public void onClick(View view){
            try {
                I_Socket.queue.add(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed(){//BACK
        super.onBackPressed();
        server.Stop();
        this.finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        server.Stop();
    }

    @Override
    protected void onPause() {
        // ACTIVITY消失时取消监听
        super.onPause();
        try{
            mManage.unregisterListener(mListener);
        }catch (Exception ex){}
    }

    @Override
    protected  void onResume(){//恢复
        super.onResume();
        connection();
        CreateSensor();
        mManage.registerListener(mListener,mSensor,SensorManager.SENSOR_DELAY_GAME);
    }

    private double _x = 0,x = 0;
    private double _y = 0,y = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            _x = event.getX();
            _y = event.getY();
        }

        if(event.getAction() == MotionEvent.ACTION_UP){

        }

        if(event.getAction() == MotionEvent.ACTION_MOVE){
            x = event.getX();
            y = event.getY();

            MessageUnit msg = new MessageUnit();
            msg.msgType = 2;

            if (Math.abs(x - _x) > Math.abs(y - _y)) {//left right
                if(Math.abs(x - _x) > 50) {
                    if (x > _x) {
                        msg.direction = 1;

                    } else {
                        msg.direction = 3;
                    }
                    msg.distance = (int)Math.abs(x - _x);
                    I_Socket.queue.add(msg);
                }
            } else {//up down
                if(Math.abs(y - _y) > 50) {
                    if (y > _y) {
                        msg.direction = 2;
                    } else {
                        msg.direction = 0;
                    }
                    msg.distance = (int)Math.abs(y - _y);
                    I_Socket.queue.add(msg);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private void CreateSensor(){
        try{
            mManage = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        }
        catch (Exception ex){
           // errorString += ex.toString() + "传感器获取错误";
        }

        try {
            mSensor = mManage.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                        float x = sensorEvent.values[0];
                        float y = sensorEvent.values[1];
                        float z = sensorEvent.values[2];
                        doSomething(x, y, z);
                    }
                }
                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {
                }
            };
        }catch (Exception ex){
            //errorString += ex.toString() + "加速传感器错误";
        }
    }

    //基准方向坐标
    private float Reference_x = 0;
    private float Reference_y = 0;
    private float Reference_z = 0;

    private int angleLv0 = 15;
    private int angleLv1 = 40;
    private int angleLv2 = 65;
    private int angleLv3 = 90;

    private float _angle_lvx = 0;
    private float _angle_lvy = 0;

    public void doSomething(float x,float y,float z) {
        /*
        [x]手机左边在下，数值为正
        [y]手机下边在下，数值为正
        [z]手机屏幕朝上，数值为正
        */
        if (Math.abs(x - Reference_x) > 15) {
            int lvx = Math.abs(x - Reference_x) < angleLv0 ? 0 : (Math.abs(x - Reference_x) < angleLv1 ? 1 : (Math.abs(x - Reference_x) < angleLv2 ? 2 : 3));
            if (lvx != _angle_lvx) {

                MessageUnit msg = new MessageUnit();
                msg.msgType = 3;
                msg.angle = (int) Math.abs(x-Reference_x);
                msg.direction = x > (0 + Reference_x) ? 3 : 1;

                I_Socket.queue.add(msg);
                _angle_lvx = lvx;
            }
        }
        if (Math.abs(y - Reference_y) > 15) {
            int lvy = Math.abs(y - Reference_y) < angleLv0 ? 0 : (Math.abs(y - Reference_y) < angleLv1 ? 1 : (Math.abs(y - Reference_y) < angleLv2 ? 2 : 3));
            if (lvy != _angle_lvy) {
                MessageUnit msg = new MessageUnit();
                msg.msgType = 3;
                msg.angle = (int) Math.abs(y);
                msg.direction = y > (0 + Reference_y) ? 2 : 0;
                I_Socket.queue.add(msg);
                _angle_lvy = lvy;
            }
        }
    }
}


