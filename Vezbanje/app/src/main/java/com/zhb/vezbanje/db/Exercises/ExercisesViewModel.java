package com.zhb.vezbanje.db.Exercises;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.zhb.vezbanje.db.AppDatabase;

import java.util.List;


public class ExercisesViewModel extends AndroidViewModel {

    private LiveData<List<String>> itemAndPersonList;
    private LiveData<List<ExercisesModel>> itemAndPersonList2;

    private AppDatabase appDatabase;

    public ExercisesViewModel(Application application) {

        super(application);

        appDatabase = AppDatabase.getDatabase(this.getApplication());
    }


    public LiveData<List<String>> getItemAndPersonList() {
        itemAndPersonList = appDatabase.exercisesModelDaoModel().getAllVezbeItems();
        return itemAndPersonList;
    }

    public LiveData<List<ExercisesModel>> getItemAndPersonList2() {
        itemAndPersonList2 = appDatabase.exercisesModelDaoModel().getAllVezbeItems2();
        return itemAndPersonList2;
    }

    // za promenu u  bazi koristi se AsyncTask
    public void deleteItem(ExercisesModel borrowModel) {
        new deleteAsyncTask(appDatabase).execute(borrowModel);
    }

    public void addBorrow(final ExercisesModel borrowModel) {
        new addAsyncTask(appDatabase).execute(borrowModel);
    }

    private static class deleteAsyncTask extends AsyncTask<ExercisesModel, Void, Void> {

        private AppDatabase db;

        deleteAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final ExercisesModel... params) {
            db.exercisesModelDaoModel().deleteVezbe(params[0]);
            return null;
        }
    }

    private static class addAsyncTask extends AsyncTask<ExercisesModel, Void, Void> {

        private AppDatabase db;

        addAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final ExercisesModel... params) {
            db.exercisesModelDaoModel().addVezbe(params[0]);
            return null;
        }

    }
}
