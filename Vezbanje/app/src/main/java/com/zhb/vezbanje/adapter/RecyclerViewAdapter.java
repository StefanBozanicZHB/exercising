package com.zhb.vezbanje.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhb.vezbanje.R;
import com.zhb.vezbanje.db.Vezbe.VezbeModel;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    // dve stvari su bitne
    // prva je koja lista je u pitanju
    // drugo koji dogadjaji su u pitanju
    private List<VezbeModel> vezbeModelList;
    // iako se poziva long click u MainActivity mora i ovde da se referencira
    private View.OnLongClickListener longClickListener;

    public RecyclerViewAdapter(List<VezbeModel> borrowModelList, View.OnLongClickListener longClickListener) {
        this.vezbeModelList = borrowModelList;
        this.longClickListener = longClickListener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        VezbeModel vezbeModel = vezbeModelList.get(position);
        holder.txtName.setText(vezbeModel.getNazivVezbe());
        holder.txtDetails.setText(
                vezbeModel.getBrojSerija() +
                "x" +
                vezbeModel.getBrojPonavljanja() + " " +
                vezbeModel.getBrojKilograma() + "kg"
        );
        holder.txtDate.setText(vezbeModel.getDatumVezbe().toLocaleString().substring(0, 11));

        holder.itemView.setTag(vezbeModel);
        holder.itemView.setOnLongClickListener(longClickListener);
    }

    @Override
    public int getItemCount() {
        return vezbeModelList.size();
    }

    public void addItems(List<VezbeModel> borrowModelList) {
        this.vezbeModelList = borrowModelList;
        notifyDataSetChanged();
    }

    // pandam onCreate u Activity, da se preuznu vrednosti
    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private TextView txtDetails;
        private TextView txtDate;

        RecyclerViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.txtName);
            txtDetails = view.findViewById(R.id.txtDetails);
            txtDate = view.findViewById(R.id.txtDate);
        }
    }
}