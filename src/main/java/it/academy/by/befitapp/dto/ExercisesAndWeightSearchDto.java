package it.academy.by.befitapp.dto;

import java.time.LocalDateTime;

public class ExercisesAndWeightSearchDto extends ListDto{
    private LocalDateTime start;
    private LocalDateTime end;

    public ExercisesAndWeightSearchDto(){

    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
