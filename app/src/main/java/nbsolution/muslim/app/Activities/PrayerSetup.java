package nbsolution.muslim.app.Activities;

import static nbsolution.muslim.app.prayer.PrayerUtils.formatTime;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import nbsolution.muslim.app.R;
import nbsolution.muslim.app.SharedData.SharedClass;

public class PrayerSetup extends AppCompatActivity {
    private static final String TAG = "PrayerSetup";
    TextView mFajr,mDuhur,mAsr,mMaghrib,mIsha,mSunrise,mSunset,mIslamicDate,mCity;
    String fajr,duhur,asr,maghrib,isha,sunrise,sunset,islamicDate,city;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer);
        SharedPreferences salatpref = getSharedPreferences("lastprayertimes", MODE_PRIVATE);
        fajr = salatpref.getString("fajr","defValue");
        duhur = salatpref.getString("duhur","defValue");
        asr = salatpref.getString("asr","defValue");
        maghrib = salatpref.getString("maghrib","defValue");
        isha = salatpref.getString("isha","defValue");
        sunrise = salatpref.getString("sunrise","defValue");
        sunset = salatpref.getString("sunset","defValue");
        islamicDate = salatpref.getString("islamicDate","defValue");
        city = SharedClass.getLocationDetails(this,"city");

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

        mFajr=findViewById(R.id.tv_fajr);
        mDuhur=findViewById(R.id.tv_duhr);
        mAsr=findViewById(R.id.tv_asar);
        mMaghrib=findViewById(R.id.tv_maghrib);
        mIsha=findViewById(R.id.tv_isha);
        mSunrise=findViewById(R.id.tv_sunrise);
        mSunset=findViewById(R.id.tv_sunset);
        mIslamicDate = findViewById(R.id.tv_islamic_date);
        mCity = findViewById(R.id.tv_city);
        ImageView btnButton = findViewById(R.id.iv_back);
        btnButton.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());
        mFajr.setText(formatTime(fajr));
        mDuhur.setText(formatTime(duhur));
        mAsr.setText(formatTime(asr));
        mMaghrib.setText(formatTime(maghrib));
        mIsha.setText(formatTime(isha));
        mSunrise.setText(formatTime(sunrise));
        mSunset.setText(formatTime(sunset));
        mIslamicDate.setText(islamicDate);
        mCity.setText(city);
        }

    public void showAds(){
                    if (mInterstitialAd != null) {
                mInterstitialAd.show(this);
            } else {
                System.out.println("working12--");
                Log.d("TAG", "The interstitial ad wasn't ready yet.");
            }
    }



    }


