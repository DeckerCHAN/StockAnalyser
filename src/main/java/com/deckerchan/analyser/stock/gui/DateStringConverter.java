package com.deckerchan.analyser.stock.gui;

import javafx.util.StringConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateStringConverter extends StringConverter<Number> {
    public static DateStringConverter DEFAULT = new DateStringConverter();

    @Override
    public String toString(Number object) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(object.longValue()), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    }

    @Override
    public Number fromString(String string) {
        return null;
    }
}
