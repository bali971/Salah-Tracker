package nbsolution.muslim.app.Activities;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import nbsolution.muslim.app.prayer.PrayerTimesLoader;

public class DateChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, DateService.class);
        // System.out.println("working12-----12r");
        context.startService(serviceIntent);
    }
}


