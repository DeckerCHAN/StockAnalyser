package com.deckerchan.analyser.stock.gui.dialog;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class DetailDialog extends Dialog {

    private final Label openPriceLabel;
    private final Label closePriceLabel;
    private final Label lowPriceLabel;
    private final Label highPriceLabel;
    private final Label adjPriceLabel;
    private final Label volumeLabel;

    public Label getOpenPriceLabel() {
        return openPriceLabel;
    }

    public Label getClosePriceLabel() {
        return closePriceLabel;
    }

    public Label getLowPriceLabel() {
        return lowPriceLabel;
    }

    public Label getHighPriceLabel() {
        return highPriceLabel;
    }

    public Label getAdjPriceLabel() {
        return adjPriceLabel;
    }

    public Label getVolumeLabel() {
        return volumeLabel;
    }

    protected DetailDialog() {

        this.openPriceLabel = new Label("Field");
        this.closePriceLabel = new Label("Field");
        this.lowPriceLabel = new Label("Field");
        this.highPriceLabel = new Label("Field");
        this.adjPriceLabel = new Label("Field");
        this.volumeLabel = new Label("Field");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(openPriceLabel, 1, 1);
        grid.add(lowPriceLabel, 2, 1);
        grid.add(closePriceLabel, 1, 2);
        grid.add(highPriceLabel, 2, 2);
        grid.add(adjPriceLabel, 3, 1);
        grid.add(volumeLabel, 3, 2);

        this.getDialogPane().setContent(grid);
        this.getDialogPane().getButtonTypes().add(new ButtonType("Got it!", ButtonBar.ButtonData.CANCEL_CLOSE));

    }
}
