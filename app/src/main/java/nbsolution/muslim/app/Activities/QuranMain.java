package nbsolution.muslim.app.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;
import java.util.Locale;

import nbsolution.muslim.app.Models.Surah;
import nbsolution.muslim.app.R;
import nbsolution.muslim.app.database.DatabaseHelper;
import nbsolution.muslim.app.database.datasource.SurahDataSource;
import nbsolution.muslim.app.fragment.SurahFragment;
import nbsolution.muslim.app.utils.settings.Config;

public class QuranMain extends AppCompatActivity {

  ImageView backBtn;
  static String lang;
  SharedPreferences FirstRunPrefs = null;
  SharedPreferences dbVersionPrefs = null;
  SharedPreferences sharedPreferences;
  ProgressDialog progressDialog;
  private InterstitialAd mInterstitialAd;
  private static final String TAG = "QuranMain";


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.quran_main);

//    Toolbar toolbar = findViewById(R.id.toolbar);
//    setSupportActionBar(toolbar);
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

    backBtn = findViewById(R.id.backBtn);
    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        getOnBackPressedDispatcher().onBackPressed();
      }
    });

    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    FirstRunPrefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
    dbVersionPrefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);

    if ((FirstRunPrefs.getBoolean(Config.FIRST_RUN, false))
        || (dbVersionPrefs.getInt(Config.DATABASE_VERSION, 0) == DatabaseHelper.DATABASE_VERSION)) {
      if (savedInstanceState == null) {
        lang = sharedPreferences.getString(Config.LANG, Config.defaultLang);

        if (lang.equals(Config.LANG_BN)) {
          setLocaleBangla();
        } else {
          setLocaleEnglish();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
            .beginTransaction()
            .replace(R.id.main_container, SurahFragment.newInstance())
            .commit();
      }
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (DatabaseHelper.DATABASE_VERSION > dbVersionPrefs.getInt(Config.DATABASE_VERSION, 0)) {
      Log.d("MyActivity onResume()", "First Run or dbUpgrade");
      {
        new AsyncInsertData().execute();
      }
    } // checking sharedPrefs finished
  }

  public void showAds(){
    if (mInterstitialAd != null) {
      mInterstitialAd.show(this);
    } else {
      System.out.println("working12--");
      Log.d("TAG", "The interstitial ad wasn't ready yet.");
    }
  }


  public boolean setLanguage() {

      String[] lang = getResources().getStringArray(R.array.lang_names);

    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setCancelable(true);
    builder.setTitle(getResources().getString(R.string.lang));
    builder.setItems(
        lang,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            // the user clicked on colors[which]
            switch (which) {
              case 0:
                SharedPreferences.Editor ed1 = sharedPreferences.edit();
                ed1.clear();
                ed1.putString(Config.LANG, Config.LANG_BN);
                ed1.apply();

                setLocaleBangla();

                FragmentManager fragmentManager1 = getSupportFragmentManager();
                fragmentManager1
                    .beginTransaction()
                    .replace(R.id.main_container, SurahFragment.newInstance())
                    .commit();

                break;
              case 1:
                SharedPreferences.Editor ed2 = sharedPreferences.edit();
                ed2.clear();
                ed2.putString(Config.LANG, Config.LANG_EN);
                ed2.apply();
                setLocaleEnglish();
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                fragmentManager2
                    .beginTransaction()
                    .replace(R.id.main_container, SurahFragment.newInstance())
                    .commit();
                break;
              case 2:
                SharedPreferences.Editor ed3 = sharedPreferences.edit();
                ed3.clear();
                ed3.putString(Config.LANG, Config.LANG_INDO);
                ed3.apply();
                setLocaleEnglish();
                FragmentManager fragmentManager3 = getSupportFragmentManager();
                fragmentManager3
                    .beginTransaction()
                    .replace(R.id.main_container, SurahFragment.newInstance())
                    .commit();
                break;
            }
          }
        });
    builder.show();
    return true;
  }

  public void setLocaleBangla() {
    Locale locale = new Locale(Config.LANG_BN);
    Locale.setDefault(locale);
    Configuration config = new Configuration();
    config.locale = locale;
    getBaseContext()
        .getResources()
        .updateConfiguration(config, getApplicationContext().getResources().getDisplayMetrics());
  }

  public void setLocaleEnglish() {
    Locale locale = new Locale(Config.LANG_EN);
    Locale.setDefault(locale);
    Configuration config = new Configuration();
    config.locale = locale;
    getBaseContext()
        .getResources()
        .updateConfiguration(config, getApplicationContext().getResources().getDisplayMetrics());
  }

  private class AsyncInsertData extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {

      progressDialog = new ProgressDialog(QuranMain.this);
      progressDialog.setMessage("Please wait..");
      progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
      progressDialog.setIndeterminate(true);
      progressDialog.setCancelable(true);
      progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {

      Log.d("onInBackground()", "Data Inserting ");
      SurahDataSource surahDataSource = new SurahDataSource(QuranMain.this);
      ArrayList<Surah> surahArrayList = surahDataSource.getEnglishSurahArrayList();

      return null;
    }

    @Override
    protected void onPostExecute(Void result) {
      super.onPostExecute(result);
      Log.d("MainActivity", "Data Inserted ");

      dbVersionPrefs
          .edit()
          .putInt(Config.DATABASE_VERSION, DatabaseHelper.DATABASE_VERSION)
          .apply();
      progressDialog.dismiss();

      if (FirstRunPrefs.getBoolean(Config.FIRST_RUN, false)) {
        setLanguage();
        FirstRunPrefs.edit().putBoolean(Config.FIRST_RUN, true).apply();
      } else {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
            .beginTransaction()
            .replace(R.id.main_container, SurahFragment.newInstance())
            .commit();
      }
    }
  }
}
