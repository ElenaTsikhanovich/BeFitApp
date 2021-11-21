package it.academy.by.befitapp.dto.journal;

import it.academy.by.befitapp.dto.ListDto;

public class JournalSearchDto extends ListDto {
    private Long day;

    public JournalSearchDto(){

    }

    public Long getDay() {
        return day;
    }

    public void setDay(Long day) {
        this.day = day;
    }
}

