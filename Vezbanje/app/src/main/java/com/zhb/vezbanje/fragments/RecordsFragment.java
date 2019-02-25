package com.zhb.vezbanje.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhb.vezbanje.R;
import com.zhb.vezbanje.adapter.RecyclerViewAdapter;
import com.zhb.vezbanje.db.Vezbe.VezbeModel;
import com.zhb.vezbanje.db.Vezbe.VezbeViewModel;

import java.util.ArrayList;
import java.util.List;


public class RecordsFragment extends Fragment  {

    private VezbeViewModel viewModel;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_records, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);

        recyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<VezbeModel>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(recyclerViewAdapter);

        viewModel = ViewModelProviders.of(this).get(VezbeViewModel.class);

        viewModel.getItemAndPersonList().observe(this, new Observer<List<VezbeModel>>() {
            @Override
            public void onChanged(@Nullable List<VezbeModel> itemAndPeople) {
                recyclerViewAdapter.addItems(itemAndPeople);
            }
        });

        return rootView;
    }
}
