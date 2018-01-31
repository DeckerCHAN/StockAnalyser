package com.deckerchan.analyser.stock.gui;

import com.deckerchan.analyser.stock.StaticConfig;
import com.deckerchan.analyser.stock.core.Engine;
import com.deckerchan.analyser.stock.core.entities.Record;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class MainStage extends Stage {

    private Engine engine;
    private MenuBar menuBar;
    private BorderPane root;

    private ListView<String> listView;

    private LineChart<Number, Number> lineChart;

    private Menu fileMenu;
    private Menu viewMenu;

    private ProgressBar busyIndrcator;

    public MainStage() {
        super();

        try {
            this.engine = Engine.getEngine();
        } catch (Exception e) {
            e.printStackTrace();
        }


        this.root = new BorderPane();


        this.listView = new ListView<>();
        this.listView.getFocusModel().focus(-1);
        this.listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.listView.setOnMouseClicked(this::listViewClicked);
        this.renderSymbols();


        //Menu Bar
        this.fileMenu = new Menu("File");
        MenuItem load = new MenuItem("Load");
        load.setOnAction(this::loadDataFromFile);
        MenuItem exit = new MenuItem("Exit");

        this.fileMenu.getItems().addAll(load, exit);


        this.viewMenu = new Menu("View");
        MenuItem overView = new MenuItem("OverView");
        this.viewMenu.getItems().addAll(overView);

        this.menuBar = new MenuBar();
        this.menuBar.getMenus().addAll(this.fileMenu, this.viewMenu);


        // Line chart
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setTickLabelFormatter(new DateStringConverter());
        xAxis.setAutoRanging(true);
        xAxis.setForceZeroInRange(false);

        final NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(true);
        yAxis.setForceZeroInRange(false);

        this.lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("Stock HAAAAAAA, 2010");


        //Busy Indicator
        this.busyIndrcator = new ProgressBar();
        this.busyIndrcator.setProgress(-1F);
        this.busyIndrcator.setVisible(false);


        HBox indicatorHbox = new HBox();
        indicatorHbox.setAlignment(Pos.BOTTOM_RIGHT);
        indicatorHbox.getChildren().addAll(this.busyIndrcator);


        //Border Panel
        this.root.setTop(this.menuBar);
        this.root.setLeft(this.listView);
        this.root.setCenter(this.lineChart);
        this.root.setBottom(indicatorHbox);

        //Set stage resizeable
        this.setResizable(true);

        this.setMinWidth(750);
        this.setMinHeight(480);

        this.setTitle("Stock Analysis");
        this.setScene(new Scene(this.root));


    }

    private void listViewClicked(MouseEvent mouseEvent) {
        try {
            String symbol = this.listView.getSelectionModel().getSelectedItem();
            this.renderBySymbol(symbol);

        } catch (Exception ex) {
            out.println(ex.getMessage());
        }

    }

    private void loadDataFromFile(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(this);
        this.showBusy();

        new Thread(() -> {
            try {


                if (file != null) {
                    try {
                        this.engine.loadFromFile(file);
                        this.renderSymbols();
                    } catch (Exception e) {
                        out.println(e.getMessage());
                    }
                }
            } catch (Exception ex) {
                out.println(String.format("Unable to load data. Due to %s", ex.getMessage()));

            } finally {
                this.hideBusy();
            }
        }).start();


    }

    private void showBusy() {
        this.busyIndrcator.setVisible(true);
    }

    private void hideBusy() {
        this.busyIndrcator.setVisible(false);
    }


    public void renderSymbols() {

        try {
            ObservableList<String> listItems = FXCollections.observableArrayList(this.engine.getAllStockSymbol());
            this.listView.setItems(listItems);

        } catch (Exception ex) {
            out.println(String.format("Unable to render symbols. Due to %s", ex.getMessage()));
        }

    }

    private void addTooltip() {
        for (XYChart.Series<Number, Number> s : lineChart.getData()) {
            for (XYChart.Data<Number, Number> d : s.getData()) {
                //TODO:Add line transparency to avoid full overlap
                //Add tooltip
                Tooltip.install(d.getNode(), new Tooltip(String.format("%s \n%s: %s", DateStringConverter.DEFAULT.toString(d.getXValue()), s.getName(), d.getYValue())));

                //Adding class on hover
                d.getNode().setOnMouseEntered(event -> {
                    d.getNode().setScaleX(StaticConfig.CHART_POINT_HOVER_SCALE);
                    d.getNode().setScaleY(StaticConfig.CHART_POINT_HOVER_SCALE);
                    d.getNode().setCursor(Cursor.HAND);
                });

                //Removing class on exit
                d.getNode().setOnMouseExited(event -> {
                    d.getNode().setScaleX(1);
                    d.getNode().setScaleY(1);
                });
                //When clicked
                d.getNode().setOnMouseClicked(event -> {
                    //TODO:Use warpper to get rid of try catch
                    try {
                        out.println(this.getWidth());
                        out.println(this.getHeight());

                        Record record = this.engine.getRecordBySymbolAndDate(this.listView.getSelectionModel().getSelectedItem(), new Date(d.getXValue().longValue()));
                        new DetailDialog(record).showAndWait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    public void renderBySymbol(String symbol) {

        this.showBusy();

        new Thread(() -> {
            try {


                List<Record> records = this.engine.getRecordsByCompany(symbol);

                List<Record> sorted = records.stream().sorted(Comparator.comparing(Record::getDate)).collect(Collectors.toList());


                List<XYChart.Data<Long, Double>> openList = sorted.stream()
                        .map(x -> new XYChart.Data<>(x.getDate().getTime(), x.getOpen()))
                        .collect(Collectors.toList());

                XYChart.Series openSeries = new XYChart.Series<Number, Number>();
                openSeries.setName("open");
                openSeries.getData().addAll(openList);

                List<XYChart.Data<Long, Double>> closeList = sorted.stream()
                        .map(x -> new XYChart.Data<>(x.getDate().getTime(), x.getClose()))
                        .collect(Collectors.toList());

                XYChart.Series closeSeries = new XYChart.Series<Number, Number>();
                closeSeries.setName("close");
                closeSeries.getData().addAll(closeList);

                List<XYChart.Data<Long, Double>> lowList = sorted.stream()
                        .map(x -> new XYChart.Data<>(x.getDate().getTime(), x.getLow()))
                        .collect(Collectors.toList());

                XYChart.Series lowSeries = new XYChart.Series<Number, Number>();
                lowSeries.setName("low");
                lowSeries.getData().addAll(lowList);


                List<XYChart.Data<Long, Double>> highList = sorted.stream()
                        .map(x -> new XYChart.Data<>(x.getDate().getTime(), x.getHigh()))
                        .collect(Collectors.toList());

                XYChart.Series highSeries = new XYChart.Series<Number, Number>();
                highSeries.setName("high");
                highSeries.getData().addAll(highList);

                List<XYChart.Data<Long, Double>> adjList = sorted.stream()
                        .map(x -> new XYChart.Data<>(x.getDate().getTime(), x.getAdjClose()))
                        .collect(Collectors.toList());

                XYChart.Series adjSeries = new XYChart.Series<Number, Number>();
                adjSeries.setName("close_adj");
                adjSeries.getData().addAll(adjList);

                Platform.runLater(() -> {
                    this.lineChart.getData().clear();
                    this.lineChart.getData().addAll(openSeries, closeSeries, highSeries, lowSeries, adjSeries);
                    this.lineChart.setTitle(String.format("Stock price of %s", symbol));
                    this.addTooltip();
                });


            } catch (Exception ex) {
                ex.printStackTrace();
                out.println(String.format("Unable to render symbol. Due to %s", ex.getMessage()));

            } finally {
                this.hideBusy();
            }
        }).start();
    }

}
