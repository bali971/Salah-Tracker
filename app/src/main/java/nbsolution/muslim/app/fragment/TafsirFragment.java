package nbsolution.muslim.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nbsolution.muslim.app.Adapters.TafsirAdapter;
import nbsolution.muslim.app.Models.Tafsir;
import nbsolution.muslim.app.R;
import nbsolution.muslim.app.database.datasource.AyahWordDataSource;
import nbsolution.muslim.app.database.datasource.TafsirKathirEnglishDataSource;


public class TafsirFragment extends Fragment {

  public ArrayList<Tafsir> tafsirArrayList;
  long surah_id;
  long verse_id;
  private RecyclerView mRecyclerView;
  private TafsirAdapter tafsirAdapter;
  private RecyclerView.LayoutManager mLayoutManager;

  public TafsirFragment() {}

  public static TafsirFragment newInstance(Bundle bundle) {
    TafsirFragment tafsirFragment = new TafsirFragment();
    tafsirFragment.setArguments(bundle);
    return tafsirFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    surah_id = getArguments().getLong(AyahWordDataSource.AYAHWORD_SURAH_ID);
    verse_id = getArguments().getLong(AyahWordDataSource.AYAHWORD_VERSE_ID);
    tafsirArrayList = getTafsirKathirEnglishArrayList(surah_id, verse_id);
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_tafsir, container, false);
    mRecyclerView = view.findViewById(R.id.recycler_tafsir_view);
    tafsirAdapter = new TafsirAdapter(getActivity(), tafsirArrayList);
    return view;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mRecyclerView.setAdapter(tafsirAdapter);
    mRecyclerView.setHasFixedSize(true);
    mLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
  }

  private ArrayList<Tafsir> getTafsirKathirEnglishArrayList(long surah_id, long verse_id) {
    ArrayList<Tafsir> tafsirArrayList;
    TafsirKathirEnglishDataSource tafsirKathirEnglishDataSource =
        new TafsirKathirEnglishDataSource(getActivity());
    tafsirArrayList =
        tafsirKathirEnglishDataSource.getTafsirKathirEnglishArrayList(surah_id, verse_id);
    return tafsirArrayList;
  }
}
