package com.zhb.vezbanje.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhb.vezbanje.R;
import com.zhb.vezbanje.db.Exercises.ExercisesModel;
import com.zhb.vezbanje.db.Running.RunningModel;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterRunning extends RecyclerView.Adapter<RecyclerViewAdapterRunning.RecyclerViewHolder> {

    private List<RunningModel> vezbeModelList;
    private View.OnLongClickListener longClickListener;

    public RecyclerViewAdapterRunning(ArrayList<RunningModel> borrowModelList, View.OnLongClickListener longClickListener) {
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
        RunningModel vezbeModel = vezbeModelList.get(position);

        holder.txtName.setText(vezbeModel.getNazivVezbe());
        holder.txtDetails.setText(
                vezbeModel.getDistanca() + "m, "+ vezbeModel.getTrajanje() + "s, " +
                        vezbeModel.getKalorije() + "cal, "+vezbeModel.getProsekNa400()+" na 400m, teg - "+ vezbeModel.isTeg()
        );
        holder.txtDate.setText(vezbeModel.getDatumVezbe().toLocaleString().substring(0, 11));

        holder.itemView.setTag(vezbeModel);
        holder.itemView.setOnLongClickListener(longClickListener);
    }

    @Override
    public int getItemCount() {
        return vezbeModelList.size();
    }

    public void addItems(List<RunningModel> borrowModelList) {
        this.vezbeModelList = borrowModelList;
        notifyDataSetChanged();
    }

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