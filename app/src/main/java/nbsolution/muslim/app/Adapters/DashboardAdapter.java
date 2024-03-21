package nbsolution.muslim.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import nbsolution.muslim.app.Activities.AllahNames;
import nbsolution.muslim.app.Activities.AzkarCategoriesActivity;
import nbsolution.muslim.app.Activities.CounterActivity;
import nbsolution.muslim.app.Activities.PrayerSetup;
import nbsolution.muslim.app.Activities.QuranMain;
import nbsolution.muslim.app.Activities.SettingsActivity;
import nbsolution.muslim.app.Activities.SixKalmasActivity;
import nbsolution.muslim.app.R;
import nbsolution.muslim.app.compass.QiblaFinder;
// import nbsolution.muslim.app.prayer.PrayerActivity;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.MyAdapter> {

    String[] options = {"Prayer Timings", "Learn Quran", "6 kalma", "Azkar", "Allah 99 Names", "Tasbeeh Counter", "Qibla Finder", "Settings"};
    Integer[] icons = {R.drawable.timings, R.drawable.quran, R.drawable.kalma, R.drawable.dua, R.drawable.allahnames,R.drawable.counter, R.drawable.compass, R.drawable.settings};
    private LayoutInflater inflater;
    Context context;

    public DashboardAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater==null){
            inflater=LayoutInflater.from(parent.getContext());
        }
        View view = inflater.inflate(R.layout.dashboard_cell, parent, false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        holder.optionTxt.setText(options[position]);
        holder.optionimage.setImageResource(icons[position]);
    }

    @Override
    public int getItemCount() {
        return options.length;
    }

    public class MyAdapter extends RecyclerView.ViewHolder {

        TextView optionTxt;
        ImageView optionimage;

        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            optionTxt=itemView.findViewById(R.id.optionTxt);
            optionimage=itemView.findViewById(R.id.optionimage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fun(getAbsoluteAdapterPosition());
                }
            });
        }
    }

    public void fun(final int pos){
        if (pos==0){
            context.startActivity(new Intent(context, PrayerSetup.class));
        }
        if (pos==1){
            context.startActivity(new Intent(context, QuranMain.class));
        }
        if (pos==2){
            context.startActivity(new Intent(context, SixKalmasActivity.class));
        }
        if (pos==3){
            context.startActivity(new Intent(context, AzkarCategoriesActivity.class));
        }
        if (pos==4){
            context.startActivity(new Intent(context, AllahNames.class));
        }
        if (pos==5){
            context.startActivity(new Intent(context, CounterActivity.class));
        }
        if (pos==6){
            context.startActivity(new Intent(context, QiblaFinder.class));
        }
        if (pos==7){
            context.startActivity(new Intent(context, SettingsActivity.class));
        }
    }

}