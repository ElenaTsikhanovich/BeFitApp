package it.academy.by.befitapp.dto;

public class JournalSearchDto extends ListDto{
    private Integer day;

    public JournalSearchDto(){

    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }
}
