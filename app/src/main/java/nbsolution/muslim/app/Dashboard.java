package nbsolution.muslim.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import nbsolution.muslim.app.BuildConfig;
import nbsolution.muslim.app.R;
import nbsolution.muslim.app.Activities.AboutUs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nbsolution.muslim.app.Adapters.DashboardAdapter;
import nbsolution.muslim.app.SharedData.SharedClass;
import nbsolution.muslim.app.prayer.PrayerTimesLoader;
import nbsolution.muslim.app.prayer.PrayerUtils;

public class Dashboard extends AppCompatActivity {
    private static final String TAG = "Dashboard";
    private InterstitialAd mInterstitialAd;
    int drawerpos = 0;
    DrawerLayout drawerLayout;
    ListView optionsLV;
    OptionsAdapter optionsAdapter;
    TextView headingTxt, dayTxt;
    ImageView menuBtn;

    Integer[] opions_icons_blue = {R.drawable.share_filled, R.drawable.star_filled, R.drawable.about_us, R.drawable.app_filled};
    String[] opions = {"Share App", "About Us"};
    RecyclerView dashboardRV;
    DashboardAdapter dashboardAdapter;

    private static final String PREF_NAME = "FunctionCallPrefs";
    private static final String LAST_CALL_DATE_KEY = "lastCallDate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        PrayerUtils.scheduleAlarm(this);
        if (shouldCallFunction(this)){
            new PrayerTimesLoader(this).execute();
        }
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        InterstitialAd.load(this,getString(R.string.interstitial_adunit_id_prod), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        super.onAdLoaded(interstitialAd);
                        mInterstitialAd = interstitialAd;
                        showAds();
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });

        drawerLayout = findViewById(R.id.main_drawer_layout);
        menuBtn=findViewById(R.id.menuBtn);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer((int) Gravity.START);
            }
        });

        optionsLV = findViewById(R.id.optionsLV);
        optionsAdapter = new OptionsAdapter();
        optionsLV.setAdapter(optionsAdapter);
        optionsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawerpos = i;
                optionsAdapter.notifyDataSetChanged();
                switch (i) {
                    case 0:
                        shareApp();
                        break;
                    case 1:
                        startActivity(new Intent(getApplicationContext(), AboutUs.class));
                        finish();
                        break;
                }
                drawerLayout.closeDrawer((int) Gravity.START);
            }
        });

        SimpleDateFormat sdff = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH);
        String str = sdff.format(new Date());
        Log.d("dateraza", str);

        headingTxt = findViewById(R.id.headingTxt);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        dayTxt = findViewById(R.id.dayTxt);
        dayTxt.setText(str);

        dashboardRV = findViewById(R.id.dashboardRV);
        dashboardRV.setLayoutManager(new GridLayoutManager(this, 2));
        dashboardAdapter = new DashboardAdapter(Dashboard.this);
        dashboardRV.setAdapter(dashboardAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            System.out.println("working12--");
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }
    public void showAds(){
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            System.out.println("working12--");
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }

    private void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Salah Tarcker");
            String shareMessage = "\nEasiest way to get Namaz timing\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    public class OptionsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return opions.length;
        }

        @Override
        public Object getItem(int i) {
            return opions[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = View.inflate(Dashboard.this, R.layout.drawer_categories_cell, null);

            LinearLayout cell = rowView.findViewById(R.id.cell);
            TextView optionname = rowView.findViewById(R.id.optionname);
            optionname.setText(opions[i]);
            optionname.setCompoundDrawablesWithIntrinsicBounds(opions_icons_blue[i], 0, 0, 0);

            if (drawerpos == i) {
                cell.setBackgroundResource(R.drawable.btn_bg_signin_solid_white);
                optionname.setTextColor(Dashboard.this.getResources().getColor(R.color.colorPrimary));
                setTextViewDrawableColor(optionname, R.color.colorPrimary);
            }

            return rowView;
        }
    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textView.getContext(), color), PorterDuff.Mode.SRC_IN));
            }
        }
    }
    public static boolean shouldCallFunction(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        long lastCallDate = prefs.getLong(LAST_CALL_DATE_KEY, 0);

        // Get current date
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);

        // Check if the function has been called today
        if (lastCallDate < currentDate.getTimeInMillis()) {
            // Function has not been called today, save current date as last call date
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(LAST_CALL_DATE_KEY, currentDate.getTimeInMillis());
            editor.apply();
            return true;
        } else {
            // Function has already been called today
            return false;
        }
    }

}