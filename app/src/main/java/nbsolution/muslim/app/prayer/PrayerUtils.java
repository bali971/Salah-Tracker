package nbsolution.muslim.app.prayer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import nbsolution.muslim.app.Activities.AlarmReceiver;
import nbsolution.muslim.app.R;

public class PrayerUtils {

//    private static ArrayList<String> NotifMessages = new ArrayList<>();
//    private static ArrayList<String> NotifSalatMessages = new ArrayList<>();

//    private static void NotifMessages(Context context){
//        NotifMessages.add(context.getString(R.string.menu_fadlGod1)+" ”"+context.getString(R.string.thought1)+"“");
//        NotifMessages.add(context.getString(R.string.menu_fadlGod1)+" ”"+context.getString(R.string.thought2)+"“");
//        NotifMessages.add(context.getString(R.string.menu_fadlGod1)+" ”"+context.getString(R.string.thought3)+"“");
//        NotifMessages.add(context.getString(R.string.menu_fadlGod1)+" ”"+context.getString(R.string.thought4)+"“");
//        NotifMessages.add(context.getString(R.string.menu_fadlGod1)+" ”"+context.getString(R.string.thought5)+"“");
//        NotifMessages.add(context.getString(R.string.menu_fadlGod1)+" ”"+context.getString(R.string.thought6)+"“");
//        NotifMessages.add(context.getString(R.string.menu_fadlGod1)+" ”"+context.getString(R.string.thought7)+"“");
//        NotifMessages.add(context.getString(R.string.menu_fadlGod1)+" ”"+context.getString(R.string.thought8)+"“");
//        NotifMessages.add(context.getString(R.string.menu_fadlGod1)+" ”"+context.getString(R.string.thought9)+"“");
//        NotifMessages.add(context.getString(R.string.menu_fadlGod1)+" ”"+context.getString(R.string.thought10)+"“");
//        NotifMessages.add(context.getString(R.string.AdkarExtra1));
//        NotifMessages.add(context.getString(R.string.AdkarExtra2));
//        NotifMessages.add(context.getString(R.string.AdkarExtra3));
//        NotifMessages.add(context.getString(R.string.AdkarExtra4));
//        NotifMessages.add(context.getString(R.string.AdkarExtra5));
//        NotifMessages.add(context.getString(R.string.AdkarExtra6));
//        NotifSalatMessages.add(context.getString(R.string.menu_fadlGod1)+" ”"+context.getString(R.string.SalatThought1)+"“");
//        NotifSalatMessages.add(context.getString(R.string.menu_fadlGod1)+" ”"+context.getString(R.string.SalatThought2)+"“");
//        NotifSalatMessages.add(context.getString(R.string.menu_fadlGod1)+" ”"+context.getString(R.string.SalatThought3)+"“");
//        NotifSalatMessages.add(context.getString(R.string.menu_fadlGod1)+" ”"+context.getString(R.string.SalatThought4)+"“");
//        NotifSalatMessages.add(context.getString(R.string.menu_fadlGod1)+" ”"+context.getString(R.string.SalatThought5)+"“");
//        NotifSalatMessages.add(context.getString(R.string.menu_fadlGod1)+" ”"+context.getString(R.string.SalatThought6)+"“");
//        NotifSalatMessages.add(context.getString(R.string.menu_fadlGod1)+" ”"+context.getString(R.string.SalatThought7)+"“");
//        NotifSalatMessages.add(context.getString(R.string.SalatExtra1));
//        NotifSalatMessages.add(context.getString(R.string.SalatExtra2));
//        NotifSalatMessages.add(context.getString(R.string.SalatExtra3));
//        NotifSalatMessages.add(context.getString(R.string.SalatExtra4));
//        NotifSalatMessages.add(context.getString(R.string.SalatExtra5));
//    }


