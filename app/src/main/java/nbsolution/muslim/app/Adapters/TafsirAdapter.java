package nbsolution.muslim.app.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import androidx.recyclerview.widget.RecyclerView;
import nbsolution.muslim.app.R;
import nbsolution.muslim.app.Models.Tafsir;
import java.util.ArrayList;

public class TafsirAdapter extends RecyclerView.Adapter<TafsirAdapter.TafsirViewHolder> {

  static final String mimeType = "text/html";
  static final String encoding = "utf-8";

  public Context context;
  public ArrayList<Tafsir> tafsirArrayList;

  public TafsirAdapter(Context context, ArrayList<Tafsir> tafsirArrayList) {
    this.context = context;
    this.tafsirArrayList = tafsirArrayList;
  }

  @Override
  public TafsirAdapter.TafsirViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tafsir, parent, false);
    TafsirAdapter.TafsirViewHolder viewHolder = new TafsirAdapter.TafsirViewHolder(view);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(TafsirAdapter.TafsirViewHolder holder, int position) {
    Tafsir tafsir = tafsirArrayList.get(position);

    String head =
        "<html><head> <style type='text/css'>@font-face {font-family: MyFont;src: url('file:///android_asset/amiri.ttf')}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>";
    String tail = "</body></html>";
    String htmlString = head + tafsir.getTafsirText() + tail;
    holder.tafsirTextWebView.loadDataWithBaseURL(
        "file:///android_asset/", htmlString, mimeType, encoding, null);
    holder.tafsirTextWebView.setBackgroundColor(Color.TRANSPARENT);
  }

  @Override
  public int getItemCount() {
    return tafsirArrayList.size();
  }

  public char[] getUnicodeString(String arString) {
    int charlength = arString.length() / 4;
    char[] arrayOfChar = new char[charlength];
    for (int position = 0; position < charlength; position++) {
      arrayOfChar[position] =
          ((char) Integer.parseInt(arString.substring(position * 4, 4 + position * 4), 16));
    }
    return arrayOfChar;
  }

  public static class TafsirViewHolder extends RecyclerView.ViewHolder {
    public WebView tafsirTextWebView;

    public TafsirViewHolder(View view) {
      super(view);
      tafsirTextWebView = view.findViewById(R.id.tafsirTextWebView);
    }
  }
}
