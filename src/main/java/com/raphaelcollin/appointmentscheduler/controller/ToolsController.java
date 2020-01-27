package com.raphaelcollin.appointmentscheduler.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.raphaelcollin.appointmentscheduler.db.DataSource;
import com.raphaelcollin.appointmentscheduler.db.model.Appointment;
import com.raphaelcollin.appointmentscheduler.db.model.Exportable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.raphaelcollin.appointmentscheduler.Main.*;

public class ToolsController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private Label titleLabel;
    @FXML
    private Label exportLabel;
    @FXML
    private GridPane exportContainerGridPane;
    @FXML
    private VBox formatVBox;
    private ToggleGroup formatToggleGroup;
    @FXML
    private JFXRadioButton csvButton;
    @FXML
    private JFXRadioButton xmlButton;
    @FXML
    private JFXRadioButton jsonButton;
    @FXML
    private JFXRadioButton xlsxButton;
    @FXML
    private VBox configVBox;
    @FXML
    private JFXButton locationButton;
    @FXML
    private JFXButton exportButton;
    @FXML
    private VBox entityVBox;

    private ToggleGroup entityToggleGroup;

    @FXML
    private JFXRadioButton appointmentButton;
    @FXML
    private JFXRadioButton patientButton;
    @FXML
    private JFXRadioButton doctorButton;

    private GridPane dateConfigurationGridPane;

    private File file;

    private static final String BUNDLE_KEY_TOOLS_START_FIELD = "start_date";
    private static final String BUNDLE_KEY_SELECT_DATE = "select_date";
    private static final String BUNDLE_KEY_TOOLS_END_FIELD = "end_date";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        double width = 1000;
        double height = 800;

        root.setMinSize(width, height);
        root.setPrefSize(width, height);
        root.setMaxSize(width, height);

        titleLabel.setFont(Font.font(32));
        AnchorPane.setTopAnchor(titleLabel, 25.0);

        exportLabel.setFont(Font.font(26));
        AnchorPane.setLeftAnchor(exportLabel, 50.0);
        AnchorPane.setTopAnchor(exportLabel, 80.0);

        exportContainerGridPane.getChildren().forEach(node -> {
            VBox box = (VBox) node;
            box.setPrefSize(100, 200);
            box.setSpacing(10);
            box.getChildren().forEach(button -> ((ButtonBase) button).setFont(Font.font(18)));
        });

        formatToggleGroup = new ToggleGroup();

        csvButton.setToggleGroup(formatToggleGroup);
        xmlButton.setToggleGroup(formatToggleGroup);
        jsonButton.setToggleGroup(formatToggleGroup);
        xlsxButton.setToggleGroup(formatToggleGroup);

        AnchorPane.setTopAnchor(exportContainerGridPane, 70.0);
        formatVBox.setPrefSize(100, 200);
        formatVBox.setAlignment(Pos.CENTER);
        formatVBox.setPadding(new Insets(0, 0, 0, 80));

        configVBox.setSpacing(40);


        formatVBox.getChildren().forEach(node -> ((JFXRadioButton) node).setPrefWidth(80));

        locationButton.setFont(Font.font(18));
        locationButton.getStyleClass().add(STYLE_CLASS_BLUE_BUTTON);
        locationButton.setTextFill(Color.WHITE);

        exportButton.setFont(Font.font(28));
        exportButton.getStyleClass().add(STYLE_CLASS_MAIN_VIEW_GREEN_BUTTON);
        exportButton.setTextFill(Color.WHITE);

        entityVBox.getChildren().forEach(node -> ((JFXRadioButton) node).setPrefWidth(170));
        entityVBox.setPadding(new Insets(0, 0, 0, 40));

        entityToggleGroup = new ToggleGroup();

        appointmentButton.setToggleGroup(entityToggleGroup);
        patientButton.setToggleGroup(entityToggleGroup);
        doctorButton.setToggleGroup(entityToggleGroup);

        dateConfigurationGridPane = new GridPane();
        dateConfigurationGridPane.setVgap(20);
        dateConfigurationGridPane.setHgap(15);
        dateConfigurationGridPane.setAlignment(Pos.CENTER);

        Label startDateLabel = new Label(resources.getString(BUNDLE_KEY_TOOLS_START_FIELD));
        startDateLabel.setFont(Font.font(18));

        JFXDatePicker startDateField = new JFXDatePicker();
        startDateField.setPromptText(resources.getString(BUNDLE_KEY_SELECT_DATE));
        startDateField.setStyle("-fx-font-size: 15px");
        startDateField.setDefaultColor(Color.valueOf("#085394"));

        Label endDateLabel = new Label(resources.getString(BUNDLE_KEY_TOOLS_END_FIELD));
        endDateLabel.setFont(Font.font(18));

        JFXDatePicker endDateField = new JFXDatePicker();
        endDateField.setPromptText(resources.getString(BUNDLE_KEY_SELECT_DATE));
        endDateField.setStyle("-fx-font-size: 15px");
        endDateField.setDefaultColor(Color.valueOf("#085394"));

        dateConfigurationGridPane.getChildren().setAll(startDateLabel, startDateField, endDateLabel, endDateField);

        GridPane.setConstraints(startDateLabel, 0, 0);
        GridPane.setConstraints(startDateField, 1, 0);
        GridPane.setConstraints(endDateLabel, 0, 1);
        GridPane.setConstraints(endDateField, 1, 1);

        formatToggleGroup.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            file = null;
            locationButton.setText(getResources().getString(BUNDLE_KEY_SELECT_LOCATION));
        }));

        entityToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(appointmentButton)) {
                configVBox.getChildren().add(0, dateConfigurationGridPane);
            } else {
                configVBox.getChildren().remove(dateConfigurationGridPane);
            }
        });

        entityToggleGroup.selectToggle(appointmentButton);
        formatToggleGroup.selectToggle(csvButton);
    }

    @FXML
    void handleExport() {

        if (file == null) {

            showAlert(Alert.AlertType.ERROR, root,
                    getResources().getString(BUNDLE_KEY_ERROR_ALERT_TITLE),
                    getResources().getString(BUNDLE_KEY_ERROR_LOCATION_HEADER),
                    getResources().getString(BUNDLE_KEY_ERROR_LOCATION_MESSAGE));

        } else {

            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    ObservableList<Exportable> data = FXCollections.observableArrayList();

                    JFXRadioButton selectedEntity = (JFXRadioButton) entityToggleGroup.getSelectedToggle();

                    if (selectedEntity.equals(patientButton)) {
                        data.addAll(DataSource.getInstance().getPatients());
                    } else if (selectedEntity.equals(doctorButton)) {
                        data.addAll(DataSource.getInstance().getDoctors());
                    } else {

                        LocalDate startDate = ((JFXDatePicker) dateConfigurationGridPane.getChildren().get(1)).getValue();
                        LocalDate endDate = ((JFXDatePicker) dateConfigurationGridPane.getChildren().get(3)).getValue();

                        for (Appointment appointment : DataSource.getInstance().getAppointments()) {

                            if ((startDate == null || appointment.getDate().toLocalDate().compareTo(startDate) >= 0) && (endDate == null ||
                                    appointment.getDate().toLocalDate().compareTo(endDate) < 0)) {
                                data.add(appointment);
                            }

                        }
                    }

                    if (!data.isEmpty()) {
                        if (formatToggleGroup.getSelectedToggle().equals(xlsxButton)) {

                            XSSFWorkbook workbook = new XSSFWorkbook();

                            workbook.createSheet(selectedEntity.getText());
                            Sheet sheet = workbook.getSheet(selectedEntity.getText());


                            CellStyle headerStyle = workbook.createCellStyle();
                            headerStyle.setAlignment(HorizontalAlignment.CENTER);
                            headerStyle.setFillForegroundColor(
                                    HSSFColor.HSSFColorPredefined.ROYAL_BLUE.getIndex());

                            XSSFFont font = workbook.createFont();
                            font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
                            font.setFontHeightInPoints((short) 14);

                            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                            headerStyle.setFont(font);

                            Row headerRow = sheet.createRow(0);
                            String [] headers = data.get(0).getHeaderLineCSV().split(",");

                            for (int j = 0; j < headers.length; j++) {
                                Cell cell = headerRow.createCell(j);
                                cell.setCellValue(headers[j]);
                                cell.setCellStyle(headerStyle);
                            }

                            for (int i = 1; i < data.size() + 1; i++) {

                                Row row = sheet.createRow(i);

                                String [] fields = data.get(i - 1).convertToCSV().split(",", -1);

                                XSSFCellStyle cellStyle = workbook.createCellStyle();
                                cellStyle.setBorderBottom(BorderStyle.THIN);
                                cellStyle.setBottomBorderColor(HSSFColor.HSSFColorPredefined.ROYAL_BLUE.getIndex());
                                cellStyle.setBorderRight(BorderStyle.THIN);
                                cellStyle.setRightBorderColor(HSSFColor.HSSFColorPredefined.ROYAL_BLUE.getIndex());
                                cellStyle.setBorderLeft(BorderStyle.THIN);
                                cellStyle.setLeftBorderColor(HSSFColor.HSSFColorPredefined.ROYAL_BLUE.getIndex());
                                cellStyle.setBorderTop(BorderStyle.THIN);
                                cellStyle.setTopBorderColor(HSSFColor.HSSFColorPredefined.ROYAL_BLUE.getIndex());

                                XSSFFont cellFont = workbook.createFont();
                                cellFont.setFontHeightInPoints((short) 12);
                                cellStyle.setFont(cellFont);

                                for (int j = 0; j < fields.length; j++) {

                                    String field = fields[j].trim();

                                    Cell cell = row.createCell(j);

                                    if (field.matches("^[\\d]+([.,][\\d]+)?")) {

                                        cell.setCellValue(Double.parseDouble(field.replaceAll(",", "")));

                                    } else {
                                        cell.setCellValue(field);
                                    }

                                    cell.setCellStyle(cellStyle);
                                }
                            }

                            for (int i = 0; i < headers.length; i++) {
                                sheet.autoSizeColumn(i);

                            }

                            try (BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file))){

                                workbook.write(output);

                            } catch (IOException e) {
                                e.printStackTrace();
                                return false;
                            }

                        } else {
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {


                                if (formatToggleGroup.getSelectedToggle().equals(csvButton)) {

                                    writer.write(data.get(0).getHeaderLineCSV());
                                    writer.newLine();

                                    for (Exportable exportable : data) {
                                        writer.write(exportable.convertToCSV());
                                        writer.newLine();
                                    }
                                } else if (formatToggleGroup.getSelectedToggle().equals(xmlButton)) {

                                    writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                                    writer.newLine();
                                    writer.write("<" + selectedEntity.getText() + "s" + ">");
                                    writer.newLine();

                                    for (Exportable datum : data) {
                                        writer.write(datum.convertToXML());
                                        writer.newLine();
                                    }

                                    writer.write("</" + selectedEntity.getText() + "s" + ">");

                                } else {
                                    writer.write("[");
                                    writer.newLine();

                                    for (int i = 0; i < data.size(); i++) {
                                        writer.write(data.get(i).convertToJSON());
                                        if (i < data.size() - 1) {
                                            writer.write(",");
                                        }
                                        writer.newLine();
                                    }

                                    writer.write("]");

                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                return false;
                            }
                        }
                    }

                    return true;

                }

            };

            task.setOnSucceeded(event -> {

                root.setCursor(Cursor.DEFAULT);

                if (task.getValue()) {

                    showAlert(Alert.AlertType.INFORMATION, root,
                            ((Stage) root.getScene().getWindow()).getTitle(),
                             getResources().getString(BUNDLE_KEY_SUCCESS_DATA_EXPORT_HEADER),
                            getResources().getString(BUNDLE_KEY_SUCCESS_DATA_EXPORT_MESSAGE));

                } else {
                    showAlert(Alert.AlertType.ERROR, root,
                            ((Stage) root.getScene().getWindow()).getTitle(),
                            getResources().getString(BUNDLE_KEY_ERROR_DATA_EXPORT_HEADER),
                            getResources().getString(BUNDLE_KEY_ERROR_DATA_EXPORT_CONTENT));
                }
            });

            root.setCursor(Cursor.WAIT);
            new Thread(task).start();

        }
    }

    @FXML
    void handleSelectLocation() {

        JFXRadioButton selectedButton = (JFXRadioButton) formatToggleGroup.getSelectedToggle();
        String format = selectedButton.getText();

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(format + " " +
                getResources().getString(BUNDLE_KEY_FILES), "*." + format.toLowerCase());
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setTitle(((Stage) root.getScene().getWindow()).getTitle());
        File file = fileChooser.showSaveDialog(root.getScene().getWindow());

        if (file != null) {

            this.file = file;
            locationButton.setText(file.getName());

        }
    }
}
