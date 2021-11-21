package it.academy.by.befitapp.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ConvertTime {

    public static LocalDateTime fromMilliToDate(Long milliSeconds){
        LocalDateTime result = Instant.ofEpochMilli(milliSeconds).atZone(ZoneId.systemDefault()).toLocalDateTime();
        return result;
    }

    public static Long fromDateToMilli(LocalDateTime localDateTime){
        Long result = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return result;
    }

}