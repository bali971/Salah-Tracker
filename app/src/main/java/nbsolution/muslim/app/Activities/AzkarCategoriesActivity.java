package nbsolution.muslim.app.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;

import nbsolution.muslim.app.Adapters.AzkarCategoryAdapter;
import nbsolution.muslim.app.Manager.DBManagerAzkar;
import nbsolution.muslim.app.R;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;


public class AzkarCategoriesActivity extends AppCompatActivity {



    private AdView mAdView;
    DBManagerAzkar dbManagerAzkar;
    AzkarCategoryAdapter azkarAdapter;
    RecyclerView azkarRV;
    ImageView btnBack;

    private InterstitialAd mInterstitialAd;
    private static final String TAG = "AzkarCategoriesActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azkar_categories);
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,getString(R.string.interstitial_adunit_id_prod), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
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
            mInterstitialAd.show(AzkarCategoriesActivity.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }


        MobileAds.initialize(this);
        mAdView = findViewById(R.id.adView);
        mAdView.loadAd(adRequest);

        btnBack = findViewById(R.id.backBtn);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
        dbManagerAzkar = new DBManagerAzkar(AzkarCategoriesActivity.this);
        dbManagerAzkar.open();
        try {
            dbManagerAzkar.copyDataBase();
        }catch (IOException e){
            e.printStackTrace();
        }

        azkarRV = findViewById(R.id.azkarRV);
        azkarRV.setLayoutManager(new LinearLayoutManager(this));
        azkarAdapter = new AzkarCategoryAdapter(AzkarCategoriesActivity.this, dbManagerAzkar.getCategories("ar"), dbManagerAzkar.getCategories("en"));
        azkarRV.setAdapter(azkarAdapter);

    }

}