package nbsolution.muslim.app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import nbsolution.muslim.app.R;
import nbsolution.muslim.app.SharedData.SharedClass;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class CounterActivity extends AppCompatActivity implements View.OnClickListener {

    int counter = 0;
    TextView counterTxt;
    ImageView backBtn, btnBackTwo;
    CardView btnCount, btnReset;
    LinearLayout btnSave, btnAzkar;
    MediaPlayer mp;

    private InterstitialAd mInterstitialAd;
    private static final String TAG = "CounterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        MobileAds.initialize(this);
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
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }

        mp = MediaPlayer.create(CounterActivity.this, R.raw.tasbeehsound);

        counterTxt = findViewById(R.id.counterTxt);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        btnBackTwo = findViewById(R.id.btnBackTwo);
        btnBackTwo.setOnClickListener(this);
        btnCount = findViewById(R.id.btnCount);
        btnCount.setOnClickListener(this);
        btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(this);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        btnAzkar = findViewById(R.id.btnAzkar);
        btnAzkar.setOnClickListener(this);


        counter = SharedClass.getCounter(CounterActivity.this);
        counterTxt.setText(String.valueOf(SharedClass.getCounter(CounterActivity.this)));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backBtn:
                getOnBackPressedDispatcher().onBackPressed();
                break;
            case R.id.btnBackTwo:
                getOnBackPressedDispatcher().onBackPressed();
                break;
            case R.id.btnCount:
                counter = counter+1;
                counterTxt.setText(String.valueOf(counter));
                if (SharedClass.getTasbeenSound(CounterActivity.this)==1){
                    if (!mp.isPlaying()){
                        mp.start();
                    }
                }
                break;
            case R.id.btnReset:
                counter = 0;
                SharedClass.saveCounter(CounterActivity.this, counter);
                counterTxt.setText(String.valueOf(counter));
                break;
            case R.id.btnSave:
                SharedClass.saveCounter(CounterActivity.this, counter);
                Toast.makeText(this, "Counter Saved", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnAzkar:
                startActivity(new Intent(getApplicationContext(), AzkarCategoriesActivity.class));
                break;
        }
    }
}