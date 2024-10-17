package controllers;

import common.auth.User;
import common.connection.CommandMsg;
import common.data.*;
import common.exceptions.*;
import common.io.InputManager;
import common.utils.DateConverter;
import controllers.tools.ObservableResourceFactory;
import io.OutputterUI;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.App;

import java.time.LocalDate;
import java.util.Scanner;

import static common.utils.DateConverter.parseLocalDate;

public class AskWindowController {
    @FXML
    private Label nameLabel;
    @FXML
    private Label coordinatesXLabel;
    @FXML
    private Label coordinatesYLabel;
    @FXML
    private Label salaryLabel;

    @FXML
    private Label endDateLabel;
    @FXML
    private Label positionLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label organizationNameLabel;
    @FXML
    private Label organizationTypeLabel;
    @FXML
    private TextField nameField;
    @FXML
    private TextField coordinatesXField;
    @FXML
    private TextField coordinatesYField;
    @FXML
    private TextField salaryField;
    @FXML
    private TextField endDateField;
    @FXML
    private TextField organizationNameField;
    @FXML
    private ComboBox<Position> positionBox;
    @FXML
    private ComboBox<Status> statusBox;
    @FXML
    private ComboBox<OrganizationType> organizationTypeBox;

    @FXML
    private Button enterButton;

    private Stage askStage;
    private Worker resultWorker;
    private ObservableResourceFactory resourceFactory;
    private Worker worker;
    private App app;
    @FXML
    public void initialize() {
        askStage = new Stage();
        positionBox.setItems(FXCollections.observableArrayList(Position.values()));
        statusBox.setItems(FXCollections.observableArrayList(Status.values()));
        organizationTypeBox.setItems(FXCollections.observableArrayList(OrganizationType.values()));
    }

    public String readName() throws InvalidDataException {
        String s = nameField.getText();
        if (s==null||s.equals("")) {
            throw new InvalidDataException("[NameEmptyException]");
        }
        return s;
    }

    public String readFullName() {
        String s = organizationNameField.getText();
        if (s==null||s.equals("")) {
            return null;
        }
        return s;
    }

    public float readXCoord() throws InvalidDataException {
        float x;
        try {
            x = Float.parseFloat(coordinatesXField.getText());
        } catch (NumberFormatException e) {
            throw new InvalidDataException("[XCoordFormatException]");
        }
        if (Float.isInfinite(x) || Float.isNaN(x)) throw new InvalidDataException("[XCoordFormatException]");
        return x;
    }

    public Long readYCoord() throws InvalidDataException {
        Long y;
        try {
            y = Long.parseLong(coordinatesYField.getText());
        } catch (NumberFormatException e) {
            throw new InvalidDataException("[YCoordFormatException]");
        }
        if (y <= -123) throw new InvalidDataException("[YCoordLimitException]");
        return y;
    }

    public Coordinates readCoords() throws InvalidDataException {
        float x = readXCoord();
        Long y = readYCoord();
        Coordinates coord = new Coordinates(x, y);
        return coord;
    }

    public long readSalary() throws InvalidDataException {
        Long s;
        try {
            s = Long.parseLong(salaryField.getText());
        } catch (NumberFormatException e) {
            throw new InvalidDataException("[SalaryFormatException]");
        }

        if (s <= 0) throw new InvalidNumberException("[SalaryLimitException]");

        return s;
    }

    public LocalDate readEndDate() throws InvalidDataException {
        String buf = endDateField.getText();
        if (buf==null||buf.equals("")) {
            return null;
        } else {
            try {
                return parseLocalDate(buf);
            } catch (InvalidDateFormatException e){
                throw new InvalidDataException("[EndDateFormatException]");
            }
        }
    }

    public Position readPosition() {
        return  positionBox.getSelectionModel().getSelectedItem();
    }

    public Status readStatus() throws InvalidEnumException{
        Status status = statusBox.getSelectionModel().getSelectedItem();
        if(status==null) throw new InvalidEnumException("[StatusEmptyException]");
        return status;
    }

    public OrganizationType readOrganizationType() throws InvalidEnumException {
       OrganizationType type= organizationTypeBox.getSelectionModel().getSelectedItem();
       if(type==null) throw new InvalidEnumException("[OrganizationTypeEmptyException]");
       return type;
    }

