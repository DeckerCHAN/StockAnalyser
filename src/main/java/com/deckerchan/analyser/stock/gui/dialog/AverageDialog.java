package com.deckerchan.analyser.stock.gui.dialog;

import com.deckerchan.analyser.stock.core.entities.DailyAverage;
import com.deckerchan.analyser.stock.core.entities.Record;
import com.deckerchan.analyser.stock.gui.DateStringConverter;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class AverageDialog extends Dialog {
    public AverageDialog(DailyAverage record) {

        Label openPrice = new Label(String.format("Average Open Price: %.2f", record.getOpen()));
        Label closePrice = new Label(String.format("Average Close Price: %.2f", record.getClose()));
        Label lowPrice = new Label(String.format("Average Low Price: %.2f", record.getLow()));
        Label highPrice = new Label(String.format("Average High Price: %.2f", record.getHigh()));
        Label adjPrice = new Label(String.format("Average Adjust close Price: %.2f", record.getAdjClose()));
        Label volume = new Label(String.format("Average Volume: %.2f", record.getVolume()));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(openPrice, 1, 1);
        grid.add(lowPrice, 2, 1);
        grid.add(closePrice, 1, 2);
        grid.add(highPrice, 2, 2);
        grid.add(adjPrice, 3, 1);
        grid.add(volume, 3, 2);

        this.getDialogPane().setContent(grid);
        this.getDialogPane().getButtonTypes().add(new ButtonType("Got it!", ButtonBar.ButtonData.CANCEL_CLOSE));
        this.setTitle(String.format("Average details on %s",  DateStringConverter.DEFAULT_FORMAT.format(record.getDate())));

    }
}
