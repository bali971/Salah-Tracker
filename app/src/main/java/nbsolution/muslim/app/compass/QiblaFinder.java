package nbsolution.muslim.app.compass;

import static android.view.View.INVISIBLE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
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

public class QiblaFinder extends AppCompatActivity {
    private static final String TAG = "QiblaFinder";
    private Compass compass;
    private ImageView arrowViewQiblat;
    private ImageView imageDial;
    private TextView text_down;
    private float currentAzimuth;
    String city, country,lat,lon;
    SharedPreferences prefs;
    float position;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qibla_finder);

        prefs = getSharedPreferences("qibla", MODE_PRIVATE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        arrowViewQiblat =  findViewById(R.id.main_image_qibla);
        imageDial =  findViewById(R.id.main_image_dial);
        text_down =  findViewById(R.id.text_down);
        ImageView btnButton = findViewById(R.id.backBtn);
        arrowViewQiblat.setVisibility(INVISIBLE);
        arrowViewQiblat.setVisibility(View.GONE);
        lat = SharedClass.getLocationDetails(this,"latitude");
        lon = SharedClass.getLocationDetails(this,"longitude");
        city = SharedClass.getLocationDetails(this,"city");
        country = SharedClass.getLocationDetails(this,"country");
        btnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });


        AdRequest adRequest = new AdRequest.Builder().build();
        MobileAds.initialize(this);
        AdView mAdView = findViewById(R.id.adView);
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

        if (lat != null & lon != null){
            setupCompass();
        }else{
            lat = SharedClass.getLocationDetails(this,"latitude");
            lon = SharedClass.getLocationDetails(this,"longitude");
        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "start compass");
        if(compass != null) {
            compass.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(compass != null) {
            compass.stop();
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

    @Override
    protected void onResume() {
        super.onResume();
        if(compass != null) {
            compass.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "stop compass");
        if(compass != null) {
            compass.stop();
        }
    }

    private void setupCompass() {
       fetch_GPS();
        compass = new Compass(this);
        Compass.CompassListener cl = new Compass.CompassListener() {
            @Override
            public void onNewAzimuth(float azimuth) { adjustGambarDial(azimuth); adjustArrowQiblat(azimuth); }
        };
        compass.setListener(cl);
    }


    public void adjustGambarDial(float azimuth) {
        Animation an = new RotateAnimation(-currentAzimuth, -azimuth, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        currentAzimuth = (azimuth);
        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);
        imageDial.startAnimation(an);
    }
    public void adjustArrowQiblat(float azimuth) {
        Animation an = new RotateAnimation(-(currentAzimuth)+position, -azimuth, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        currentAzimuth = (azimuth);
        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);
        arrowViewQiblat.startAnimation(an);
        if(position > 0){
            arrowViewQiblat .setVisibility(View.VISIBLE);
        }else{
            arrowViewQiblat .setVisibility(INVISIBLE);
            arrowViewQiblat .setVisibility(View.GONE);
        }
    }


    public void fetch_GPS(){
        double result = 0;
        double latitude = Double.parseDouble(lat);
        double longitude = Double.parseDouble(lon);
        if(latitude != 0 && longitude != 0){
            if(latitude < 0.001 && longitude < 0.001) {
                arrowViewQiblat.setVisibility(INVISIBLE);
                arrowViewQiblat.setVisibility(View.GONE);
                text_down.setText(getResources().getString(R.string.locationunready));
            }else{
                double longitude2 = 39.826209; // Kaabah Position https://www.latlong.net/place/kaaba-mecca-saudi-arabia-12639.html
                double longitude1 = longitude;
                double latitude2 = Math.toRadians(21.422507); // Kaabah Position https://www.latlong.net/place/kaaba-mecca-saudi-arabia-12639.html
                double latitude1 = Math.toRadians(latitude);
                double longDiff= Math.toRadians(longitude2-longitude1);
                double y= Math.sin(longDiff)*Math.cos(latitude2);
                double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);
                result = (Math.toDegrees(Math.atan2(y, x))+360)%360;
                 position = (float) result;
                arrowViewQiblat .setVisibility(View.VISIBLE);
                text_down.setText(city+","+country);

            }
        }else{
            arrowViewQiblat.setVisibility(INVISIBLE);
            arrowViewQiblat.setVisibility(View.GONE);
            text_down.setText(getResources().getString(R.string.gpsplz));
        }
    }


}
