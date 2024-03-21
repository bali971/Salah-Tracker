package nbsolution.muslim.app.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import nbsolution.muslim.app.R;
import nbsolution.muslim.app.fragment.TafsirFragment;


public class TafsirActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tafsir);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    Bundle bundle = this.getIntent().getExtras();

    if (savedInstanceState == null) {
      FragmentManager fragmentManager = getSupportFragmentManager();

      fragmentManager
          .beginTransaction()
          .replace(R.id.main_container, TafsirFragment.newInstance(bundle))
          .commit();
    }
  }
}
