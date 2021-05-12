package com.example.pxisjavaver;

import android.app.LauncherActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class aptListAdapter extends RecyclerView.Adapter<aptListAdapter.ViewHolder> {

    public aptListAdapter(List<aptListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    private List<aptListItem> listItems;
    private Context context;



    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_px_aptmt_rv, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        aptListItem listItem = listItems.get(position);

        holder.aptPurposeTV.setText(listItem.getAptPurpose());
        holder.aptDateTV.setText(listItem.getAptDate());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView aptPurposeTV;
        public TextView aptDateTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            aptPurposeTV = (TextView)itemView.findViewById(R.id.aptListPurpose);
            aptDateTV = (TextView)itemView.findViewById(R.id.aptListDate);

        }
    }
}
