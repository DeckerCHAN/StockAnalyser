package com.deckerchan.analyser.stock;

import com.deckerchan.analyser.stock.core.Engine;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import static java.lang.System.out;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EngineTest {

    private Engine engine;

    @Before
    public void setUp() throws Exception {
        this.engine = Engine.getEngine();
        this.engine.loadFromFile(Paths.get(System.getProperty("user.dir"), "data", "stock.csv").toFile());
    }


    @Test
    public void testA() throws Exception {

        Assert.assertNotNull(engine.getRecordBySymbolAndDate("AE", new Date(Long.valueOf("1490878800000"))));
        Assert.assertEquals(engine.getRecordBySymbolAndDate("AE", new Date(Long.valueOf("1490878800000"))).getOpen(), 38.29, 0.01);
        Assert.assertEquals(engine.getAllStockSymbol().size(), 19);
    }

    @After
    public void tearDown() throws Exception {
        Files.delete(Paths.get(System.getProperty("user.dir"), "data.db"));
    }
}
