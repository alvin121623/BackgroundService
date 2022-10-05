package application.aku.backgroundservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView tvon, tvoff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvon = findViewById(R.id.tvon);
        tvon.setOnClickListener(v -> {
            if (!isServiceRunning(getApplicationContext(), MyService.class)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    startForegroundService(new Intent(getApplicationContext(), MyService.class));
                }else {
                    startService(new Intent(getApplicationContext(), MyService.class));
                }
            }
        });

        tvoff = findViewById(R.id.tvoff);
        tvoff.setOnClickListener(v -> {
            stopService(new Intent(getApplicationContext(), MyService.class));
        });
    }

    public boolean isServiceRunning(Context context, Class<?> serviceClas) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClas.getName())) {
                return true;
            }
        }

        return false;
    }
}
