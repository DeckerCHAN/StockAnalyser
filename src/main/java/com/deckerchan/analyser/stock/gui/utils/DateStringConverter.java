package com.deckerchan.analyser.stock.gui.utils;

import javafx.util.StringConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateStringConverter extends StringConverter<Number> {
    public static final DateStringConverter DEFAULT = new DateStringConverter();
    public static final SimpleDateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public String toString(Number object) {
        return DEFAULT_FORMAT.format(new Date(object.longValue()));

    }

    @Override
    public Number fromString(String string) {
        return null;
    }
}
