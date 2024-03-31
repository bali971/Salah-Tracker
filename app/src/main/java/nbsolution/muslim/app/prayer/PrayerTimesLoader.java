package nbsolution.muslim.app.prayer;

import static android.content.Context.MODE_PRIVATE;
import static nbsolution.muslim.app.prayer.PrayerUtils.scheduleNotification;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import nbsolution.muslim.app.Helper.HttpHandler;
import nbsolution.muslim.app.R;
import nbsolution.muslim.app.SharedData.SharedClass;

public class PrayerTimesLoader extends AsyncTask<Void, Void, Void> {

    String GETPrayerCity;
    String GETCountry;
    String fajr,duhur,asr,maghrib,isha,sunrise,sunset,islamicDay,islamicMonth,islamicYear, islamicDate,design,formattedDate;
    Boolean Passed=false;
    Context context;
    public PrayerTimesLoader(Context context) {
        this.context = context;
        if (SharedClass.getPermission(context,"isPermissionGranted")) {
            GETPrayerCity = SharedClass.getLocationDetails(context, "city");
            GETCountry = SharedClass.getLocationDetails(context, "country");
        }else {
            GETPrayerCity = SharedClass.getManualLocationDetails(context,"city");
            GETCountry = SharedClass.getManualLocationDetails(context, "country");
        }
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = formatter.format(currentDate);
        // System.out.println("working12 again through receiver");
        //Toast.makeText(MainActivity.this,getString(R.string.loadingprayertimes),Toast.LENGTH_LONG).show();
    }
    @Override
    protected Void doInBackground(Void... arg0) {
        HttpHandler sh = new HttpHandler();
        String url ="http://api.aladhan.com/v1/timingsByCity/"+formattedDate+"?city="+GETPrayerCity+"&country="+GETCountry;
        // System.out.println("working12---"+url);
        String jsonStr = sh.makeServiceCall(url);
        //Log.e("TAG", "Response from url: " + jsonStr);
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONObject data = jsonObj.getJSONObject("data");
                JSONObject prayertimes = data.getJSONObject("timings");
                JSONObject date = data.getJSONObject("date");
                JSONObject hijri = date.getJSONObject("hijri");
                JSONObject hijriMonth = hijri.getJSONObject("month");
                JSONObject designation = hijri.getJSONObject("designation");
                fajr = prayertimes.getString("Fajr");
                duhur = prayertimes.getString("Dhuhr");
                asr = prayertimes.getString("Asr");
                maghrib = prayertimes.getString("Maghrib");
                isha = prayertimes.getString("Isha");
                sunrise = prayertimes.getString("Sunrise");
                sunset = prayertimes.getString("Sunset");
                islamicDay = hijri.getString("day");
                islamicMonth = hijriMonth.getString("en");
                islamicYear = hijri.getString("year");
                design = designation.getString("abbreviated");
                islamicDate = islamicDay+"-"+islamicMonth+"-"+islamicYear+" "+design;
                Passed =true;
            } catch (final JSONException e) {
                Log.e("TAG", "Json parsing error: " + e.getMessage());
               /* context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, getString(R.string.errorloadprayertimes), Toast.LENGTH_LONG).show();
                    }
                });*/
            }
        } else {
            Log.e("TAG", "Couldn't get json from server.");
           /* runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, getString(R.string.errorloadprayertimes), Toast.LENGTH_LONG).show();
                }
            });*/
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
       // Toast.makeText(context, context.getString(R.string.prayertimesloaded), Toast.LENGTH_LONG).show();
        SharedPreferences salatpref = context.getSharedPreferences("lastprayertimes", MODE_PRIVATE);
        SharedPreferences.Editor editor = salatpref.edit();
        // editor.putString("city", GETPrayerCity);
        editor.putString("fajr",fajr);
        editor.putString("duhur",duhur);
        editor.putString("asr",asr);
        editor.putString("maghrib",maghrib);
        editor.putString("isha",isha);
        editor.putString("sunrise",sunrise);
        editor.putString("sunset",sunset);
        editor.putString("islamicDate",islamicDate);
        editor.apply();

        if (Passed) {
            String[] time1 = fajr.split(":");
            int hh1 = Integer.parseInt(time1[0].trim());
            int mm1 = Integer.parseInt(time1[1].trim());
            String[] time2 = duhur.split(":");
            int hh2 = Integer.parseInt(time2[0].trim());
            int mm2 = Integer.parseInt(time2[1].trim());
            String[] time3 = asr.split(":");
            int hh3 = Integer.parseInt(time3[0].trim());
            int mm3 = Integer.parseInt(time3[1].trim());
            String[] time4 = maghrib.split(":");
            int hh4 = Integer.parseInt(time4[0].trim());
            int mm4 = Integer.parseInt(time4[1].trim());
            String[] time5 = isha.split(":");
            int hh5 = Integer.parseInt(time5[0].trim());
            int mm5 = Integer.parseInt(time5[1].trim());
            int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

            scheduleNotification(context, 1, hh1, mm1, today);
            scheduleNotification(context, 2, hh2, mm2, today);
            scheduleNotification(context, 3, hh3, mm3, today);
            scheduleNotification(context, 4, hh4, mm4, today);
            scheduleNotification(context, 5, hh5, mm5, today);
        }
    }
}
