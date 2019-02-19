package com.zhb.vezbanje.adapter;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        final RunningModel runningModel = vezbeModelList.get(position);

        holder.txtName.setText(runningModel.getNazivVezbe());
        holder.txtDetails.setText(runningModel.getDistanca() + "m, " + runningModel.getTrajanje() + "s, " + runningModel.getKalorije() + "cal, " + runningModel.getProsekNa400() + " na 400m, teg - " + runningModel.isTeg());
        holder.txtDate.setText(runningModel.getDatumVezbe().toLocaleString().substring(0, 11));

        // klik na linearLayout
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
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

        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
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


        // sta da se menja of grafike kadase nesto selektuje
        if (rowIndex == position) {
            holder.txtName.setTextColor(Color.parseColor("#c4038d"));
        } else {
            holder.txtName.setTextColor(Color.parseColor("#000000"));
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
        private TextView txtDetails;
        private TextView txtDate;
        private LinearLayout linearLayout;

        RecyclerViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.txtName);
            txtDetails = view.findViewById(R.id.txtDetails);
            txtDate = view.findViewById(R.id.txtDate);
            linearLayout = view.findViewById(R.id.linearLayout);
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