package net.bergby.qnomore.services;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import net.bergby.qnomore.MainActivity;
import net.bergby.qnomore.R;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by thomas on 27-Apr-17.
 */
public class OrderCountDown extends Service
{

    private final static String TAG = "BroadcastService";
    public static final String COUNTDOWN_BR = "net.bergby.qnomore.services.countdown_br";
    private Intent bi = new Intent(COUNTDOWN_BR);
    private int time = 0;
    private ArrayList<String> items;
    private double sum;

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
            items = (ArrayList<String>) bundle.get("items");
            sum = (Double) bundle.get("sum");
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
                String title = "Your order is being prepared!";
                int icon = R.drawable.ic_action_clock;
                int notificationId = 001;
                String timeLeft =
                        String.format(Locale.getDefault(), "%d min, %d sec",
                                TimeUnit.MILLISECONDS.toMinutes(l),
                                TimeUnit.MILLISECONDS.toSeconds(l) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))
                        );

                android.support.v4.app.NotificationCompat.Builder nBuilder =
                        new NotificationCompat.Builder(getBaseContext())
                                .setSmallIcon(icon)
                                .setContentTitle(title)
                                .setAutoCancel(true)
                                .setContentText(timeLeft);

                NotificationManager nNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                nNotifyMgr.notify(notificationId, nBuilder.build());
            }

            @Override
            public void onFinish()
            {
                int icon = R.drawable.ic_check_box;
                String content = "Your order is complete! Click to see pickup code.";
                String title = "Your order is complete!";

                StringBuilder expandedContent = new StringBuilder();
                for (String s: items)
                {
                    expandedContent.append(s).append(", ");
                }

                expandedContent.append("\n").append("The total sum is: ").append("€").append(sum);

                int notificationId = new Random().nextInt(100);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                // Removes the old notification, and changes it to the pickup notification
                notificationManager.cancel(1);


                android.support.v4.app.NotificationCompat.Builder nBuilder =
                        new NotificationCompat.Builder(getBaseContext())
                                .setSmallIcon(icon)
                                .setContentTitle(title)
                                .setContentText(content)
                                .setDefaults(Notification.DEFAULT_ALL)
                                .setStyle(new android.support.v4.app.NotificationCompat.BigTextStyle()
                                        .bigText(expandedContent));

                // Sets intent to the notification
                Intent notificationIntent = new Intent(getBaseContext(), MainActivity.class);
                notificationIntent.putExtra("notificationFragment", "fromIntent");

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getBaseContext());
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(notificationIntent);

                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                nBuilder.setContentIntent(resultPendingIntent);

                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                notificationManager.notify(notificationId, nBuilder.build());
                countDownTimer.cancel();
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
