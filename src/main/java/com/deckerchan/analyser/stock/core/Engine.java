package com.deckerchan.analyser.stock.core;

import com.deckerchan.analyser.stock.core.entities.Record;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.out;

public class Engine {
    private static Engine instance;
    private final Dao<Record, UUID> recordUUIDDao;

    private Engine() throws Exception {
        String databaseUrl = "jdbc:sqlite:data.db";
        ConnectionSource connectionSource =
                new JdbcConnectionSource(databaseUrl);

        this.recordUUIDDao =
                DaoManager.createDao(connectionSource, Record.class);
        TableUtils.createTableIfNotExists(connectionSource, Record.class);

    }

    public static Engine getEngine() throws Exception {
        if (Engine.instance == null) {
            Engine.instance = new Engine();
        }
        return Engine.instance;
    }

    public void loadFromFile(File file) throws Exception {
        try (Stream<String> stream = Files.lines(Paths.get(file.getAbsolutePath()))) {
            stream.skip(1).forEach(x -> {

                try {
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

                    this.recordUUIDDao.create(record);
                } catch (Exception ex) {
                    out.println(String.format("Unable to add this data. Due to %s", ex.getMessage()));
                }

            });
        }
    }

    public List<String> getAllStockSymbol() throws Exception {
        return this.recordUUIDDao.queryForAll().stream().map(x -> x.getSymbol()).distinct().collect(Collectors.toList());
    }

    public List<Record> getRecordsByCompany(String symbol) throws Exception {
        return this.recordUUIDDao.queryForEq("symbol", symbol);
    }


}
