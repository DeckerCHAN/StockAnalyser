package com.deckerchan.analyser.stock.gui;

import com.deckerchan.analyser.stock.StaticConfig;
import com.deckerchan.analyser.stock.core.Engine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;

import static java.lang.System.out;

public class MainStage extends Stage {

    private Engine engine;
    private MenuBar menuBar;
    private BorderPane root;

    private ListView<String> listView;

    private LineChart<Number, Number> lineChart;

    private Menu fileMenu;
    private Menu viewMenu;


    public MainStage() {
        super();

        try {
            this.engine = Engine.getEngine();
        } catch (Exception e) {
            e.printStackTrace();
        }


        this.root = new BorderPane();


        this.menuBar = new MenuBar();


        this.listView = new ListView<>();
        this.listView.getFocusModel().focus(-1);
        this.listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.listView.setOnMouseClicked(this::listViewClicked);
        this.renderSymbols();

        this.fileMenu = new Menu("File");
        MenuItem load = new MenuItem("Load");
        load.setOnAction(this::loadDataFromFile);
        MenuItem exit = new MenuItem("Exit");

        this.fileMenu.getItems().addAll(load, exit);


        this.viewMenu = new Menu("View");
        MenuItem overView = new MenuItem("OverView");
        this.viewMenu.getItems().addAll(overView);

        this.menuBar.getMenus().addAll(this.fileMenu, this.viewMenu);

        final NumberAxis xAxis = new NumberAxis();
        xAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.format("H%d", object.longValue());
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        });
        final NumberAxis yAxis = new NumberAxis();
        this.lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("Stock Monitoring, 2010");

        XYChart.Series series = new XYChart.Series<Number, Number>();
        series.setName("My portfolio");


        series.getData().add(new XYChart.Data<>(1, 23));
        series.getData().add(new XYChart.Data<>(2, 14));
        series.getData().add(new XYChart.Data<>(3, 15));
        series.getData().add(new XYChart.Data<>(4, 24));
        series.getData().add(new XYChart.Data<>(5, 34));
        series.getData().add(new XYChart.Data<>(6, 36));
        series.getData().add(new XYChart.Data<>(7, 22));
        series.getData().add(new XYChart.Data<>(8, 45));
        series.getData().add(new XYChart.Data<>(9, 43));
        series.getData().add(new XYChart.Data<>(10, 17));
        series.getData().add(new XYChart.Data<>(11, 29));
        series.getData().add(new XYChart.Data<>(12, 25));

        this.lineChart.getData().add(series);

        for (XYChart.Series<Number, Number> s : lineChart.getData()) {
            for (XYChart.Data<Number, Number> d : s.getData()) {

                //1. Add tooltip
                Tooltip.install(d.getNode(), new Tooltip(String.format("%s \n%s: %s", d.getXValue(), s.getName(), d.getYValue())));

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
            }
        }


        this.root.setTop(this.menuBar);
        this.root.setLeft(this.listView);
        this.root.setCenter(this.lineChart);

        //Set stage resizeable
        this.setResizable(true);

        this.setTitle("Stock Analysis");
        this.setScene(new Scene(this.root));


    }

    private void listViewClicked(MouseEvent mouseEvent) {
        try{
            //TODO: get selected items.
        }catch (Exception ex){
            out.println(ex.getMessage());
        }

    }

    private void loadDataFromFile(ActionEvent actionEvent) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            File file = fileChooser.showOpenDialog(this);

            if (file != null) {
                try {
                    this.engine.loadFromFile(file);
                    this.renderSymbols();
                } catch (Exception e) {
                    out.println(e.getMessage());
                }
                ;
            }
        } catch (Exception ex) {
            out.println(String.format("Unable to load data. Due to %s", ex.getMessage()));

        }
    }


    public void renderSymbols() {

        try {
            ObservableList<String> listImtes = FXCollections.observableArrayList(this.engine.getAllStockSymbol());
            this.listView.setItems(listImtes);

        } catch (Exception ex) {
            out.println(String.format("Unable to render symbols. Due to %s", ex.getMessage()));
        }

    }

}
