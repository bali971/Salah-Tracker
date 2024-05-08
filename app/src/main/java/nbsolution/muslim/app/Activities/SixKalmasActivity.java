package nbsolution.muslim.app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import nbsolution.muslim.app.R;

public class SixKalmasActivity extends AppCompatActivity implements View.OnClickListener {

    TextView kalmaone, kalmatwo, kalmathree, kalmafour, kalmafive, kalmasix;
    ImageView btnCopy1, btnCopy2, btnCopy3, btnCopy4, btnCopy5, btnCopy6;
    ImageView btnShare1, btnShare2, btnShare3, btnShare4, btnShare5, btnShare6,backBtn;

    private static final String TAG = "SixKalma";
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_six_kalmas);

        AdRequest adRequest = new AdRequest.Builder().build();
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

        kalmaone = findViewById(R.id.kalmaone);
        kalmatwo = findViewById(R.id.kalmatwo);
        kalmathree = findViewById(R.id.kalmathree);
        kalmafour = findViewById(R.id.kalmafour);
        kalmafive = findViewById(R.id.kalmafive);
        kalmasix = findViewById(R.id.kalmasix);
        btnCopy1 = findViewById(R.id.btnCopy1);
        btnCopy2 = findViewById(R.id.btnCopy2);
        btnCopy3 = findViewById(R.id.btnCopy3);
        btnCopy4 = findViewById(R.id.btnCopy4);
        btnCopy5 = findViewById(R.id.btnCopy5);
        btnCopy6 = findViewById(R.id.btnCopy6);
        btnShare1 = findViewById(R.id.btnShare1);
        btnShare2 = findViewById(R.id.btnShare2);
        btnShare3 = findViewById(R.id.btnShare3);
        btnShare4 = findViewById(R.id.btnShare4);
        btnShare5 = findViewById(R.id.btnShare5);
        btnShare6 = findViewById(R.id.btnShare6);
        backBtn = findViewById(R.id.backBtn);

        btnCopy1.setOnClickListener(this);
        btnCopy2.setOnClickListener(this);
        btnCopy3.setOnClickListener(this);
        btnCopy4.setOnClickListener(this);
        btnCopy5.setOnClickListener(this);
        btnCopy6.setOnClickListener(this);
        btnShare1.setOnClickListener(this);
        btnShare2.setOnClickListener(this);
        btnShare3.setOnClickListener(this);
        btnShare4.setOnClickListener(this);
        btnShare5.setOnClickListener(this);
        btnShare6.setOnClickListener(this);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCopy1:
                copytxt("First", kalmaone.getText().toString());
                break;
            case R.id.btnCopy2:
                copytxt("Second", kalmatwo.getText().toString());
                break;
            case R.id.btnCopy3:
                copytxt("Third", kalmathree.getText().toString());
                break;
            case R.id.btnCopy4:
                copytxt("Fourth", kalmafour.getText().toString());
                break;
            case R.id.btnCopy5:
                copytxt("Fifth", kalmafive.getText().toString());
                break;
            case R.id.btnCopy6:
                copytxt("Sixth", kalmasix.getText().toString());
                break;
            case R.id.btnShare1:
                shareKalma(kalmaone.getText().toString());
                break;
            case R.id.btnShare2:
                shareKalma(kalmatwo.getText().toString());
                break;
            case R.id.btnShare3:
                shareKalma(kalmathree.getText().toString());
                break;
            case R.id.btnShare4:
                shareKalma(kalmafour.getText().toString());
                break;
            case R.id.btnShare5:
                shareKalma(kalmafive.getText().toString());
                break;
            case R.id.btnShare6:
                shareKalma(kalmasix.getText().toString());
                break;
        }
    }

    public void copytxt(String kalma, String txt){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(kalma+" Kalma",txt);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(SixKalmasActivity.this, "Copied", Toast.LENGTH_SHORT).show();
    }

    public void shareKalma(String kalma){
        Intent intent2 = new Intent(); intent2.setAction(Intent.ACTION_SEND);
        intent2.setType("text/plain");
        intent2.putExtra(Intent.EXTRA_TEXT, kalma);
        startActivity(Intent.createChooser(intent2, "Share via"));
    }

}