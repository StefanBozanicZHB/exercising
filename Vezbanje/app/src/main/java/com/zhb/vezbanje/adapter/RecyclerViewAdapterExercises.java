package com.zhb.vezbanje.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhb.vezbanje.R;
import com.zhb.vezbanje.db.Exercises.ExercisesModel;
import com.zhb.vezbanje.db.Vezbe.VezbeModel;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterExercises extends RecyclerView.Adapter<RecyclerViewAdapterExercises.RecyclerViewHolder> {

    private List<ExercisesModel> vezbeModelList;
    private View.OnLongClickListener longClickListener;

    public RecyclerViewAdapterExercises(ArrayList<ExercisesModel> borrowModelList, View.OnLongClickListener longClickListener) {
        this.vezbeModelList = borrowModelList;
        this.longClickListener = longClickListener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_exercise, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        ExercisesModel vezbeModel = vezbeModelList.get(position);

        holder.txtName.setText(vezbeModel.getExercisesName());

        holder.itemView.setTag(vezbeModel);
        holder.itemView.setOnLongClickListener(longClickListener);
    }

    @Override
    public int getItemCount() {
        return vezbeModelList.size();
    }

    public void addItems(List<ExercisesModel> borrowModelList) {
        this.vezbeModelList = borrowModelList;
        notifyDataSetChanged();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;

        RecyclerViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.txtName);
        }
    }
}