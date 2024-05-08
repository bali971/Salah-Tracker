package nbsolution.muslim.app.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

import nbsolution.muslim.app.R;
import nbsolution.muslim.app.SharedData.SharedClass;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.lang.reflect.Array;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener  {

    ImageView backBtn;
    Button btnLocation;
    MethodsCustomAdapter methodsCustomAdapter;
    int flag = 2;
    private IntentFilter intentFilter;
    private ProgressDialog progressDialog;
    public static LinearLayout locationLayout;
    LinearLayout btnCurrent, btnMethod;
    TextView txtCurrent;
    public static TextView txtCountry, txtCity, txtLocation, txtMethod;
    TextView txtFajrOn, txtFajrOff, txtZohrOn, txtZohrOff, txtAsarOn, txtAsarOff, txtMaghribOn, txtMaghribOff, txtIshaOn, txtIshaOff, txtCounterOn, txtCounterOff;

    private InterstitialAd mInterstitialAd;
    private static final String TAG = "SettingSctivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Context context = this;

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

        txtCounterOff=findViewById(R.id.txtCounterOff);
        txtCounterOff.setOnClickListener(this);
        txtCounterOn=findViewById(R.id.txtCounterOn);
        txtCounterOn.setOnClickListener(this);
        txtFajrOn=findViewById(R.id.txtFajrOn);
        txtFajrOn.setOnClickListener(this);
        txtFajrOff=findViewById(R.id.txtFajrOff);
        txtFajrOff.setOnClickListener(this);
        txtZohrOn=findViewById(R.id.txtZohrOn);
        txtZohrOn.setOnClickListener(this);
        txtZohrOff=findViewById(R.id.txtZohrOff);
        txtZohrOff.setOnClickListener(this);
        txtAsarOn=findViewById(R.id.txtAsarOn);
        txtAsarOn.setOnClickListener(this);
        txtAsarOff=findViewById(R.id.txtAsarOff);
        txtAsarOff.setOnClickListener(this);
        txtMaghribOn=findViewById(R.id.txtMaghribOn);
        txtMaghribOn.setOnClickListener(this);
        txtMaghribOff=findViewById(R.id.txtMaghribOff);
        txtMaghribOff.setOnClickListener(this);
        txtIshaOn=findViewById(R.id.txtIshaOn);
        txtIshaOn.setOnClickListener(this);
        txtIshaOff=findViewById(R.id.txtIshaOff);
        txtIshaOff.setOnClickListener(this);

        checkAlarmStatus();
        checkCounterSound();

        flag = SharedClass.getMethod(SettingsActivity.this);

//        btnLocation = findViewById(R.id.txtUpdateLocation);
//        btnLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                val context: Context = this@SplashActivity
////                val geocoder = Geocoder(context, Locale.getDefault())
////                val addresses = geocoder.getFromLocation(latitude, longitude, 5)
////                if (addresses != null && !addresses.isEmpty()) {
////                    val address = addresses[0]
////                    val cityName = address.locality
////                    val countryName = address.countryName
////                    SharedClass.setLocationFlag(
////                            this,
////                            countryName,
////                            cityName,
////                            latitude.toString(),
////                            longitude.toString(),
////                            1
////                    )
//                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//                Array address = geocoder.getFromLocation()
//            }
//            });

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        btnMethod = findViewById(R.id.btnMethod);
        txtMethod = findViewById(R.id.txtMethod);
        txtMethod.setText(SharedClass.methods[flag]);
        txtCountry = findViewById(R.id.txtCountry);
        txtCity = findViewById(R.id.txtCity);
        txtLocation = findViewById(R.id.txtLocation);
        txtCurrent = findViewById(R.id.txtCurrent);
        btnCurrent = findViewById(R.id.btnCurrent);

        locationLayout = findViewById(R.id.locationLayout);
        if(SharedClass.getPermission(this,"isPermissionGranted")) {
            if (SharedClass.getLocationFlag(SettingsActivity.this) == 0) {
                locationLayout.setVisibility(View.GONE);
            } else if (SharedClass.getLocationFlag(SettingsActivity.this) == 1) {
               // btnCurrent.setBackgroundResource(R.drawable.btn_bg_signin_stroke);
                //txtCurrent.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                locationLayout.setVisibility(View.VISIBLE);
                txtCountry.setText(SharedClass.getLocationDetails(SettingsActivity.this, "country"));
                txtCity.setText(SharedClass.getLocationDetails(SettingsActivity.this, "city"));
                txtLocation.setText(SharedClass.getLocationDetails(SettingsActivity.this, "latitude") + "," + SharedClass.getLocationDetails(SettingsActivity.this, "longitude"));
            } else if (SharedClass.getLocationFlag(SettingsActivity.this) == 2) {
               // btnCurrent.setBackgroundResource(R.drawable.btn_bg_signin_solid);
                //txtCurrent.setTextColor(getResources().getColor(R.color.white));
                locationLayout.setVisibility(View.VISIBLE);
                txtCountry.setText(SharedClass.getLocationDetails(SettingsActivity.this, "country"));
                txtCity.setText(SharedClass.getLocationDetails(SettingsActivity.this, "city"));
                txtLocation.setText(SharedClass.getLocationDetails(SettingsActivity.this, "latitude") + "," + SharedClass.getLocationDetails(SettingsActivity.this, "longitude"));
            }
        }else {
            // btnCurrent.setBackgroundResource(R.drawable.btn_bg_signin_solid);
           // txtCurrent.setTextColor(getResources().getColor(R.color.white));
            locationLayout.setVisibility(View.VISIBLE);
            txtCountry.setText(SharedClass.getManualLocationDetails(SettingsActivity.this, "country"));
            txtCity.setText(SharedClass.getManualLocationDetails(SettingsActivity.this, "city"));
            txtLocation.setText(SharedClass.getManualLatLng(SettingsActivity.this, "lat") + "," + SharedClass.getManualLatLng(SettingsActivity.this, "lng"));
        }

        btnCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // btnCurrent.setBackgroundResource(R.drawable.btn_bg_signin_solid);
                //txtCurrent.setTextColor(getResources().getColor(R.color.white));

                // displayProgress();
            }
        });

        btnMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                methodsDialog();
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

    void checkAlarmStatus(){
        if (SharedClass.getAlarmStatus(SettingsActivity.this, 1)==1){
            txtFajrOn.setTextColor(getResources().getColor(R.color.white));
            txtFajrOn.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
            txtFajrOff.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtFajrOff.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
        }else{
            txtFajrOff.setTextColor(getResources().getColor(R.color.white));
            txtFajrOff.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
            txtFajrOn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtFajrOn.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
        }

        if (SharedClass.getAlarmStatus(SettingsActivity.this, 2)==1){
            txtZohrOn.setTextColor(getResources().getColor(R.color.white));
            txtZohrOn.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
            txtZohrOff.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtZohrOff.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
        }else{
            txtZohrOff.setTextColor(getResources().getColor(R.color.white));
            txtZohrOff.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
            txtZohrOn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtZohrOn.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
        }

        if (SharedClass.getAlarmStatus(SettingsActivity.this, 3)==1){
            txtAsarOn.setTextColor(getResources().getColor(R.color.white));
            txtAsarOn.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
            txtAsarOff.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtAsarOff.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
        }else{
            txtAsarOff.setTextColor(getResources().getColor(R.color.white));
            txtAsarOff.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
            txtAsarOn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtAsarOn.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
        }

        if (SharedClass.getAlarmStatus(SettingsActivity.this, 4)==1){
            txtMaghribOn.setTextColor(getResources().getColor(R.color.white));
            txtMaghribOn.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
            txtMaghribOff.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtMaghribOff.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
        }else{
            txtMaghribOff.setTextColor(getResources().getColor(R.color.white));
            txtMaghribOff.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
            txtMaghribOn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtMaghribOn.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
        }

        if (SharedClass.getAlarmStatus(SettingsActivity.this, 5)==1){
            txtIshaOn.setTextColor(getResources().getColor(R.color.white));
            txtIshaOn.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
            txtIshaOff.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtIshaOff.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
        }else{
            txtIshaOff.setTextColor(getResources().getColor(R.color.white));
            txtIshaOff.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
            txtIshaOn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtIshaOn.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
        }
    }

    void checkCounterSound(){
        if (SharedClass.getTasbeenSound(SettingsActivity.this)==1){
            txtCounterOn.setTextColor(getResources().getColor(R.color.white));
            txtCounterOn.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
            txtCounterOff.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtCounterOff.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
        }else{
            txtCounterOff.setTextColor(getResources().getColor(R.color.white));
            txtCounterOff.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
            txtCounterOn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtCounterOn.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtFajrOn:
                txtFajrOn.setTextColor(getResources().getColor(R.color.white));
                txtFajrOn.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtFajrOff.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtFajrOff.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                SharedClass.setAlarmStatus(SettingsActivity.this, 1, 1);
                break;
            case R.id.txtFajrOff:
                txtFajrOff.setTextColor(getResources().getColor(R.color.white));
                txtFajrOff.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtFajrOn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtFajrOn.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                SharedClass.setAlarmStatus(SettingsActivity.this, 1, 0);
                break;
            case R.id.txtZohrOn:
                txtZohrOn.setTextColor(getResources().getColor(R.color.white));
                txtZohrOn.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtZohrOff.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtZohrOff.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                SharedClass.setAlarmStatus(SettingsActivity.this, 2, 1);
                break;
            case R.id.txtZohrOff:
                txtZohrOff.setTextColor(getResources().getColor(R.color.white));
                txtZohrOff.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtZohrOn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtZohrOn.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                SharedClass.setAlarmStatus(SettingsActivity.this, 2, 0);
                break;
            case R.id.txtAsarOn:
                txtAsarOn.setTextColor(getResources().getColor(R.color.white));
                txtAsarOn.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtAsarOff.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtAsarOff.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                SharedClass.setAlarmStatus(SettingsActivity.this, 3, 1);
                break;
            case R.id.txtAsarOff:
                txtAsarOff.setTextColor(getResources().getColor(R.color.white));
                txtAsarOff.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtAsarOn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtAsarOn.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                SharedClass.setAlarmStatus(SettingsActivity.this, 3, 0);
                break;
            case R.id.txtMaghribOn:
                txtMaghribOn.setTextColor(getResources().getColor(R.color.white));
                txtMaghribOn.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtMaghribOff.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtMaghribOff.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                SharedClass.setAlarmStatus(SettingsActivity.this, 4, 1);
                break;
            case R.id.txtMaghribOff:
                txtMaghribOff.setTextColor(getResources().getColor(R.color.white));
                txtMaghribOff.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtMaghribOn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtMaghribOn.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                SharedClass.setAlarmStatus(SettingsActivity.this, 4, 0);
                break;
            case R.id.txtIshaOn:
                txtIshaOn.setTextColor(getResources().getColor(R.color.white));
                txtIshaOn.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtIshaOff.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtIshaOff.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                SharedClass.setAlarmStatus(SettingsActivity.this, 5, 1);
                break;
            case R.id.txtIshaOff:
                txtIshaOff.setTextColor(getResources().getColor(R.color.white));
                txtIshaOff.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtIshaOn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtIshaOn.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                SharedClass.setAlarmStatus(SettingsActivity.this, 5, 0);
                break;
            case R.id.txtCounterOn:
                txtCounterOn.setTextColor(getResources().getColor(R.color.white));
                txtCounterOn.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtCounterOff.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtCounterOff.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                SharedClass.saveTasbeenSound(SettingsActivity.this, 1);
                break;
            case R.id.txtCounterOff:
                txtCounterOff.setTextColor(getResources().getColor(R.color.white));
                txtCounterOff.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtCounterOn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtCounterOn.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                SharedClass.saveTasbeenSound(SettingsActivity.this, 0);
                break;
            default:
                break;
        }
    }

    public class MethodsCustomAdapter extends BaseAdapter {

        Context context;

        public MethodsCustomAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return SharedClass.methods.length;
        }

        @Override
        public Object getItem(int position) {
            return SharedClass.methods[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = View.inflate(context, R.layout.custom_method_cell, null);

            TextView codeTxt = rowView.findViewById(R.id.countrynametxt);
            codeTxt.setText(SharedClass.methods[position]);

            if (position==flag){
                codeTxt.setTextColor(getResources().getColor(R.color.white));
                codeTxt.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }

            return rowView;
        }
    }

    public void methodsDialog() {

        final Dialog dialog = new Dialog(SettingsActivity.this);
        dialog.setContentView(R.layout.method_cell);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(((getWidth(SettingsActivity.this) / 100) * 80), LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ListView methodsLV = (ListView) dialog.findViewById(R.id.methodsLV);
        methodsCustomAdapter = new MethodsCustomAdapter(SettingsActivity.this);
        methodsLV.setAdapter(methodsCustomAdapter);
        methodsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                flag = i;
                SharedClass.setMethod(SettingsActivity.this, i);
                txtMethod.setText(SharedClass.methods[i]);
                methodsCustomAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public static int getWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private void displayProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
            progressDialog.setMessage("Getting location...");
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

}
