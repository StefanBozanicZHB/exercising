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
import android.widget.Button;
import android.widget.EditText;

import com.zhb.vezbanje.R;
import com.zhb.vezbanje.adapter.RecyclerViewAdapter;
import com.zhb.vezbanje.adapter.RecyclerViewAdapterExercises;
import com.zhb.vezbanje.db.Exercises.ExercisesModel;
import com.zhb.vezbanje.db.Exercises.ExercisesViewModel;
import com.zhb.vezbanje.db.Vezbe.VezbeModel;
import com.zhb.vezbanje.db.Vezbe.VezbeViewModel;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment implements View.OnLongClickListener{

    private ExercisesViewModel exercisesViewModel;
    private RecyclerViewAdapterExercises recyclerViewAdapterExercises;
    private RecyclerView recyclerViewExercise;

    private EditText edtNameExercise;

    private Button btnSubmitExercise;


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        recyclerViewExercise = rootView.findViewById(R.id.recyclerViewExercise);

        recyclerViewAdapterExercises = new RecyclerViewAdapterExercises(new ArrayList<ExercisesModel>(), this);
        recyclerViewExercise.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewExercise.setAdapter(recyclerViewAdapterExercises);

        exercisesViewModel = ViewModelProviders.of(this).get(ExercisesViewModel.class);

        exercisesViewModel.getItemAndPersonList2().observe(this, new Observer<List<ExercisesModel>>() {
            @Override
            public void onChanged(@Nullable List<ExercisesModel> itemAndPeople) {
                recyclerViewAdapterExercises.addItems(itemAndPeople);
            }
        });


        //on click
        edtNameExercise = rootView.findViewById(R.id.edtNameExercise);
        btnSubmitExercise = rootView.findViewById(R.id.btnSubmitExercise);

        btnSubmitExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercisesViewModel.addBorrow(new ExercisesModel(edtNameExercise.getText().toString()));
            }
        });


        return rootView;
    }

    @Override
    public boolean onLongClick(View v) {
        ExercisesModel borrowModel = (ExercisesModel) v.getTag();
        exercisesViewModel.deleteItem(borrowModel);
        return true;
    }

}
