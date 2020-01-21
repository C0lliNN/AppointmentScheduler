package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class FinancialController implements Initializable {


    @FXML
    private AnchorPane root;
    @FXML
    private Label titleLabel;
    @FXML
    private GridPane gridPaneFields;
    @FXML
    private JFXComboBox<String> yearField;
    @FXML
    private JFXComboBox<String> monthField;
    @FXML
    private JFXTextField startDayField;
    @FXML
    private JFXTextField endDayField;
    @FXML
    private JFXTextField appointmentsField;
    @FXML
    private JFXTextField totalEarnedField;
    @FXML
    private VBox resultsVBox;
    @FXML
    private LineChart<Integer, Number> lineChart;
    @FXML
    private PieChart pieChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        double width = 1000;
        double height = 800;

        root.setMinSize(width, height);
        root.setPrefSize(width, height);
        root.setMaxSize(width, height);

        titleLabel.setFont(Font.font(32));
        AnchorPane.setLeftAnchor(titleLabel, 0.0);
        AnchorPane.setRightAnchor(titleLabel, 500.0);
        AnchorPane.setTopAnchor(titleLabel, 25.0);

        AnchorPane.setTopAnchor(lineChart, 500.0);
        lineChart.setPrefSize(990, 280);


        XYChart.Series<Integer, Number> series = new XYChart.Series<>();
        series.setName("Actual Result");
        series.getData().add(new XYChart.Data<>(1, 53213.9));
        series.getData().add(new XYChart.Data<>(2, 43213.3));
        series.getData().add(new XYChart.Data<>(3, 55713.1));
        series.getData().add(new XYChart.Data<>(4, 48713.9));
        series.getData().add(new XYChart.Data<>(5, 53213.9));
        series.getData().add(new XYChart.Data<>(6, 15213.9));
        series.getData().add(new XYChart.Data<>(7, 23213.9));
        series.getData().add(new XYChart.Data<>(8, 35213.9));
        series.getData().add(new XYChart.Data<>(9, 65213.9));
        series.getData().add(new XYChart.Data<>(10, 42213.9));
        series.getData().add(new XYChart.Data<>(11, 2213.9));
        series.getData().add(new XYChart.Data<>(12, 53213.9));
        series.getData().add(new XYChart.Data<>(13, 33213.9));
        series.getData().add(new XYChart.Data<>(14, 43213.3));
        series.getData().add(new XYChart.Data<>(15, 15713.1));
        series.getData().add(new XYChart.Data<>(16, 48713.9));
        series.getData().add(new XYChart.Data<>(17, 53213.9));
        series.getData().add(new XYChart.Data<>(18, 43213.3));
        series.getData().add(new XYChart.Data<>(19, 55713.1));
        series.getData().add(new XYChart.Data<>(20, 48713.9));


        lineChart.getXAxis().setAutoRanging(true);
        lineChart.getXAxis().setTickMarkVisible(false);
        lineChart.getXAxis().setTickLabelsVisible(false);

        lineChart.getYAxis().setAutoRanging(true);
        lineChart.getYAxis().setTickMarkVisible(false);
        lineChart.getYAxis().setTickLabelFont(Font.font(15));

        lineChart.getData().add(series);
        lineChart.setLegendVisible(false);
        lineChart.setCreateSymbols(false);

        AnchorPane.setTopAnchor(pieChart, 20.0);
        AnchorPane.setLeftAnchor(pieChart, 500.0);

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Grapefruit", 13),
                        new PieChart.Data("Oranges", 25),
                        new PieChart.Data("Plums", 10),
                        new PieChart.Data("Pears", 22),
                        new PieChart.Data("Apples", 30));
        pieChart.setData(pieChartData);

        gridPaneFields.setPadding(new Insets(0, 0, 0, 40));
        gridPaneFields.setHgap(10);
        gridPaneFields.setVgap(25);

        AnchorPane.setLeftAnchor(gridPaneFields, 0.0);
        AnchorPane.setRightAnchor(gridPaneFields, 500.0);
        AnchorPane.setTopAnchor(gridPaneFields, 100.0);

        for (Node node : gridPaneFields.getChildren()) {
            if (node instanceof Label) {
                ((Label) node).setFont(Font.font(22));
            }
            if (node instanceof JFXComboBox) {
                ((JFXComboBox) node).setPrefSize(170, 25);
                ((JFXComboBox) node).setStyle("-fx-font-size: 18px");
            }
            if (node instanceof JFXTextField) {
                ((JFXTextField) node).setPrefSize(170, 25);
                ((JFXTextField) node).setFont(Font.font(18));
            }
        }

        resultsVBox.setSpacing(0);
        resultsVBox.setPadding(new Insets(0,0,0,40));

        for (int j = 0; j < resultsVBox.getChildren().size(); j++) {

            Node node = resultsVBox.getChildren().get(j);

            if (node instanceof Label) {
                ((Label) resultsVBox.getChildren().get(j)).setFont(Font.font(22));
            } else {
                ((JFXTextField) node).setPrefSize(200, 25);
                ((JFXTextField) node).setFont(Font.font(18));
            }

        }

        AnchorPane.setLeftAnchor(resultsVBox, 0.0);
        AnchorPane.setRightAnchor(resultsVBox, 530.0);
        AnchorPane.setTopAnchor(resultsVBox, 350.0);

        appointmentsField.setEditable(false);
        totalEarnedField.setEditable(false);

        yearField.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setupData());
        monthField.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setupData());
        startDayField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue) {
                setupData();
            }
        });
        endDayField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue) {
                setupData();
            }
        });
    }


    private void setupData() {

    }

}
