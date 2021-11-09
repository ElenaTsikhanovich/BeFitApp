package it.academy.by.befitapp.dto;

public class JournalSearchDto extends ListDto{
    private Long idFood;
    private Integer day;

    public JournalSearchDto(){

    }

    public Long getIdFood() {
        return idFood;
    }

    public void setIdFood(Long idFood) {
        this.idFood = idFood;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }
}
