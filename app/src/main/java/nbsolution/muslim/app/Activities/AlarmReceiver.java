package nbsolution.muslim.app.Activities;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import nbsolution.muslim.app.R;
import nbsolution.muslim.app.SharedData.SharedClass;
import nbsolution.muslim.app.prayer.PrayerTimesLoader;
import nbsolution.muslim.app.prayer.PrayerUtils;

import java.util.Calendar;
import java.util.Random;

import static nbsolution.muslim.app.Helper.NotifsChannels.CHANNEL_1_ID;

public class AlarmReceiver extends BroadcastReceiver {
    private Random rng = new Random();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            // Set AGAIN the alarm when device booted.
            return;
        }
        Intent activityintent = new Intent(context, PrayerTimesLoader.class);
        AlarmNotif(intent, context, activityintent);
        System.out.println("Alarm Receiver");
    }

    private void AlarmNotif(Intent intent, Context context, Intent activityintent) {
        PendingIntent contentIntent1 = PendingIntent.getActivity(context, 1, activityintent, PendingIntent.FLAG_MUTABLE);
        PendingIntent contentIntent2 = PendingIntent.getActivity(context, 2, activityintent, PendingIntent.FLAG_MUTABLE);
        PendingIntent contentIntent3 = PendingIntent.getActivity(context, 3, activityintent, PendingIntent.FLAG_MUTABLE);
        PendingIntent contentIntent4 = PendingIntent.getActivity(context, 4, activityintent, PendingIntent.FLAG_MUTABLE);
        PendingIntent contentIntent5 = PendingIntent.getActivity(context, 5, activityintent, PendingIntent.FLAG_MUTABLE);

        int id1 = intent.getIntExtra("NotifId1", 0);
        int id2 = intent.getIntExtra("NotifId2", 0);
        int id3 = intent.getIntExtra("NotifId3", 0);
        int id4 = intent.getIntExtra("NotifId4", 0);
        int id5 = intent.getIntExtra("NotifId5", 0);

        Calendar today = Calendar.getInstance();
        int hh = today.get(Calendar.HOUR_OF_DAY);
        int mm = today.get(Calendar.MINUTE);
        // tomorrow add 1 day to calendar
        today.add(Calendar.DAY_OF_MONTH, 1);
        int tomorrow = today.get(Calendar.DAY_OF_MONTH);

//        ArrayList<String> NotifMessages = intent.getStringArrayListExtra("ArrayMessages");
//        ArrayList<String> NotifSalatMessages = intent.getStringArrayListExtra("ArraySalatMessages");
//        assert NotifMessages != null;
        // Notification settings
        int isFajarAllowed = SharedClass.getAlarmStatus(context, 1);
        int isZuharAllowed = SharedClass.getAlarmStatus(context, 2);
        int isAsarAllowed = SharedClass.getAlarmStatus(context, 3);
        int isMaghribAllowed = SharedClass.getAlarmStatus(context, 4);
        int isEshaAllowed = SharedClass.getAlarmStatus(context, 5);
        //Prayers Notifications
        //  int random2 = rng.nextInt(NotifSalatMessages.size());
        int hh1 = intent.getIntExtra("NotifTimeHH1", 0);
        int mm1 = intent.getIntExtra("NotifTimeMM1", 0);
        if (id1 != 0 && hh <= hh1 && mm <= mm1 + 15 && isFajarAllowed == 1) {
//            String message1 = NotifSalatMessages.get(random2);
            String title1 = intent.getStringExtra("NotifTitle1");
            createNotif(context, contentIntent1, id1, title1);
        }
        int hh2 = intent.getIntExtra("NotifTimeHH2", 0);
        int mm2 = intent.getIntExtra("NotifTimeMM2", 0);
        if (id2 != 0 && hh <= hh2 && mm <= mm2 + 15 && isZuharAllowed == 1) {
//            random2 = rng.nextInt(NotifSalatMessages.size());
//            String message2 = NotifSalatMessages.get(random2);
            String title2 = intent.getStringExtra("NotifTitle2");
            createNotif(context, contentIntent2, id1, title2);
        }
        int hh3 = intent.getIntExtra("NotifTimeHH3", 0);
        int mm3 = intent.getIntExtra("NotifTimeMM3", 0);
        if (id3 != 0 && hh <= hh3 && mm <= mm3 + 15 && isAsarAllowed == 1) {
//            random2 = rng.nextInt(NotifSalatMessages.size());
//            String message3 = NotifSalatMessages.get(random2);
            String title3 = intent.getStringExtra("NotifTitle3");
            createNotif(context, contentIntent3, id3, title3);
        }
        int hh4 = intent.getIntExtra("NotifTimeHH4", 0);
        int mm4 = intent.getIntExtra("NotifTimeMM4", 0);
        if (id4 != 0 && hh <= hh4 && mm <= mm4 + 15 && isMaghribAllowed == 1) {
//            random2 = rng.nextInt(NotifSalatMessages.size());
//            String message4 = NotifSalatMessages.get(random2);
            String title4 = intent.getStringExtra("NotifTitle4");
            createNotif(context, contentIntent4, id4, title4);
        }
        int hh5 = intent.getIntExtra("NotifTimeHH5", 0);
        int mm5 = intent.getIntExtra("NotifTimeMM5", 0);
        if (id5 != 0 && hh <= hh5 && mm <= mm5 + 15 && isEshaAllowed == 1) {
//            random2 = rng.nextInt(NotifSalatMessages.size());
//            String message5 = NotifSalatMessages.get(random2);
            String title5 = intent.getStringExtra("NotifTitle5");
            createNotif(context, contentIntent5, id5, title5);
        }

        if (id1 != 0) {
            PrayerUtils.scheduleNotification(context, 1, hh1, mm1, tomorrow);
        } else if (id2 != 0) {
            PrayerUtils.scheduleNotification(context, 2, hh2, mm2, tomorrow);
        } else if (id3 != 0) {
            PrayerUtils.scheduleNotification(context, 3, hh3, mm3, tomorrow);
        } else if (id4 != 0) {
            PrayerUtils.scheduleNotification(context, 4, hh4, mm4, tomorrow);
        } else if (id5 != 0) {
            PrayerUtils.scheduleNotification(context, 5, hh5, mm5, tomorrow);
        }


    }

    public void createNotif(Context context, PendingIntent intent, int id, String ttl) {
        //Notification sound = default sound
        int isFajarAllowed = SharedClass.getAlarmStatus(context, 1);
        Uri alarm = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.azan);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] pattern = {0, 100, 200, 300};
        NotificationChannel channel1 = new NotificationChannel(
                CHANNEL_1_ID,
                "Salah Tracker",
                NotificationManager.IMPORTANCE_HIGH
        );
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel1);
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(ttl)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setBigContentTitle(ttl))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(alarm)
                .setVibrate(pattern)
                .setColor(16753737)
                .setLights(16753737, 100, 1000)
                .setContentIntent(intent)
                .setAutoCancel(true)
                .build();
        //  NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManagerCompat.notify(id, notification);
    }

}
