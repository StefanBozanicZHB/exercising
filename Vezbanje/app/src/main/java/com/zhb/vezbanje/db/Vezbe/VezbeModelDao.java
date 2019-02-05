package com.zhb.vezbanje.db.Vezbe;

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
public interface VezbeModelDao {

    // kada se vracaju vise odgovora treba obgriliti sa LiveData
    @Query("select * from VezbeModel")
    LiveData<List<VezbeModel>> getAllVezbeItems();

    @Query("select * from VezbeModel where id = :id")
    VezbeModel getItembyId(String id);

    @Query("SELECT * FROM VezbeModel WHERE id = (SELECT MAX(id) FROM VezbeModel)")
    LiveData<VezbeModel> getLastId();

    @Insert(onConflict = REPLACE) // import static android.arch.persistence.room.OnConflictStrategy.REPLACE;
    void addVezbe(VezbeModel borrowModel);

    @Delete
    void deleteVezbe(VezbeModel borrowModel);
}
