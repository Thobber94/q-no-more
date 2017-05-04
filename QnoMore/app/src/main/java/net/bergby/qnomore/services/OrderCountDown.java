package net.bergby.qnomore.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by thomas on 27-Apr-17.
 */
public class OrderCountDown extends Service
{

    private final static String TAG = "BroadcastService";
    public static final String COUNTDOWN_BR = "net.bergby.qnomore.services.countdown_br";
    private Intent bi = new Intent(COUNTDOWN_BR);
    private int time = 0;

    private CountDownTimer countDownTimer = null;

    @Override
    public void onCreate()
    {
        super.onCreate();

        Log.i(TAG, "Starting the order-timer...");

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        countDownTimer.cancel();
        Log.i(TAG, "Timer cancelled");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Bundle bundle = intent.getExtras();

        if (!bundle.isEmpty())
        {
            time = (Integer) bundle.get("countDownTime");
            startCountDown();
        }

        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    private void startCountDown()
    {
        bi.putExtra("finished", false);
        sendBroadcast(bi);
        countDownTimer = new CountDownTimer(time, 1000)
        {
            @Override
            public void onTick(long l)
            {
                //Log.i(TAG, "Time until order is finished: " + (l / 1000));
                bi.putExtra("countdown", l);
                sendBroadcast(bi);
            }

            @Override
            public void onFinish()
            {
                bi.putExtra("finished", true);
                sendBroadcast(bi);
                countDownTimer.cancel();
                //Log.i(TAG, "Order ready");
            }
        };
        countDownTimer.start();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
