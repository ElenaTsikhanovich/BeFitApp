package it.academy.by.befitapp.dto.exercises;

import it.academy.by.befitapp.dto.ListDto;

import java.time.LocalDateTime;

public class ExercisesAndWeightSearchDto extends ListDto {
    private Long start;
    private Long end;

    public ExercisesAndWeightSearchDto() {

    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }
}
