package com.deckerchan.analyser.stock;

import com.deckerchan.analyser.stock.gui.MainStage;

import javafx.stage.Stage;

public class Application extends javafx.application.Application {


    private MainStage main;

    public Application() {
        super();
    }

    @Override
    public void start(Stage s) throws Exception {
        main = new MainStage();
        main.show();
    }
}