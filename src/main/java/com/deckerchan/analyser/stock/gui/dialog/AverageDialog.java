package com.deckerchan.analyser.stock.gui.dialog;

import com.deckerchan.analyser.stock.core.entities.DailyAverage;
import com.deckerchan.analyser.stock.gui.utils.DateStringConverter;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class AverageDialog extends DetailDialog {
    public AverageDialog(DailyAverage average) {

        super();
        this.getOpenPriceLabel().setText(String.format("Average Open Price: %.2f", average.getOpen()));
        this.getClosePriceLabel().setText(String.format("Average Close Price: %.2f", average.getClose()));
        this.getLowPriceLabel().setText(String.format("Average Low Price: %.2f", average.getLow()));
        this.getHighPriceLabel().setText(String.format("Average High Price: %.2f", average.getHigh()));
        this.getAdjPriceLabel().setText(String.format("Average Adjust close Price: %.2f", average.getAdjClose()));
        this.getVolumeLabel().setText(String.format("Average Volume: %.2f", average.getVolume()));
    this.setTitle(String.format("Average details on %s",  DateStringConverter.DEFAULT_FORMAT.format(average.getDate())));

    }
}
