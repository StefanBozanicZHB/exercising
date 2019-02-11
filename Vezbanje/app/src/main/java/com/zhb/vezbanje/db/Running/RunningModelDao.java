package com.zhb.vezbanje.db.Running;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import com.zhb.vezbanje.db.DateConverter;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

// za generisanje upita
// prvi je tip povratne informacije, pa naziv
@Dao
@TypeConverters(DateConverter.class)
public interface RunningModelDao {

    // kada se vracaju vise odgovora treba obgriliti sa LiveData
    @Query("select * from RunningModel")
    LiveData<List<RunningModel>> getAllVezbeItems();

    @Query("select * from RunningModel where id = :id")
    RunningModel getItembyId(String id);

    @Query("SELECT * FROM RunningModel WHERE id = (SELECT MAX(id) FROM RunningModel)")
    LiveData<RunningModel> getLastId();

    @Insert(onConflict = REPLACE) // import static android.arch.persistence.room.OnConflictStrategy.REPLACE;
    void addVezbe(RunningModel borrowModel);

    @Delete
    void deleteVezbe(RunningModel borrowModel);
}
