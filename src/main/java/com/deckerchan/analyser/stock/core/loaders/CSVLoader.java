package com.deckerchan.analyser.stock.core.loaders;

import com.deckerchan.analyser.stock.core.entities.Record;
import com.deckerchan.analyser.stock.utils.ConsumerWappers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Stream;

public class CSVLoader implements Loader {
    private File csvFile;

    public CSVLoader(File csvFile) {
        this.csvFile = csvFile;
    }

    @Override
    public Collection<Record> load() throws IOException {
        ArrayList<Record> records  = new ArrayList<>();


        try (Stream<String> stream = Files.lines(Paths.get(this.csvFile.getAbsolutePath()))) {
            stream.skip(1).forEach(ConsumerWappers.throwingConsumerWrapper(

                    x -> {

                        String[] fields = x.split(",");

                        Record record = new Record();

                        record.setId(UUID.randomUUID());

                        record.setSymbol(fields[0]);

                        record.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(fields[1]));
                        record.setOpen(Double.valueOf(fields[2]));
                        record.setHigh(Double.valueOf(fields[3]));
                        record.setLow(Double.valueOf(fields[4]));
                        record.setClose(Double.valueOf(fields[5]));
                        record.setVolume(Integer.valueOf(fields[6]));
                        record.setAdjClose(Double.valueOf(fields[7]));

                        records.add(record);

                    }
            ));

            return records;
        }
    }
}
