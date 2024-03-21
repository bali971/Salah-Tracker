package nbsolution.muslim.app;

public class Constants {

    public static final String NOTIFICATION_CHANNEL_ID = "REMINDY_NOTIFICATION_CHANNEL_ID";
    public static final String NOTIFICATION_CHANNEL_NAME = "Alert Notifications";
    public static final String NOTIFICATION_CHANNEL_DESCRIPTION = "Shows notifications whenever an Reminder is triggered";
    public static String LATITUDE = "latitude";
    public static String LONGITUDE = "longitude";
    public static String QUIBLA_DEGREE = "quibla_degree";
    public static String TIME_12 = "hh:mm a";
    public static String ALARM_FOR = "alarm_for_";

    public static String FAJR = "Fajr";
    public static String SUNRISE = "Sunrise";
    public static String DHUHR = "Dhuhr";
    public static String ASR = "Asr";
    public static String SUNSET = "Sunset";
    public static String MAGHRIB = "Maghrib";
    public static String ISHA = "Isha";

    public static String EXTRA_PRAYER_NAME = "prayer_name";
    public static String EXTRA_PRAYER_TIME = "prayer_time";
    public static int ONE_MINUTE = 60000;
    public static int FIVE_MINUTES = ONE_MINUTE * 5;

    public static int LOCATION_ID = 1011;
    public static int ALARM_ID = 1010;

    public static String[] KEYS = {
            FAJR,
            SUNRISE,
            DHUHR,
            ASR,
            SUNSET,
            MAGHRIB,
            ISHA
    };

    public static String[] NAME_ID = {
            FAJR,
            SUNRISE,
            DHUHR,
            ASR,
            SUNSET,
            MAGHRIB,
            ISHA
    };
}