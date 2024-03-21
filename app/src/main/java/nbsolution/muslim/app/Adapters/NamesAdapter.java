package nbsolution.muslim.app.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nbsolution.muslim.app.Models.NamesModel;
import nbsolution.muslim.app.R;
import nbsolution.muslim.app.intrface.OnItemClickListener;

public class NamesAdapter extends RecyclerView.Adapter<NamesAdapter.MyAdapter> {

    private LayoutInflater inflater;
    Context context;
    List<NamesModel> data;

    private OnItemClickListener buttonClickListener;

    public NamesAdapter(Context context, List<NamesModel> data, OnItemClickListener buttonClickListener) {
        this.context = context;
        this.data = data;
        this.buttonClickListener = buttonClickListener;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater==null){
            inflater=LayoutInflater.from(parent.getContext());
        }
        View rowView = inflater.inflate(R.layout.name_cell, parent, false);
        return new MyAdapter(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        final NamesModel currentItem = data.get(position);
        holder.engTxt.setText(data.get(position).getTransliteration());
        holder.arabicTxt.setText(data.get(position).getName());
        holder.translationTxt.setText(data.get(position).getMeaning());
        holder.btnShare1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG123", holder.arabicTxt.getText().toString());
                buttonClickListener.onItemClicktoShare(holder.arabicTxt.getText().toString());
            }
        });
        holder.btnCopy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickListener.onItemClicktoCopy(currentItem.getName());
            }
        });

        int pos = position+1;
        holder.txtNum.setText(String.valueOf(pos));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {

        TextView engTxt, arabicTxt, translationTxt, txtNum;
        ImageView btnCopy1, btnShare1;



        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            txtNum = itemView.findViewById(R.id.txtNum);
            engTxt = itemView.findViewById(R.id.engTxt);
            arabicTxt = itemView.findViewById(R.id.arabictxt);
            translationTxt = itemView.findViewById(R.id.translationTxt);
            btnCopy1 = itemView.findViewById(R.id.btnCopy1);
            btnShare1 = itemView.findViewById(R.id.btnShare1);
        }

    }
}