    public Organization readOrganization() throws InvalidDataException {
        String fullName = readFullName();
        OrganizationType orgType = readOrganizationType();
        return new Organization(fullName, orgType);
    }

    public Worker readWorker() throws InvalidDataException {
        askStage.showAndWait();

        if(worker==null) throw new InvalidDataException("");
        return worker;

    }
    public void setWorker(Worker worker) {
        nameField.setText(worker.getName());
        coordinatesXField.setText(worker.getCoordinates().getX() + "");
        coordinatesYField.setText(worker.getCoordinates().getY() + "");
        salaryField.setText(worker.getSalary() + "");
        organizationNameField.setText(worker.getOrganization().getFullName()!=null?worker.getOrganization().getFullName():"");
        endDateField.setText(worker.getEndDate()!=null? DateConverter.dateToString(worker.getEndDate()):"");
        positionBox.setValue(worker.getPosition());
        statusBox.setValue(worker.getStatus());
        organizationTypeBox.setValue(worker.getOrganization().getType());
    }
    @FXML
    private void enterButtonOnAction() {
        try {

            String name = readName();
            Coordinates coords = readCoords();
            long salary = readSalary();
            LocalDate date = readEndDate();
            Position pos = readPosition();
            Status stat = readStatus();
            Organization org = readOrganization();
            worker = new DefaultWorker(name, coords, salary, date, pos, stat, org);

            askStage.close();
        } catch (InvalidDataException|IllegalArgumentException exception) {
            app.getOutputManager().error(exception.getMessage());
        }
    }
/*
    public String readPassword() throws InvalidDataException {
        String s = read();
        if (s.equals("")) throw new EmptyStringException();
        return s;
    }

    public String readLogin() throws InvalidDataException {
        String s = read();
        if (s.equals("")) throw new EmptyStringException();
        return s;
    }

    public User readUser() throws InvalidDataException {
        return new User(readPassword(), readLogin());
    }

    public CommandMsg readCommand() {
        String cmd = read();
        String arg = null;
        Worker worker = null;
        User user = null;
        if (cmd.contains(" ")) { //if command has argument
            String[] arr = cmd.split(" ", 2);
            cmd = arr[0];
            arg = arr[1];
        }
        if (cmd.equals("add") || cmd.equals("add_if_min") || cmd.equals("add_if_max") || cmd.equals("update")) {
            try {
                worker = readWorker();
            } catch (InvalidDataException ignored) {
            }
        } else if (cmd.equals("login") || cmd.equals("register")) {
            try {
                user = readUser();
            } catch (InvalidDataException ignored) {
            }
            return new CommandMsg(cmd, null, null, user);
        }
        return new CommandMsg(cmd, arg, worker);
    }*/

    public void setAskStage(Stage askStage) {
        this.askStage = askStage;
        this.askStage.setOnCloseRequest((e)->worker=null);
    }

    public void setApp(App app) {
        this.app = app;
    }

    /**
     * Init langs.
     *
     * @param resourceFactory Resource factory to set.
     */
    public void initLangs(ObservableResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        bindGuiLanguage();
    }

    public void bindGuiLanguage(){
        nameLabel.textProperty().bind(resourceFactory.getStringBinding("NameColumn"));
        coordinatesXLabel.textProperty().bind(resourceFactory.getStringBinding("CoordinatesXColumn"));
        coordinatesYLabel.textProperty().bind(resourceFactory.getStringBinding("CoordinatesYColumn"));
        endDateLabel.textProperty().bind(resourceFactory.getStringBinding("EndDateColumn"));
        positionLabel.textProperty().bind(resourceFactory.getStringBinding("PositionColumn"));
        statusLabel.textProperty().bind(resourceFactory.getStringBinding("StatusColumn"));
        organizationNameLabel.textProperty().bind(resourceFactory.getStringBinding("OrganizationNameColumn"));
        organizationTypeLabel.textProperty().bind(resourceFactory.getStringBinding("OrganizationTypeColumn"));
        salaryLabel.textProperty().bind(resourceFactory.getStringBinding("SalaryColumn"));
        enterButton.textProperty().bind(resourceFactory.getStringBinding("EnterButton"));
    }
}
