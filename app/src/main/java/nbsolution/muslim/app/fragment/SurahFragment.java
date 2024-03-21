package nbsolution.muslim.app.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nbsolution.muslim.app.Activities.AyahWordActivity;
import nbsolution.muslim.app.Adapters.SurahAdapter;
import nbsolution.muslim.app.Models.Surah;
import nbsolution.muslim.app.R;
import nbsolution.muslim.app.database.datasource.SurahDataSource;
import nbsolution.muslim.app.intrface.OnItemClickListener;
import nbsolution.muslim.app.utils.settings.Config;

/** A simple {@link Fragment} subclass. */
public class SurahFragment extends Fragment {

  static String lang;
  private ArrayList<Surah> surahArrayList;
  private RecyclerView mRecyclerView;
  private SurahAdapter surahAdapter;
  private RecyclerView.LayoutManager mLayoutManager;

  public SurahFragment() {
    // Required empty public constructor
  }

  public static SurahFragment newInstance() {
    SurahFragment surahFragment = new SurahFragment();
    return surahFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
    lang = sp.getString(Config.LANG, Config.defaultLang);
    surahArrayList = getSurahArrayList();
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_surah, container, false);
    mRecyclerView = view.findViewById(R.id.recycler_surah_view);
    surahAdapter = new SurahAdapter(surahArrayList, getActivity());

    return view;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mRecyclerView.setAdapter(surahAdapter);

    mRecyclerView.setHasFixedSize(true);
    // use a linear layout manager
    mLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    surahAdapter.SetOnItemClickListener(
        new OnItemClickListener() {
          @Override
          public void onItemClick(View v, int position) {
            Surah surah = (Surah) surahAdapter.getItem(position);

            long surah_id = surah.getId();
            long ayah_number = surah.getAyahNumber();
            String surah_name = surah.getNameTranslate();

            Log.d("SurahFragment", "ID: " + surah_id + " Surah Name: " + surah_name);

            Bundle dataBundle = new Bundle();
            dataBundle.putLong(SurahDataSource.SURAH_ID_TAG, surah_id);
            dataBundle.putLong(SurahDataSource.SURAH_AYAH_NUMBER, ayah_number);
            dataBundle.putString(SurahDataSource.SURAH_NAME_TRANSLATE, surah_name);

            Intent intent = new Intent(getActivity(), AyahWordActivity.class);
            intent.putExtras(dataBundle);
            startActivity(intent);
          }

          @Override
          public void onItemClicktoShare(String name) {
            // do nothing
          }

          @Override
          public void onItemClicktoCopy(String name) {
            // do nothing
          }
        });
  }

  private ArrayList<Surah> getSurahArrayList() {
    ArrayList<Surah> surahArrayList = new ArrayList<Surah>();
    SurahDataSource surahDataSource = new SurahDataSource(getActivity());

    switch (lang) {
      case Config.LANG_BN:
        surahArrayList = surahDataSource.getBanglaSurahArrayList();
        break;
      case Config.LANG_INDO:
        surahArrayList = surahDataSource.getIndonesianSurahArrayList();
        break;
      case Config.LANG_EN:
        surahArrayList = surahDataSource.getEnglishSurahArrayList();
        break;
    }

    return surahArrayList;
  }
}
