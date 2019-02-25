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

    VezbeModel vezbeModelForDelete;
    VezbeViewModel viewModel;

    public RecyclerViewAdapter(List<VezbeModel> borrowModelList) {
        this.vezbeModelList = borrowModelList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.ctx = parent.getContext();

        viewModel = ViewModelProviders.of((FragmentActivity) parent.getContext()).get(VezbeViewModel.class);

        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        final VezbeModel vezbeModel = vezbeModelList.get(position);

        holder.txtName.setText(vezbeModel.getNazivVezbe());
        holder.txtDetails.setText(vezbeModel.getBrojSerija() + "x" + vezbeModel.getBrojPonavljanja() + " " + vezbeModel.getBrojKilograma() + "kg");
        holder.txtDate.setText(vezbeModel.getDatumVezbe().toLocaleString().substring(0, 11));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // menja se sadrzaj padajuceg menija kada se klikne
                if (mActionMode != null) {
                    mActionMode = ((Activity) view.getContext()).startActionMode(mActionModeCallback); // za fragmente ((Activity)getContext()). ...
                    mActionMode.setTitle(vezbeModel.getNazivVezbe() + " " + vezbeModel.getDatumVezbe().toLocaleString().substring(0, 6));

                }

                vezbeModelForDelete = vezbeModel;

                row_index = position;
                notifyDataSetChanged();

            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                row_index = position;
                notifyDataSetChanged();

                mActionMode = ((Activity) view.getContext()).startActionMode(mActionModeCallback); // za fragmente ((Activity)getContext()). ...
                mActionMode.setTitle(vezbeModel.getNazivVezbe() + "");

                vezbeModelForDelete = vezbeModel;

                return false;
            }
        });


        if (row_index == position) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#80db00c2"));
//            holder.txtName.setTextColor(Color.parseColor("#c4038d"));
//            holder.txtDate.setTextColor(Color.parseColor("#c4038d"));
//            holder.txtDetails.setTextColor(Color.parseColor("#c4038d"));

            holder.txtName.setTextSize(22);
            holder.txtName.setTypeface(null, Typeface.BOLD);
            holder.txtDate.setTypeface(null, Typeface.BOLD);
            holder.txtDetails.setTypeface(null, Typeface.BOLD);

        } else {

            holder.cardView.setCardBackgroundColor(Color.parseColor("#0Ddb00c2"));
//            holder.txtName.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.txtDetails.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.txtDate.setTextColor(Color.parseColor("#FFFFFF"));

            holder.txtName.setTextSize(18);
            holder.txtName.setTypeface(null, Typeface.NORMAL);
            holder.txtDate.setTypeface(null, Typeface.NORMAL);
            holder.txtDetails.setTypeface(null, Typeface.NORMAL);

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
        private CardView cardView;

        RecyclerViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.txtName);
            txtDetails = view.findViewById(R.id.txtDetails);
            txtDate = view.findViewById(R.id.txtDate);
            cardView = view.findViewById(R.id.cardView);
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
                    viewModel.deleteItem(vezbeModelForDelete);
                    Toast.makeText(ctx, "Deleting: " + vezbeModelForDelete.getNazivVezbe() + " " + vezbeModelForDelete.getDatumVezbe() + "x" + vezbeModelForDelete.getBrojPonavljanja(), Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;
                case R.id.menu_edit:
                    Toast.makeText(ctx, "Edi", Toast.LENGTH_SHORT).show();
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