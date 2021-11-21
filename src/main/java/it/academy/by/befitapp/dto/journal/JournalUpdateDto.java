package it.academy.by.befitapp.dto.journal;

import it.academy.by.befitapp.dto.NutrientDto;
import it.academy.by.befitapp.model.Journal;

public class JournalUpdateDto {
    private Journal journal;
    private Long updateTime;
    private NutrientDto nutrientDto;

    public JournalUpdateDto(){

    }

    public Journal getJournal() {
        return journal;
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public NutrientDto getNutrientDto() {
        return nutrientDto;
    }

    public void setNutrientDto(NutrientDto nutrientDto) {
        this.nutrientDto = nutrientDto;
    }
}
