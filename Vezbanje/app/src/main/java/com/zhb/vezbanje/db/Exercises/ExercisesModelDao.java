package com.zhb.vezbanje.db.Exercises;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ExercisesModelDao {

    @Query("select exercisesName from ExercisesModel")
    LiveData<List<String>> getAllVezbeItems();

    @Query("select * from ExercisesModel")
    LiveData<List<ExercisesModel>> getAllVezbeItems2();

    @Insert(onConflict = REPLACE)
    void addVezbe(ExercisesModel borrowModel);

    @Delete
    void deleteVezbe(ExercisesModel borrowModel);
}
