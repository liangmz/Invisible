package com.Invisible.socket;

import java.security.PublicKey;

/**
 * Created by 001 on 2014/10/29.
 */
public class Keep extends Thread {
    private boolean isInterrupted;

    public Keep(){
        isInterrupted = true;
    }

    public void run() {
        while (isInterrupted) {
            try {
                MessageUnit msg = new MessageUnit();
                msg.msgType = 0;
                I_Socket.queue.add(msg);
                sleep(10000);
            } catch (Exception Ex) {
            }
        }
    }

    @Override
    public void interrupt(){
        super.interrupt();
        this.isInterrupted = false;
    }
}
