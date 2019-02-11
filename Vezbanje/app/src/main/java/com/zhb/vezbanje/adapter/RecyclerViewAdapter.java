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
import com.zhb.vezbanje.db.Vezbe.VezbeModel;
import com.zhb.vezbanje.db.Vezbe.VezbeViewModel;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    int row_index;
    private List<VezbeModel> vezbeModelList;

    ActionMode mActionMode;
    Context ctx;

    public RecyclerViewAdapter(List<VezbeModel> borrowModelList) {
        this.vezbeModelList = borrowModelList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.ctx = parent.getContext();

        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {

        final VezbeModel vezbeModel = vezbeModelList.get(position);
        holder.txtName.setText(vezbeModel.getNazivVezbe());
        holder.txtDetails.setText(
                vezbeModel.getBrojSerija() +
                        "x" +
                        vezbeModel.getBrojPonavljanja() + " " +
                        vezbeModel.getBrojKilograma() + "kg"
        );
        holder.txtDate.setText(vezbeModel.getDatumVezbe().toLocaleString().substring(0, 11));


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                notifyDataSetChanged();

            }
        });

        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mActionMode != null) return false;
                mActionMode = ((Activity) v.getContext()).startActionMode(mActionModeCallback); // za fragmente ((Activity)getContext()). ...
                mActionMode.setTitle("New Title");

//                VezbeViewModel viewModel;
//                viewModel = ViewModelProviders.of((FragmentActivity) v.getContext()).get(VezbeViewModel.class);
//
//                Toast.makeText(v.getContext(), "dugi", Toast.LENGTH_SHORT).show();
//                viewModel.deleteItem(vezbeModel);
                return false;
            }
        });


        if (row_index == position) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#567845"));
            holder.txtName.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.txtName.setTextColor(Color.parseColor("#000000"));
        }

        holder.itemView.setTag(vezbeModel);
    }

    @Override
    public int getItemCount() {
        return vezbeModelList.size();
    }

    public void addItems(List<VezbeModel> borrowModelList) {
        this.vezbeModelList = borrowModelList;
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

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
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
                    Toast.makeText(ctx, "Cast", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;
                case R.id.menu_edit:
                    Toast.makeText(ctx, "Print", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };
}