package com.zhb.vezbanje.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.zhb.vezbanje.db.Exercises.ExercisesModel;
import com.zhb.vezbanje.db.Exercises.ExercisesModelDao;
import com.zhb.vezbanje.db.Running.RunningModel;
import com.zhb.vezbanje.db.Running.RunningModelDao;
import com.zhb.vezbanje.db.Vezbe.VezbeModel;
import com.zhb.vezbanje.db.Vezbe.VezbeModelDao;

// jedna AppDatabase za sve entities
@Database(entities = {VezbeModel.class, ExercisesModel.class, RunningModel.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),    // contex
                    AppDatabase.class,                  // klasa gde se nalazi baza podataka
                    "borrow_db")                 // naziv baze podataka
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract VezbeModelDao itemAndPersonModel();

    public abstract ExercisesModelDao exercisesModelDaoModel();

    public abstract RunningModelDao runningModelDaoModel();

}
