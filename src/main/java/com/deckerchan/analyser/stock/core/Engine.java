package com.deckerchan.analyser.stock.core;

import com.deckerchan.analyser.stock.StaticConfig;
import com.deckerchan.analyser.stock.core.entities.DailyAverage;
import com.deckerchan.analyser.stock.core.entities.Record;
import com.deckerchan.analyser.stock.core.loaders.CSVLoader;
import com.deckerchan.analyser.stock.utils.ConsumerWappers;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Engine {
    private static Engine instance;
    private final Dao<Record, UUID> recordUUIDDao;
    private final Dao<DailyAverage, Date> averageDateDao;

    private Engine() throws Exception {

        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "ERROR");

        String databaseUrl = StaticConfig.JDBC_CONNECT_STRING;
        ConnectionSource connectionSource =
                new JdbcConnectionSource(databaseUrl);

        this.recordUUIDDao =
                DaoManager.createDao(connectionSource, Record.class);
        TableUtils.createTableIfNotExists(connectionSource, Record.class);

        this.averageDateDao = DaoManager.createDao(connectionSource, DailyAverage.class);
        TableUtils.createTableIfNotExists(connectionSource, DailyAverage.class);


    }

    public static Engine getEngine() throws Exception {
        if (Engine.instance == null) {
            Engine.instance = new Engine();
        }
        return Engine.instance;
    }

    public void loadFromFile(File file) throws Exception {
        Collection<Record> data = new CSVLoader(file).load();
        data.forEach(ConsumerWappers.throwingConsumerWrapper(this.recordUUIDDao::create));
        data.stream().map(Record::getDate).distinct().forEach(ConsumerWappers.throwingConsumerWrapper(x -> {


            //Delete old one
            DeleteBuilder<DailyAverage, Date> db = this.averageDateDao.deleteBuilder();
            db.where().eq("date", x);
            db.delete();

            //New average object
            DailyAverage average = new DailyAverage();
            average.setDate(x);

            //Set Open price
            OptionalDouble averageOpen = this.recordUUIDDao.queryBuilder().where().eq("date", x).query().stream().mapToDouble(Record::getOpen).average();
            if (averageOpen.isPresent()) {
                average.setOpen(averageOpen.getAsDouble());
            } else {
                average.setOpen(0D);
            }

            //Set close price
            OptionalDouble averageClose = this.recordUUIDDao.queryBuilder().where().eq("date", x).query().stream().mapToDouble(Record::getClose).average();
            if (averageClose.isPresent()) {
                average.setClose(averageClose.getAsDouble());
            } else {
                average.setClose(0D);
            }

            //Set high price
            OptionalDouble averageHigh = this.recordUUIDDao.queryBuilder().where().eq("date", x).query().stream().mapToDouble(Record::getHigh).average();
            if (averageHigh.isPresent()) {
                average.setHigh(averageHigh.getAsDouble());
            } else {
                average.setHigh(0D);
            }

            //Set low price
            OptionalDouble averageLow = this.recordUUIDDao.queryBuilder().where().eq("date", x).query().stream().mapToDouble(Record::getLow).average();
            if (averageLow.isPresent()) {
                average.setLow(averageLow.getAsDouble());
            } else {
                average.setLow(0D);
            }

            //Set low price
            OptionalDouble averageAdj = this.recordUUIDDao.queryBuilder().where().eq("date", x).query().stream().mapToDouble(Record::getAdjClose).average();
            if (averageAdj.isPresent()) {
                average.setAdjClose(averageAdj.getAsDouble());
            } else {
                average.setAdjClose(0D);
            }

            //Set volume
            OptionalDouble averageVolume = this.recordUUIDDao.queryBuilder().where().eq("date", x).query().stream().mapToInt(Record::getVolume).average();
            if (averageVolume.isPresent()) {
                average.setVolume(averageVolume.getAsDouble());
            } else {
                average.setVolume(0D);
            }

            this.averageDateDao.create(average);
        }));
    }

    public List<String> getAllStockSymbol() throws Exception {
        return this.recordUUIDDao.queryBuilder().selectColumns("symbol").distinct().query().stream().map(Record::getSymbol).collect(Collectors.toList());
    }

    public List<Record> getRecordsByCompany(String symbol) throws Exception {
        return this.recordUUIDDao.queryForEq("symbol", symbol);
    }

    public List<DailyAverage> getOverallDailyAverage() throws Exception {
        return this.averageDateDao.queryForAll();
    }

    public DailyAverage getAverageForSpecificDay(Date date) throws Exception {
        return this.averageDateDao.queryForId(date);
    }

    public Record getRecordBySymbolAndDate(String symbol, Date date) throws Exception {
        return this.recordUUIDDao.queryBuilder().where().eq("symbol", symbol).and().eq("date", date).queryForFirst();

    }


}
