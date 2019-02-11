package com.zhb.vezbanje.db.Running;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.zhb.vezbanje.db.AppDatabase;
import com.zhb.vezbanje.db.Vezbe.VezbeModel;

import java.util.List;


public class RunningViewModel extends AndroidViewModel {

    private LiveData<List<RunningModel>> itemAndPersonList;

    private AppDatabase appDatabase;

    private LiveData<RunningModel> lastVezbeModel;

    public RunningViewModel(Application application) {

        super(application);

        appDatabase = AppDatabase.getDatabase(this.getApplication());
    }


    public LiveData<List<RunningModel>> getItemAndPersonList() {
        itemAndPersonList = appDatabase.runningModelDaoModel().getAllVezbeItems();
        return itemAndPersonList;
    }

    public LiveData<RunningModel> getLastModel() {

        lastVezbeModel = appDatabase.runningModelDaoModel().getLastId();

        return lastVezbeModel;
    }

    // za promenu u  bazi koristi se AsyncTask
    public void deleteItem(RunningModel borrowModel) {
        new deleteAsyncTask(appDatabase).execute(borrowModel);
    }

    public void addBorrow(final RunningModel borrowModel) {
        new addAsyncTask(appDatabase).execute(borrowModel);
    }

    private static class deleteAsyncTask extends AsyncTask<RunningModel, Void, Void> {

        private AppDatabase db;

        deleteAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final RunningModel... params) {
            db.runningModelDaoModel().deleteVezbe(params[0]);
            return null;
        }
    }

    private static class addAsyncTask extends AsyncTask<RunningModel, Void, Void> {

        private AppDatabase db;

        addAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final RunningModel... params) {
            db.runningModelDaoModel().addVezbe(params[0]);
            return null;
        }

    }
}
