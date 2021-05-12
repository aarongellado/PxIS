package com.example.pxisjavaver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class chkupListAdapter extends RecyclerView.Adapter<chkupListAdapter.ViewHolder> {


    public chkupListAdapter(List<chkupListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    private List<chkupListItem> listItems;
    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_px_checkup, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        chkupListItem listItem = listItems.get(position);
        holder.chkupDateTV.setText(listItem.getChkUpDate());
        holder.chkupDiagTV.setText(listItem.getChkUpDiags());
        holder.chkupDocTV.setText(listItem.getChkUpDoc());

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView chkupDateTV;
        public TextView chkupDiagTV;
        public TextView chkupDocTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chkupDateTV = (TextView) itemView.findViewById(R.id.checkupListDate);
            chkupDiagTV = (TextView) itemView.findViewById(R.id.checkupDiags);
            chkupDocTV = (TextView) itemView.findViewById(R.id.checkupDrName);

        }
    }
}
