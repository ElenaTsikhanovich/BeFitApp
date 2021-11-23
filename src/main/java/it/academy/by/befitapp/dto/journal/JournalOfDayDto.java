package it.academy.by.befitapp.dto.journal;

import it.academy.by.befitapp.dto.NutrientDto;
import it.academy.by.befitapp.model.Journal;

import java.util.List;

public class JournalOfDayDto {
    private List<Journal> journals;
    private NutrientDto nutrientDto;
    private Double dayNorm;
    private Double goal;


    public JournalOfDayDto(){
    }

    public List<Journal> getJournals() {
        return journals;
    }

    public void setJournals(List<Journal> journals) {
        this.journals = journals;
    }

    public NutrientDto getNutrientDto() {
        return nutrientDto;
    }

    public void setNutrientDto(NutrientDto nutrientDto) {
        this.nutrientDto = nutrientDto;
    }

    public Double getDayNorm() {
        return dayNorm;
    }

    public void setDayNorm(Double dayNorm) {
        this.dayNorm = dayNorm;
    }

    public Double getGoal() {
        return goal;
    }

    public void setGoal(Double goal) {
        this.goal = goal;
    }
}
