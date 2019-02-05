package com.zhb.vezbanje.db.Exercises;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class ExercisesModel {

    @PrimaryKey(autoGenerate = true)
    public int id;
    private String exercisesName;

    public ExercisesModel(){}

    public ExercisesModel(String exercisesName) {
        this.exercisesName = exercisesName;
    }

    public int getId() {
        return id;
    }

    public void setExercisesName(String exercisesName) {
        this.exercisesName = exercisesName;
    }

    public String getExercisesName() {
        return exercisesName;
    }
}
