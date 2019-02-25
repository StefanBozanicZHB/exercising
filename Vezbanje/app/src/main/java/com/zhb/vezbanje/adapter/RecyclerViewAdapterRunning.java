package com.zhb.vezbanje.adapter;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhb.vezbanje.R;
import com.zhb.vezbanje.db.Exercises.ExercisesModel;
import com.zhb.vezbanje.db.Running.RunningModel;
import com.zhb.vezbanje.db.Running.RunningViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterRunning extends RecyclerView.Adapter<RecyclerViewAdapterRunning.RecyclerViewHolder> {

    // preuzimanje contexta za Toast
    private Context context;

    private List<RunningModel> vezbeModelList;

    // bitan je index za prikazivanje selektovanog item-a
    private int rowIndex;

    // za pomocni meni koji iskace iznad toolbar-a
    private ActionMode actionMode;

    private RunningModel runningModelLastFocus;
    private RunningViewModel runningViewModel;

    public RecyclerViewAdapterRunning(ArrayList<RunningModel> borrowModelList) {
        this.vezbeModelList = borrowModelList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.context = parent.getContext();

        runningViewModel = ViewModelProviders.of((FragmentActivity) parent.getContext()).get(RunningViewModel.class);

        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_running, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        final RunningModel runningModel = vezbeModelList.get(position);

        holder.txtName.setText(runningModel.getNazivVezbe());
        holder.txtDistance.setText(runningModel.getDistanca() + "m");
        holder.txtTime.setText(runningModel.getTrajanje());
        holder.txtCal.setText(runningModel.getKalorije() + "cal");
        holder.txtAvrage.setText(runningModel.getProsekNa400() + "s");

        int viewImage = View.GONE;
        if (runningModel.isTeg()) {
            viewImage = View.VISIBLE;
        }

        holder.imageView.setVisibility(viewImage);

        holder.txtDate.setText(runningModel.getDatumVezbe().toLocaleString().substring(0, 11));

        // klik na linearLayout
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // menja se sadrzaj padajuceg menija kada se klikne
                if (actionMode != null) {
                    actionMode = ((Activity) view.getContext()).startActionMode(actionModeCallback); // za fragmente ((Activity)getContext()). ...
                    actionMode.setTitle(runningModel.getNazivVezbe() + "");
                }

                runningModelLastFocus = runningModel;

                rowIndex = position;
                notifyDataSetChanged();

            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                actionMode = ((Activity) view.getContext()).startActionMode(actionModeCallback); // za fragmente ((Activity)getContext()). ...
                actionMode.setTitle(runningModel.getNazivVezbe() + "");

                runningModelLastFocus = runningModel;

                rowIndex = position;
                notifyDataSetChanged();

                return false;
            }
        });


        if (rowIndex == position) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#80db00c2"));

            holder.txtName.setTextSize(22);
            holder.txtName.setTypeface(null, Typeface.BOLD);
            holder.txtDate.setTypeface(null, Typeface.BOLD);
            holder.txtDistance.setTypeface(null, Typeface.BOLD);
            holder.txtTime.setTypeface(null, Typeface.BOLD);
            holder.txtAvrage.setTypeface(null, Typeface.BOLD);
            holder.txtCal.setTypeface(null, Typeface.BOLD);

        } else {

            holder.cardView.setCardBackgroundColor(Color.parseColor("#0Ddb00c2"));

            holder.txtName.setTextSize(18);
            holder.txtName.setTypeface(null, Typeface.NORMAL);
            holder.txtDate.setTypeface(null, Typeface.NORMAL);
            holder.txtDistance.setTypeface(null, Typeface.NORMAL);
            holder.txtTime.setTypeface(null, Typeface.NORMAL);
            holder.txtAvrage.setTypeface(null, Typeface.NORMAL);
            holder.txtCal.setTypeface(null, Typeface.NORMAL);

        }
        holder.itemView.setTag(runningModel);
    }

    @Override
    public int getItemCount() {
        return vezbeModelList.size();
    }

    public void addItems(List<RunningModel> runningModels) {
        this.vezbeModelList = runningModels;
        notifyDataSetChanged();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName;
        private TextView txtDistance;
        private TextView txtTime;
        private TextView txtCal;
        private TextView txtAvrage;
        private TextView txtDate;

        private ImageView imageView;

        private CardView cardView;

        RecyclerViewHolder(View view) {
            super(view);

            txtName = view.findViewById(R.id.txtName);
            txtDistance = view.findViewById(R.id.txtDistance);
            txtTime = view.findViewById(R.id.txtTime);
            txtCal = view.findViewById(R.id.txtCal);
            txtAvrage = view.findViewById(R.id.txtAvrage);
            txtDate = view.findViewById(R.id.txtDate);

            imageView = view.findViewById(R.id.imageView);

            cardView = view.findViewById(R.id.cardView);
        }
    }

    // calback koji poziva pomocni meni i gde se nalazi sva logika za pozivanje
    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.context_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_delete:
                    runningViewModel.deleteItem(runningModelLastFocus);
                    Toast.makeText(context, "Deleting: " + runningModelLastFocus.getNazivVezbe() + " " + runningModelLastFocus.getDatumVezbe(), Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;
                case R.id.menu_edit:
                    Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
        }
    };
}