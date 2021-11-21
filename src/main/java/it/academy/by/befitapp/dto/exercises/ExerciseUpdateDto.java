package it.academy.by.befitapp.dto.exercises;

import it.academy.by.befitapp.model.Exercise;

public class ExerciseUpdateDto {
    private Exercise exercise;
    private Long updateTime;

    public ExerciseUpdateDto(){

    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
