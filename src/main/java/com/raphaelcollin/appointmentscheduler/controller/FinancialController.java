package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.raphaelcollin.appointmentscheduler.db.DataSource;
import com.raphaelcollin.appointmentscheduler.db.model.Appointment;
import com.raphaelcollin.appointmentscheduler.db.model.Doctor;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.COMPLETED;
import static com.raphaelcollin.appointmentscheduler.Main.getResources;
import static com.raphaelcollin.appointmentscheduler.db.DataSource.*;

public class FinancialController implements Initializable, PropertyChangeListener {


    private static final String JANUARY = "january";
    private static final String FEBRUARY = "february";
    private static final String MARCH = "march";
    private static final String APRIL = "april";
    private static final String MAY = "may";
    private static final String JUNE = "june";
    private static final String JULY = "july";
    private static final String AUGUST = "august";
    private static final String SEPTEMBER = "september";
    private static final String OCTOBER = "october";
    private static final String NOVEMBER = "november";
    private static final String DECEMBER = "december";
    @FXML
    private AnchorPane root;
    @FXML
    private Label titleLabel;
    @FXML
    private GridPane gridPaneFields;
    @FXML
    private JFXComboBox<Integer> yearField;
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
    private LineChart<Integer, Double> lineChart;
    @FXML
    private PieChart pieChart;

    private static final String REGEX_DAY = "^[0-31]+";

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

        lineChart.getXAxis().setAutoRanging(true);
        lineChart.getXAxis().setTickMarkVisible(false);
        lineChart.getXAxis().setTickLabelsVisible(false);

        lineChart.getYAxis().setAutoRanging(true);
        lineChart.getYAxis().setTickMarkVisible(false);
        lineChart.getYAxis().setTickLabelFont(Font.font(15));

        lineChart.setLegendVisible(false);
        lineChart.setCreateSymbols(false);

        AnchorPane.setTopAnchor(pieChart, 20.0);
        AnchorPane.setLeftAnchor(pieChart, 500.0);

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
                ((JFXTextField) node).setFont(Font.font(22));
            }

        }

        totalEarnedField.setStyle("-fx-text-fill: #006700");
        appointmentsField.setStyle("-fx-text-fill: #1671b0");

        AnchorPane.setLeftAnchor(resultsVBox, 0.0);
        AnchorPane.setRightAnchor(resultsVBox, 530.0);
        AnchorPane.setTopAnchor(resultsVBox, 350.0);

        monthField.setItems(FXCollections.observableList(Arrays.asList(
                resources.getString(JANUARY),
                resources.getString(FEBRUARY),
                resources.getString(MARCH),
                resources.getString(APRIL),
                resources.getString(MAY),
                resources.getString(JUNE),
                resources.getString(JULY),
                resources.getString(AUGUST),
                resources.getString(SEPTEMBER),
                resources.getString(OCTOBER),
                resources.getString(NOVEMBER),
                resources.getString(DECEMBER)
        )));

        monthField.getSelectionModel().selectFirst();

        appointmentsField.setEditable(false);
        totalEarnedField.setEditable(false);

        yearField.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setupData());
        monthField.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setupData());
        startDayField.textProperty().addListener((observable, oldValue, newValue) -> {

                setupData();

        });
        endDayField.textProperty().addListener((observable, oldValue, newValue) -> {

                setupData();

        });

        DataSource.getInstance().addObserver(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(INITIAL_DATA_LOADED)) {

            Platform.runLater(() -> {
                int mostRecentYear = DataSource.getInstance().getFirstYear();
                int oldestYear = DataSource.getInstance().getLastYear();

                if (mostRecentYear > Integer.MIN_VALUE && oldestYear < Integer.MAX_VALUE) {
                    ObservableList<Integer> years = FXCollections.observableArrayList();

                    for (int i = mostRecentYear; i <= oldestYear; i++) {
                        years.add(i);
                    }

                    yearField.setItems(years);

                }

                yearField.getSelectionModel().selectFirst();
                startDayField.setText("0");
                endDayField.setText("31");
            });

        }
        if (evt.getPropertyName().equals(APPOINTMENTS_CHANGE) || evt.getPropertyName().equals(DOCTORS_CHANGE)) {
            Platform.runLater(this::setupData);

        }
    }

    private void setupData() {

        String startDay = startDayField.getText().trim();
        String endDay = endDayField.getText().trim();

        if (startDay.matches(REGEX_DAY) && endDay.matches(REGEX_DAY)) {
            Map<Doctor, Integer> doctorsAppointments = new HashMap<>();
            for (Doctor doctor: DataSource.getInstance().getDoctors()) {
                doctorsAppointments.put(doctor, 0);
            }

            int appointmentsCount = 0;
            double totalEarnings = 0;

            Map<Integer, Double> daysEarnings = new HashMap<>();

            for (Appointment appointment : DataSource.getInstance().getAppointments()) {

                if (appointment.getStatus().equals(COMPLETED)) {
                    int appointmentYear = appointment.getDate().getYear();
                    int appointmentMonth = appointment.getDate().getMonthValue();
                    int appointmentDay = appointment.getDate().getDayOfMonth();

                    if (appointmentYear == yearField.getSelectionModel().getSelectedItem() &&
                            appointmentMonth == monthField.getSelectionModel().getSelectedIndex() + 1 &&
                            appointmentDay >= Integer.parseInt(startDay) && appointmentDay <= Integer.parseInt(endDay)) {

                        double price = appointment.getPrice();

                        if (daysEarnings.containsKey(appointmentDay)) {
                            price += daysEarnings.get(appointmentDay);
                        }

                        daysEarnings.put(appointmentDay, price);

                        doctorsAppointments.put(appointment.getDoctor(), doctorsAppointments.get(appointment.getDoctor()) + 1);
                        appointmentsCount++;
                        totalEarnings += appointment.getPrice();

                    }
                }
            }

            XYChart.Series<Integer, Double> series = new XYChart.Series<>();

            daysEarnings.forEach((key, value) -> series.getData().add(new XYChart.Data<>(key, value)));

            if (lineChart.getData().isEmpty()) {
                lineChart.getData().add(series);
            } else {
                lineChart.getData().set(0, series);
            }

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            final int totalAppointments = appointmentsCount;

            if (totalAppointments > 0) {
                doctorsAppointments.forEach((key, value) -> {
                    pieChartData.add(new PieChart.Data(key.getName(), Math.abs(100 * value / totalAppointments)));
                });

                pieChart.setData(pieChartData);
            }

            appointmentsField.setText(totalAppointments + "");
            totalEarnedField.setText(getResources().getString("currency_symbol") + totalEarnings);

        }

    }

}
