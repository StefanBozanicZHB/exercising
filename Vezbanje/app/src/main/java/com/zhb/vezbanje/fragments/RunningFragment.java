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
import com.zhb.vezbanje.adapter.RecyclerViewAdapterRunning;
import com.zhb.vezbanje.db.Running.RunningModel;
import com.zhb.vezbanje.db.Running.RunningViewModel;
import com.zhb.vezbanje.db.Vezbe.VezbeModel;
import com.zhb.vezbanje.db.Vezbe.VezbeViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RunningFragment extends Fragment implements View.OnLongClickListener {

    private RunningViewModel viewModel;
    private RecyclerViewAdapterRunning recyclerViewAdapterRunning;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_running, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);

        // u kontruktor se prenosi lista, i longClick event sa ovog contexta
        recyclerViewAdapterRunning = new RecyclerViewAdapterRunning(new ArrayList<RunningModel>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(recyclerViewAdapterRunning);

        // ovo je sablon kako se poziva instanca ViewModela
        // samo se menja get argument gde se prosledjuje klasa gde se nalazu metoda koja se kasnije poziva (delete, add ili updata...)
        viewModel = ViewModelProviders.of(this).get(RunningViewModel.class);



        // svaka promena modela da utice na adapter
        viewModel.getItemAndPersonList().observe(this, new Observer<List<RunningModel>>() {
            @Override
            public void onChanged(@Nullable List<RunningModel> itemAndPeople) {
                recyclerViewAdapterRunning.addItems(itemAndPeople);
            }
        });

        return rootView;
    }

    @Override
    public boolean onLongClick(View v) {
        RunningModel borrowModel = (RunningModel) v.getTag();
        // ako se prosledjuje parametar
        viewModel.deleteItem(borrowModel);
        return true;
    }
}
