package com.deckerchan.analyser.stock.gui.dialog;

import com.deckerchan.analyser.stock.core.entities.Record;
import com.deckerchan.analyser.stock.gui.DateStringConverter;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class DetailDialog extends Dialog {
    public DetailDialog(Record record) {

        Label openPrice = new Label(String.format("Open Price: %.2f", record.getOpen()));
        Label closePrice = new Label(String.format("Close Price: %.2f", record.getClose()));
        Label lowPrice = new Label(String.format("Low Price: %.2f", record.getLow()));
        Label highPrice = new Label(String.format("High Price: %.2f", record.getHigh()));
        Label adjPrice = new Label(String.format("Adjust close Price: %.2f", record.getAdjClose()));
        Label volume = new Label(String.format("Volume: %d", record.getVolume()));

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
        this.setTitle(String.format("Stock Details for %s on %s", record.getSymbol(), DateStringConverter.DEFAULT_FORMAT.format(record.getDate())));

    }
}
