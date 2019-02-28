package com.zhb.vezbanje.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.zhb.vezbanje.R;
import com.zhb.vezbanje.adapter.RecyclerViewAdapterExercises;
import com.zhb.vezbanje.db.Exercises.ExercisesModel;
import com.zhb.vezbanje.db.Exercises.ExercisesViewModel;
import com.zhb.vezbanje.db.Running.RunningModel;
import com.zhb.vezbanje.db.Running.RunningViewModel;
import com.zhb.vezbanje.db.Vezbe.VezbeModel;
import com.zhb.vezbanje.db.Vezbe.VezbeViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.graphics.Color;
import android.widget.TextView;

public class InputFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private Date date, currentDate;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;

    private EditText edtSerije, edtPonavaljnje, edtKilaza, edtDistance, edtDuration;

    private CheckBox chbWeight;

    private LinearLayout lnlExercises, lnlRunning;

    private TextView txtDate, txtDifferentDate;

    private Spinner spnExerciseName;

    private VezbeViewModel borrowedListViewModel;
    private ExercisesViewModel exercisesViewModel;
    private RunningViewModel runningViewModel;

    private FloatingActionButton fab;

    private View rootView;

    private int currentDay;

    private static VezbeModel lastVezbeModel;
    private static RunningModel lastRunningModel;

    List<String> spinnerArray;

    public InputFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_input, container, false);

        initialisationComponents();

        borrowedListViewModel = ViewModelProviders.of(this).get(VezbeViewModel.class);
        runningViewModel = ViewModelProviders.of(this).get(RunningViewModel.class);

        fabSetOnClickListener();

        setCallendarControl();

        setComboBoxListener();

        setCurrentDate();

        return rootView;
    }

    private void setCurrentDate() {

        Calendar c = Calendar.getInstance();
        currentDate = new Date();
        c.setTime(currentDate);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        date = calendar.getTime();

        currentDay = dayOfMonth;

        setTextDate(dayOfMonth, month, year);
    }

    private void setTextDate(int dayOfMonth, int month, int year) {

        String dateDifferentText = "(today)";
        int color = ContextCompat.getColor(getContext(), R.color.dateCurrent);
        txtDate.setText(dayOfMonth + "." + (++month) + "." + year + ".");

        long numberOfDays = getUnitBetweenDates(currentDate, date, TimeUnit.DAYS);

        if (numberOfDays == 0) {
            if (currentDay < dayOfMonth) {
                numberOfDays = 1;
            } else if (currentDay > dayOfMonth) {
                numberOfDays = -1;
            }
        }

        if (numberOfDays > 0) {
            dateDifferentText = "(future!)";
            color = ContextCompat.getColor(getContext(), R.color.dateFuture);
        } else if (numberOfDays == -1) {
            dateDifferentText = "(yesterday)";
            color = ContextCompat.getColor(getContext(), R.color.dateBefore);
        } else if (numberOfDays == -2) {
            dateDifferentText = "(day before yesterday)";
            color = ContextCompat.getColor(getContext(), R.color.dateBefore);
        } else if (numberOfDays < -2) {
            dateDifferentText = "(few days ago)";
            color = ContextCompat.getColor(getContext(), R.color.dateBefore);
        }

        txtDifferentDate.setTextColor(color);
        txtDifferentDate.setText(dateDifferentText);

    }

    public static void hideKeyboardFrom(Context context, View view) {

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    private static long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {

        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);

    }

    private void setComboBoxListener() {

        spnExerciseName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0 || position == 1 || position == 2) {
                    lnlExercises.setVisibility(View.GONE);
                    lnlRunning.setVisibility(View.VISIBLE);
                } else {
                    lnlExercises.setVisibility(View.VISIBLE);
                    lnlRunning.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

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
                hideKeyboardFrom(getContext(), getView());
                if (lnlExercises.getVisibility() == View.VISIBLE) {
                    if (spnExerciseName.getSelectedItem().toString().equals(null)
                            || edtSerije.getText().toString().matches("")
                            || edtPonavaljnje.getText().toString().matches("")
                            || edtKilaza.getText().toString().matches("")
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
                        setSnackBar(false);  //here "layout" is your parentView in a layout
                        clearEditTexts();
                    }
                } else {
                    if (edtDistance.getText().toString().matches("") || edtDuration.getText().toString().matches("")) {
                        Toast.makeText(getContext(), "Missing fields", Toast.LENGTH_SHORT).show();
                    } else {
                        // ovo mora da se promeni
                        runningViewModel.addBorrow(new RunningModel(
                                spnExerciseName.getSelectedItem().toString(),
                                edtDistance.getText().toString(),
                                edtDuration.getText().toString(),
                                "333",
                                "92",
                                chbWeight.isChecked(),
                                date
                        ));
                        lastIdVezbeRunning();
                        setSnackBar(true);  //here "layout" is your parentView in a layout
                        clearEditTexts();
                    }
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

        setTextDate(dayOfMonth, month, year);
    }

    private void populateSpinner() {

        spinnerArray = new ArrayList<String>();
        spinnerArray.add("Trčanje");
        spinnerArray.add("Biciklo");
        spinnerArray.add("Plivanje");

        exercisesViewModel = ViewModelProviders.of(this).get(ExercisesViewModel.class);

        exercisesViewModel.getItemAndPersonList().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> itemAndPeople) {
                int lenght = itemAndPeople.size();
                for (int i = 0; i < lenght; i++) {
                    spinnerArray.add(itemAndPeople.get(i));
                }
            }
        });

        spnExerciseName = rootView.findViewById(R.id.spnExerciseName);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getContext(), R.layout.spinner_item, spinnerArray
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spnExerciseName.setAdapter(spinnerArrayAdapter);

    }

    private void initialisationComponents() {
        edtSerije = rootView.findViewById(R.id.edtSerije);
        edtPonavaljnje = rootView.findViewById(R.id.edtPonavaljnje);
        edtKilaza = rootView.findViewById(R.id.edtKilaza);
        edtDistance = rootView.findViewById(R.id.edtDistance);
        edtDuration = rootView.findViewById(R.id.edtDuration);

        chbWeight = rootView.findViewById(R.id.chbWeight);

        lnlExercises = rootView.findViewById(R.id.lnlExercises);
        lnlRunning = rootView.findViewById(R.id.lnlRunning);

        txtDifferentDate = rootView.findViewById(R.id.txtDifferentDate);
        txtDate = rootView.findViewById(R.id.txtDate);

        fab = rootView.findViewById(R.id.fab);

        calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getContext(), InputFragment.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        populateSpinner();

//        edtDistance.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                Toast.makeText(getContext(), ""+edtDistance.getText().toString(), Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });

        setupFloatingLabelError();
    }


    private void setupFloatingLabelError() {
        final TextInputLayout floatingDisatnce = rootView.findViewById(R.id.tilDisatnce);
        floatingDisatnce.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {

                if (text.length() > 0 && text.length() <= 3) {

                    floatingDisatnce.setError(edtDistance.getText().toString() + "m");

                } else if (text.length() > 0) {

                    int lenght = text.length();
                    floatingDisatnce.setError(edtDistance.getText().toString().substring(0, lenght - 3) + "," + edtDistance.getText().toString().substring(lenght - 3, lenght) + "km");

                } else {
                    floatingDisatnce.setErrorEnabled(false);
                }
            }

            // ...
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        final TextInputLayout floatingDuration = rootView.findViewById(R.id.tilDuration);
        floatingDuration.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {

                int duration = 0;
                try {
                    duration = Integer.parseInt(edtDuration.getText().toString());
                } catch (Exception e)
                {}

                int minutes = duration / 60;
                int secundes = duration % 60;

                if (duration > 0) {
                    if (minutes == 0) {

                        floatingDuration.setError(secundes + "s");

                    } else {
                        floatingDuration.setError(minutes + ":" + secundes);

                    }
                }
                else{
                    floatingDuration.setErrorEnabled(false);
                }
            }

            // ...
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void clearEditTexts() {

        edtSerije.getText().clear();
        edtPonavaljnje.getText().clear();
        edtKilaza.getText().clear();
        edtDuration.getText().clear();
        edtDistance.getText().clear();

    }

    private void setSnackBar(final boolean isRunning) {
        Snackbar snackbar = Snackbar
                .make(rootView.findViewById(R.id.coordinatorLayout), "Success insert record!", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isRunning) {
                            deleteLastItemExercise();
                        } else {
                            deleteLastItemRunning();
                        }

                        Toast.makeText(getContext(), "Success delete last record", Toast.LENGTH_SHORT).show();
                    }
                });

        snackbar.setActionTextColor(Color.RED);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    private void lastIdVezbe() {
        borrowedListViewModel.getLastModel().observe(this, new Observer<VezbeModel>() {
            @Override
            public void onChanged(@Nullable VezbeModel lastVezba) {
                lastVezbeModel = lastVezba;
            }
        });
    }

    private void lastIdVezbeRunning() {
        runningViewModel.getLastModel().observe(this, new Observer<RunningModel>() {
            @Override
            public void onChanged(@Nullable RunningModel lastVezba) {
                lastRunningModel = lastVezba;
            }
        });
    }

    private void deleteLastItemExercise() {
        borrowedListViewModel.deleteItem(lastVezbeModel);
    }

    private void deleteLastItemRunning() {
        runningViewModel.deleteItem(lastRunningModel);
    }

}
