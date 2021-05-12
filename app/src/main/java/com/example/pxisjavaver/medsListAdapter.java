package com.example.pxisjavaver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class medsListAdapter extends RecyclerView.Adapter<medsListAdapter.ViewHolder> {

    private List<medsListItem> listItems;
    private Context context;

    public medsListAdapter(List<medsListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_px_meds, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        medsListItem listItem = listItems.get(position);
        holder.medsDateTV.setText("Date: "+listItem.getMedsDate());
        holder.medsPrescTV.setText(listItem.getMedsPresc());
        holder.medsDocNameTV.setText("Doctor: "+listItem.getMedsDocName());
        holder.medsValidDateTV.setText("Valid Until: "+listItem.getMedsValidDate());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView medsDateTV;
        public TextView medsPrescTV;
        public TextView medsDocNameTV;
        public TextView medsValidDateTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            medsDateTV = (TextView)itemView.findViewById(R.id.medsListDate);
            medsPrescTV = (TextView)itemView.findViewById(R.id.medsPresc);
            medsDocNameTV = (TextView)itemView.findViewById(R.id.medsDrName);
            medsValidDateTV = (TextView)itemView.findViewById(R.id.medsDateValid);
        }
    }
}
