package nbsolution.muslim.app.intrface;

import android.view.View;

public interface OnItemClickListener {
  void onItemClick(View v, int position);

  void onItemClicktoShare(String name);

  void onItemClicktoCopy(String name);

}
