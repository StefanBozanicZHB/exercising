package com.zhb.vezbanje.fragments;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.zhb.vezbanje.R;
import com.zhb.vezbanje.adapter.RecyclerViewAdapterExercises;
import com.zhb.vezbanje.db.Exercises.ExercisesModel;
import com.zhb.vezbanje.db.Exercises.ExercisesViewModel;
import com.zhb.vezbanje.db.Vezbe.VezbeModel;
import com.zhb.vezbanje.db.Vezbe.VezbeViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.graphics.Color;
import android.widget.TextView;

public class InputFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private Date date;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;

    private EditText edtSerije;
    private EditText edtPonavaljnje;
    private EditText edtKilaza;

    private Spinner spnExerciseName;

    private VezbeViewModel borrowedListViewModel;
    private ExercisesViewModel exercisesViewModel;

    private FloatingActionButton fab;

    private View rootView;

    private static VezbeModel lastVezbeModel;

    List<String> spinnerArray;

    public InputFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_input, container, false);

        initialisationComponents();

        borrowedListViewModel = ViewModelProviders.of(this).get(VezbeViewModel.class);

        fabSetOnClickListener();

        setCallendarControl();

        return rootView;
    }

    private void setCallendarControl() {
        rootView.findViewById(R.id.dateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }

    private void fabSetOnClickListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spnExerciseName.getSelectedItem().toString().equals(null)
                        || edtSerije.getText().equals(null)
                        || edtPonavaljnje.getText().equals(null)
                        || edtKilaza.getText().equals(null)
                        || date == null)
                    Toast.makeText(getContext(), "Missing fields", Toast.LENGTH_SHORT).show();
                else {
                    borrowedListViewModel.addBorrow(new VezbeModel(
                            spnExerciseName.getSelectedItem().toString(),
                            edtSerije.getText().toString(),
                            edtPonavaljnje.getText().toString(),
                            edtKilaza.getText().toString(),
                            date
                    ));

                    lastIdVezbe();

                    setSnackBar();  //here "layout" is your parentView in a layout
                    clearEditTexts();

                }
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        date = calendar.getTime();
    }

    private void populateSpinner(){

        spinnerArray =  new ArrayList<String>();
        spinnerArray.add("aaa");


        exercisesViewModel = ViewModelProviders.of(this).get(ExercisesViewModel.class);

        exercisesViewModel.getItemAndPersonList().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> itemAndPeople) {
                int lenght = itemAndPeople.size();
                for (int i = 0; i < lenght; i++){
                    spinnerArray.add(itemAndPeople.get(i));
                }
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnExerciseName = rootView.findViewById(R.id.spnExerciseName);

        spnExerciseName.setAdapter(adapter);
    }

    private void initialisationComponents(){
        edtSerije = rootView.findViewById(R.id.edtSerije);
        edtPonavaljnje = rootView.findViewById(R.id.edtPonavaljnje);
        edtKilaza = rootView.findViewById(R.id.edtKilaza);

        fab = rootView.findViewById(R.id.fab);

        calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getContext(), InputFragment.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        populateSpinner();
    }

    private void clearEditTexts(){
        edtSerije.getText().clear();
        edtPonavaljnje.getText().clear();
        edtKilaza.getText().clear();
    }

    private void setSnackBar() {
        Snackbar snackbar = Snackbar
                .make(rootView.findViewById(R.id.coordinatorLayout), "Success insert record!", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteItemVezba();
                        Toast.makeText(getContext(), "Success delete last record", Toast.LENGTH_SHORT).show();
                    }
                });

        snackbar.setActionTextColor(Color.RED);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    private void lastIdVezbe(){
        borrowedListViewModel.getLastModel().observe(this, new Observer<VezbeModel>() {
            @Override
            public void onChanged(@Nullable VezbeModel lastVezba) {
                lastVezbeModel = lastVezba;
            }
        });
    }

    private void deleteItemVezba(){
        borrowedListViewModel.deleteItem(lastVezbeModel);
    }
}