    public static void scheduleNotification(Context context, int type, int hh, int mm, int day){
//        if (NotifMessages.isEmpty() || NotifSalatMessages.isEmpty()) {
//            NotifMessages(context);
//        }
        Intent broadcastIntent = new Intent(context, AlarmReceiver.class);
        broadcastIntent.addCategory("android.intent.category.DEFAULT");
//        broadcastIntent.putStringArrayListExtra("ArrayMessages",NotifMessages);
//        broadcastIntent.putStringArrayListExtra("ArraySalatMessages",NotifSalatMessages);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar cal = Calendar.getInstance();
        String HH =String.format("%02d", hh);
        String MM=String.format("%02d", mm);
        switch (type) {
            case 1: { //Fajr
                /** For testing play around with times below **/
                /*Calendar today = Calendar.getInstance();
                int current_hh = today.get(Calendar.HOUR_OF_DAY);
                int current_mm = today.get(Calendar.MINUTE);
                mm = current_mm + 1;
                hh = current_hh;*/
                String message = "It's Fajar time";
                broadcastIntent.putExtra("NotifTitle1",  message);
                broadcastIntent.putExtra("NotifId1",type);
                broadcastIntent.putExtra("NotifTimeMM1",mm);
                broadcastIntent.putExtra("NotifTimeHH1",hh);
                PendingIntent broadcast1 = PendingIntent.getBroadcast(context, 1, broadcastIntent ,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
                cal.setTimeInMillis(System.currentTimeMillis());
                cal.set(Calendar.HOUR_OF_DAY, hh);
                cal.set(Calendar.MINUTE,mm);
                cal.set(Calendar.SECOND,0);
                cal.set(Calendar.DAY_OF_MONTH,day);
                // alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast1);
                // alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000*60*10,  broadcast1);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast1);
                break;
            }
            case 2: { //Dhuhr
                String message = "It's Dhuhr time";
                broadcastIntent.putExtra("NotifTitle2",  message);
                broadcastIntent.putExtra("NotifId2",type);
                broadcastIntent.putExtra("NotifTimeMM2",mm);
                broadcastIntent.putExtra("NotifTimeHH2",hh);
                PendingIntent broadcast2 = PendingIntent.getBroadcast(context,2, broadcastIntent ,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
                cal.setTimeInMillis(System.currentTimeMillis());
                cal.set(Calendar.HOUR_OF_DAY, hh);
                cal.set(Calendar.MINUTE,mm);
                cal.set(Calendar.SECOND,0);
                cal.set(Calendar.DAY_OF_MONTH,day);
                // alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast2);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast2);
                break;
            }
            case 3: { //Asr
                String message = "It's Asr time";
                broadcastIntent.putExtra("NotifTitle3",  message);
                broadcastIntent.putExtra("NotifId3",type);
                broadcastIntent.putExtra("NotifTimeMM3",mm);
                broadcastIntent.putExtra("NotifTimeHH3",hh);
                PendingIntent broadcast3 = PendingIntent.getBroadcast(context, 3, broadcastIntent ,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
                cal.setTimeInMillis(System.currentTimeMillis());
                cal.set(Calendar.HOUR_OF_DAY, hh);
                cal.set(Calendar.MINUTE,mm);
                cal.set(Calendar.SECOND,0);
                cal.set(Calendar.DAY_OF_MONTH,day);
                // alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast3);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast3);
                break;
            }
            case 4: { //Maghrib
                String message = "It's Maghrib time";
                broadcastIntent.putExtra("NotifTitle4",  message);
                broadcastIntent.putExtra("NotifId4",type);
                broadcastIntent.putExtra("NotifTimeMM4",mm);
                broadcastIntent.putExtra("NotifTimeHH4",hh);
                PendingIntent broadcast4 = PendingIntent.getBroadcast(context,4, broadcastIntent ,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
                cal.setTimeInMillis(System.currentTimeMillis());
                cal.set(Calendar.HOUR_OF_DAY, hh);
                cal.set(Calendar.MINUTE,mm);
                cal.set(Calendar.SECOND,0);
                cal.set(Calendar.DAY_OF_MONTH,day);
                // alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast4);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast4);
                break;
            }
            case 5: { //Isha
                String message = "It's Isha time";
                broadcastIntent.putExtra("NotifTitle5",  message);
                broadcastIntent.putExtra("NotifId5",type);
                broadcastIntent.putExtra("NotifTimeMM5",mm);
                broadcastIntent.putExtra("NotifTimeHH5",hh);
                PendingIntent broadcast5 = PendingIntent.getBroadcast(context, 5, broadcastIntent ,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
                cal.setTimeInMillis(System.currentTimeMillis());
                cal.set(Calendar.HOUR_OF_DAY, hh);
                cal.set(Calendar.MINUTE,mm);
                cal.set(Calendar.SECOND,0);
                cal.set(Calendar.DAY_OF_MONTH,day);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast5);
                break;
            }

        }
    }

    public static String formatTime(String timeString){
        String formattedTime = "";
        try {
            // Split the time string into hours and minutes
            int hours = Integer.parseInt(timeString.split(":")[0]);
            int minutes = Integer.parseInt(timeString.split(":")[1]);

            // Convert to 12-hour format
            String period = "";
            if (hours > 12) {
                hours -= 12;
                period = "pm";
            } else if (hours == 0) {
                hours = 12;
                period = "am";
            } else if (hours == 12) {
                period = "pm";
            } else {
                period = "am";
            }

            // Format the time string
            formattedTime = String.format("%02d:%02d %s", hours, minutes, period);

        } catch (NumberFormatException e) {
            System.out.println("Invalid time format. Please provide a string in HH:MM format.");
        }
        return formattedTime;
    }

}
