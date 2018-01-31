package com.deckerchan.analyser.stock.gui.dialog;

import com.deckerchan.analyser.stock.core.entities.Record;
import com.deckerchan.analyser.stock.gui.utils.DateStringConverter;

public class PriceDialog extends DetailDialog {
    public PriceDialog(Record record) {
        super();
        this.getOpenPriceLabel().setText(String.format("Open Price: %.2f", record.getOpen()));
        this.getClosePriceLabel().setText(String.format("Close Price: %.2f", record.getClose()));
        this.getLowPriceLabel().setText(String.format("Low Price: %.2f", record.getLow()));
        this.getHighPriceLabel().setText(String.format("High Price: %.2f", record.getHigh()));
        this.getAdjPriceLabel().setText(String.format("Adjust close Price: %.2f", record.getAdjClose()));
        this.getVolumeLabel().setText(String.format("Volume: %d", record.getVolume()));

        this.setTitle(String.format("Stock Details for %s on %s", record.getSymbol(), DateStringConverter.DEFAULT_FORMAT.format(record.getDate())));

    }
}
