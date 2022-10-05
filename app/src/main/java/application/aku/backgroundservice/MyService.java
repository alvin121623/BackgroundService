package application.aku.backgroundservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {
    CountDownTimer countDownTimer;
    private long startTime = 5000;
    private final long interval = 1 * 1000;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        countDownTimer = new MyCountDownTimer(startTime, interval);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startForegroundService(new Intent(getApplicationContext(), MyService.class));
        }else {
            startService(new Intent(getApplicationContext(), MyService.class));
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    "XXX69", "XXX69", NotificationManager.IMPORTANCE_LOW
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }

        Intent intent1 = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent1,0);
        Notification notification = new NotificationCompat.Builder(this, "XXX69")
                .setContentTitle("Service On")
                .setContentText("Your service is active")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);

        countDownTimer.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        stopSelf();
        countDownTimer.cancel();
        super.onDestroy();
    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            countDownTimer.cancel();
            try {
                MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bell);
                mPlayer.start();
                mPlayer.setOnCompletionListener(mp -> mp.release());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            countDownTimer.start();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.d("millis", ""+millisUntilFinished);
        }
    }
}
