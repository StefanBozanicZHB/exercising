package com.zhb.vezbanje.db.Vezbe;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.zhb.vezbanje.db.AppDatabase;

import java.util.List;


public class VezbeViewModel extends AndroidViewModel {

    private LiveData<List<VezbeModel>> itemAndPersonList;

    private AppDatabase appDatabase;

    private LiveData<VezbeModel> lastVezbeModel;

    public VezbeViewModel(Application application) {

        super(application);

        appDatabase = AppDatabase.getDatabase(this.getApplication());
    }


    public LiveData<List<VezbeModel>> getItemAndPersonList() {
        itemAndPersonList = appDatabase.itemAndPersonModel().getAllVezbeItems();
        return itemAndPersonList;
    }

    public LiveData<VezbeModel> getLastModel() {

        lastVezbeModel = appDatabase.itemAndPersonModel().getLastId();

        return lastVezbeModel;
    }

    // za promenu u  bazi koristi se AsyncTask
    public void deleteItem(VezbeModel borrowModel) {
        new deleteAsyncTask(appDatabase).execute(borrowModel);
    }

    public void addBorrow(final VezbeModel borrowModel) {
        new addAsyncTask(appDatabase).execute(borrowModel);
    }

    private static class deleteAsyncTask extends AsyncTask<VezbeModel, Void, Void> {

        private AppDatabase db;

        deleteAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final VezbeModel... params) {
            db.itemAndPersonModel().deleteVezbe(params[0]);
            return null;
        }
    }

    private static class addAsyncTask extends AsyncTask<VezbeModel, Void, Void> {

        private AppDatabase db;

        addAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final VezbeModel... params) {
            db.itemAndPersonModel().addVezbe(params[0]);
            return null;
        }

    }
}
