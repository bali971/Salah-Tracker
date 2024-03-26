package nbsolution.muslim.app.Activities;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import nbsolution.muslim.app.prayer.PrayerTimesLoader;

public class DateService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("working12-----12s");
        // Call your function here
        new PrayerTimesLoader(this).execute();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
