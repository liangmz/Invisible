package com.Invisible.socket;

/**
 * Created by 001 on 2014/10/24.
 */
public class MessageUnit {
    /*
    msgType:
    0:连接保持
    1:点击命令
    2:触控命令
    3:传感命令
    * */
    public int msgType;//[0,255]
    public Integer data1;//[0,255]

    public int clickCode;
    public int angle;//[0,65535] , 角度
    public int direction;//[0,255] , 方向 0123上右下左
    public int distance;//距离

    public byte[] GetByte() {
        int length = -1;
        switch (msgType){
            case 0:length = 2;break;
            case 1:length = 3;break;
            case 2:length = 7;break;
            case 3:length = 5;break;
            default:length = 0 ;break;
        }
        byte[] data = new byte[length];

        data[0]= (byte)length;
        data[1] = (byte)msgType;
        if(msgType == 1){
            data[2] = (byte)clickCode;
        }
        else if(msgType == 2) {
            data[2] = (byte) direction;
            data[3] = (byte) (distance & 255);
            data[4] = (byte) ((distance & 65280) >> 8);
            data[5] = (byte) ((distance & 16711680) >> 16);
            data[6] = (byte) ((distance & 2130706432) >> 24);
        }
        else if(msgType == 3) {
            data[2] = (byte) direction;
            data[3] = (byte) (angle & 255);
            data[4] = (byte) ((angle & 65280) >> 8);
        }
        return data;
    }
}


