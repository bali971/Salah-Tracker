package nbsolution.muslim.app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nbsolution.muslim.app.Adapters.NamesAdapter;
import nbsolution.muslim.app.Models.NamesModel;
import nbsolution.muslim.app.R;
import nbsolution.muslim.app.databinding.ActivitySplashBinding;
import nbsolution.muslim.app.intrface.OnItemClickListener;

public class AllahNames extends AppCompatActivity implements OnItemClickListener {

    NamesAdapter namesAdapter;
    TextView txtSetting;
    ImageView backBtn;
    RecyclerView namesRV;
    List<NamesModel> namesArr = new ArrayList<>();
    private InterstitialAd mInterstitialAd;
    private static final String TAG = "AllahName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allah_names);

        MobileAds.initialize(this);
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

        namesRV = findViewById(R.id.namesRV);
        namesRV.setLayoutManager(new LinearLayoutManager(this));

        backBtn = findViewById(R.id.backBtn);
        txtSetting = findViewById(R.id.txtSetting);
        Log.d("json", loadJSONFromAsset());
        getSingleTodayWeather(loadJSONFromAsset());
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

    }

    public void getSingleTodayWeather(String response) {
        namesArr.clear();
        try {
            String jsonData = response;
            if (jsonData != null) {
                JSONObject Jobject = new JSONObject(jsonData);
                JSONArray array = Jobject.getJSONArray("data");
                for (int i=0; i<array.length(); i++){
                    JSONObject namesobj = array.getJSONObject(i);
                    String name = namesobj.getString("name");
                    String transliteration = namesobj.getString("transliteration");
                    JSONObject enobj = namesobj.getJSONObject("en");
                    String meaning = enobj.getString("meaning");
                    namesArr.add(new NamesModel(name, transliteration, meaning));
                }

                namesAdapter = new NamesAdapter(AllahNames.this, namesArr, this);


                namesRV.setAdapter(namesAdapter);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("names.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    public void copytxt(String txt){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(" Name",txt);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(AllahNames.this, "Copied", Toast.LENGTH_SHORT).show();
    }
    public void shareName(String name){
        Intent intent2 = new Intent(); intent2.setAction(Intent.ACTION_SEND);
        intent2.setType("text/plain");
        intent2.putExtra(Intent.EXTRA_TEXT, name);
        startActivity(Intent.createChooser(intent2, "Share via"));
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
    public void onItemClick(View v, int position) {
        // do nothing
    }

    @Override
    public void onItemClicktoShare(String name) {
        shareName(name);
    }

    @Override
    public void onItemClicktoCopy(String name) {
        copytxt(name);
    }
}