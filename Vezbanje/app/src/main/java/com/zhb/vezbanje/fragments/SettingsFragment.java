package com.zhb.vezbanje.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.zhb.vezbanje.R;
import com.zhb.vezbanje.SharedPrefManager;
import com.zhb.vezbanje.adapter.RecyclerViewAdapter;
import com.zhb.vezbanje.adapter.RecyclerViewAdapterExercises;
import com.zhb.vezbanje.db.Exercises.ExercisesModel;
import com.zhb.vezbanje.db.Exercises.ExercisesViewModel;
import com.zhb.vezbanje.db.Vezbe.VezbeModel;
import com.zhb.vezbanje.db.Vezbe.VezbeViewModel;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment implements View.OnLongClickListener {

    private ExercisesViewModel exercisesViewModel;
    private RecyclerViewAdapterExercises recyclerViewAdapterExercises;
    private RecyclerView recyclerViewExercise;

    private EditText edtNameExercise, edtAge, edtGender;

    private Button btnSubmitExercise, btnAge;

    private final String FILENAME = "testfile.txt";

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

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


        edtAge = rootView.findViewById(R.id.edtAge);
        edtGender = rootView.findViewById(R.id.edtGender);
        btnAge = rootView.findViewById(R.id.btnAge);

        edtAge.setText(SharedPrefManager.getInstance(getContext()).getAge());
        edtGender.setText(SharedPrefManager.getInstance(getContext()).getGender());

        btnAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(getContext()).saveAge(edtAge.getText().toString());
                SharedPrefManager.getInstance(getContext()).saveGendel(edtGender.getText().toString());
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
