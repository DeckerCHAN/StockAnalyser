package com.deckerchan.analyser.stock.core.loaders;

import com.deckerchan.analyser.stock.core.entities.Record;

import java.io.IOException;
import java.util.Collection;

public interface Loader {
    Collection<Record> load() throws IOException;
}
