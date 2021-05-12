package com.example.pxisjavaver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class drAptListAdapter extends RecyclerView.Adapter<drAptListAdapter.ViewHolder> {
    private List<drAptListItem> listItems;
    private Context context;

    public drAptListAdapter(List<drAptListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_dr_aptmt_rv, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        drAptListItem listItem = listItems.get(position);
        holder.pxAptPurpose.setText(listItem.getDrAptPurpose());
        holder.pxAptName.setText(listItem.getDrAptName());
        holder.pxAptDate.setText(listItem.getDrAptDate());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView pxAptPurpose;
        public TextView pxAptName;
        public TextView pxAptDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pxAptPurpose = (TextView)itemView.findViewById(R.id.drAptPurposeListTV);
            pxAptName = (TextView)itemView.findViewById(R.id.drAptNameListTV);
            pxAptDate = (TextView)itemView.findViewById(R.id.drAptDateListTV);

        }
    }
}
