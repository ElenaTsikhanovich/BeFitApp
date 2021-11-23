package it.academy.by.befitapp;

import it.academy.by.befitapp.dao.api.IProductDao;
import it.academy.by.befitapp.utils.ConvertTime;
import it.academy.by.befitapp.utils.ProductBaseExelParser;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TestClass {
    public static void main(String[] args) {
        ProductBaseExelParser.parse("Calorie_table.xls");

    }
}
