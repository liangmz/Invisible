package com.Invisible.socket;

import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

import android.widget.Toast;
import com.Invisible.main.LogOn;
import com.Invisible.socket.MessageUnit;
/**
 * Created by 001 on 2014/10/24.
 * 4.0开始主线程无法访问网络
 */

public class I_Socket extends Thread {
    public static BlockingQueue <MessageUnit> queue = new LinkedBlockingQueue<MessageUnit>();

    private Keep keepCon = null;
    private boolean isInterrupted;
    private String ip;
    private int port;

    private DatagramSocket server;
    private InetAddress addr;

    public I_Socket(String ip , int port) {
        this.ip = ip;
        this.port = port;
        isInterrupted = true;
        queue.clear();

        keepCon = new Keep();
    }

    public void run(){
        while (isInterrupted){
            try{
                server = new DatagramSocket();
                addr= InetAddress.getByName(ip);
                //SendData("connected");

                keepCon.start();
                while (isInterrupted){
                    try {
                        MessageUnit msg = (MessageUnit) queue.take();
                        SendData(msg.GetByte());
                    }catch (InterruptedException Exp){
                    }
                }
                //SendData("0");
                server.close();
            }catch (Exception Ex) {
                try {
                    server.close();
                }catch (Exception ex){
                }
                server = null;
            }
        }
    }

    private void SendData(byte[] Data) throws Exception{
        DatagramPacket packet = new DatagramPacket(Data,Data.length , addr , port);
        server.send(packet);
    }
    private void SendData(String data) throws Exception{
        byte[] Data = data.getBytes();
        DatagramPacket packet = new DatagramPacket(Data,Data.length , addr , port);
        server.send(packet);
    }

    public void Stop(){
        isInterrupted = false;
        keepCon.interrupt();
        this.interrupt();
    }
}




