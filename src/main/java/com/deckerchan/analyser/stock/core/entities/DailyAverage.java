package com.deckerchan.analyser.stock.core.entities;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "daily")
public class DailyAverage {
    @DatabaseField(id = true)
    private Date date;


    @DatabaseField
    private String open;

    @DatabaseField
    private double close;

    @DatabaseField
    private double high;

    @DatabaseField
    private double low;

    @DatabaseField
    private double adjClose;

    @DatabaseField
    private int volume;

}
