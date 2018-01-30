package com.deckerchan.analyser.stock.core;

public class Engine {
    private static Engine instance;

    private Engine() {
    }

    public static Engine getEngine() {
        if (Engine.instance == null) {
            Engine.instance = new Engine();
        }
        return Engine.instance;
    }


}
