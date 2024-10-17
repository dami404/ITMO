package controllers;


import common.connection.CollectionOperation;
import common.connection.CommandMsg;
import common.connection.Request;
import common.connection.Response;
import common.data.*;
import common.exceptions.*;
import common.utils.DateConverter;
import controllers.tools.MapUtils;
import controllers.tools.TableFilter;
import controllers.tools.ZoomOperator;
import javafx.animation.ScaleTransition;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;
import main.App;
import client.Client;
import controllers.tools.ObservableResourceFactory;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * Main window controller.
 */
public class MainWindowController {

    private final long RANDOM_SEED = 1821L;
    private final Duration ANIMATION_DURATION = Duration.millis(800);
    private final double MAX_SIZE = 250;

    @FXML
    private TableView<Worker> workerTable;
    @FXML
    private TableColumn<Worker, Integer> idColumn;
    @FXML
    private TableColumn<Worker, String> ownerColumn;
    @FXML
    private TableColumn<Worker, Date> creationDateColumn;
    @FXML
    private TableColumn<Worker, LocalDate> endDateColumn;
    @FXML
    private TableColumn<Worker, String> nameColumn;
    @FXML
    private TableColumn<Worker, Long> salaryColumn;
    @FXML
    private TableColumn<Worker, Float> coordinatesXColumn;
    @FXML
    private TableColumn<Worker, Long> coordinatesYColumn;
    @FXML
    private TableColumn<Worker, Position> positionColumn;
    @FXML
    private TableColumn<Worker, Status> statusColumn;
    @FXML
    private TableColumn<Worker, String> organizationNameColumn;
    @FXML
    private TableColumn<Worker, OrganizationType> organizationTypeColumn;
    @FXML
    private AnchorPane canvasPane;
    @FXML
    private Tab tableTab;
    @FXML
    private Tab canvasTab;
    @FXML
    private Button infoButton;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button executeScriptButton;
    @FXML
    private Button addIfMinButton;
    @FXML
    private Button addIfMaxButton;
    @FXML
    private Button filterStartsWithNameButton;
    @FXML
    private Button groupByEndDateButton;
    @FXML
    private Button removeFirstButton;

    @FXML
    private Button printUniqueSalariesButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button helpButton;
    @FXML
    private Tooltip infoButtonTooltip;
    @FXML
    private Tooltip addButtonTooltip;
    @FXML
    private Tooltip updateButtonTooltip;
    @FXML
    private Tooltip removeButtonTooltip;
    @FXML
    private Tooltip clearButtonTooltip;
    @FXML
    private Tooltip executeScriptButtonTooltip;
    @FXML
    private Tooltip addIfMinButtonTooltip;
    @FXML
    private Tooltip removeGreaterButtonTooltip;
    @FXML
    private Tooltip historyButtonTooltip;
    @FXML
    private Tooltip sumOfHealthButtonTooltip;
    @FXML
    private Tooltip refreshButtonTooltip;
    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private Label usernameLabel;

    private App app;
    private Tooltip shapeTooltip;
    private TableFilter<Worker> tableFilter;
    private Client client;
    private Stage askStage;
    private Stage primaryStage;
    private FileChooser fileChooser;
    private AskWindowController askWindowController;
    private Map<String, Color> userColorMap;
    private Map<Shape, Integer> shapeMap;
    private Map<Integer, Text> textMap;
    private Shape prevClicked;
    private Color prevColor;
    private Random randomGenerator;
    private ObservableResourceFactory resourceFactory;
    private Map<String, Locale> localeMap;

    /**
     * Initialize main window.
     */
    public void initialize() {
        initializeTable();
        initCanvas();
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        userColorMap = new HashMap<>();
        shapeMap = new HashMap<>();
        textMap = new HashMap<>();
        randomGenerator = new Random(RANDOM_SEED);
        localeMap = new HashMap<>();
        localeMap.put("English", new Locale("en", "NZ"));
        localeMap.put("\u0420\u0443\u0441\u0441\u043a\u0438\u0439", new Locale("ru", "RU"));
        localeMap.put("Deutsche", new Locale("de", "DE"));
        localeMap.put("Dansk", new Locale("da", "DK"));
        languageComboBox.setItems(FXCollections.observableArrayList(localeMap.keySet()));
    }

    /**
     * Initialize table.
     */
    private void initializeTable() {

        idColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        ownerColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getUserLogin()));
        creationDateColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCreationDate()));
        nameColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getName()));
        salaryColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getSalary()));
        coordinatesXColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCoordinates().getX()));
        coordinatesYColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCoordinates().getY()));
        statusColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getStatus()));
        positionColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getPosition()));
        endDateColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getEndDate()));
        organizationNameColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getOrganization().getFullName()));
        organizationTypeColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getOrganization().getType()));

        creationDateColumn.setCellFactory(column -> {
            TableCell<Worker, Date> cell = new TableCell<Worker, Date>() {
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty) {
                        setText(null);
                    }
                    else {
                        setText(DateConverter.dateToString(item));
                    }
                }
            };

            return cell;
        });
        endDateColumn.setCellFactory(column -> {
            TableCell<Worker, LocalDate> cell = new TableCell<Worker, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty) {
                        setText(null);
                    }
                    else {
                        setText(DateConverter.dateToString(item));
                    }
                }
            };

            return cell;
        });
    }

    public void initFilter(){
        tableFilter = new TableFilter<Worker>(workerTable,client.getWorkerManager().getCollection(),resourceFactory)
                .addFilter(idColumn, (w)->Integer.toString(w.getId()))
                .addFilter(nameColumn, (w)->w.getName())
                .addFilter(coordinatesXColumn, (w)->Float.toString(w.getCoordinates().getX()))
                .addFilter(coordinatesYColumn, (w)->Long.toString(w.getCoordinates().getY()))
                .addFilter(creationDateColumn, (w)-> DateConverter.dateToString(w.getCreationDate()))
                .addFilter(endDateColumn, (w)->DateConverter.dateToString(w.getEndDate()))
                .addFilter(positionColumn, (w)->w.getPosition().toString())
                .addFilter(statusColumn, (w)->w.getStatus().toString())
                .addFilter(salaryColumn, (w)->Long.toString(w.getSalary()))
                .addFilter(organizationNameColumn, (w)->w.getOrganization().getFullName())
                .addFilter(organizationTypeColumn, (w)->w.getOrganization().getType().toString())
                .addFilter(ownerColumn, (w)->w.getUserLogin());
    }

    public TableFilter<Worker> getFilter(){
        return tableFilter;
    }
    public TableColumn<Worker,?> getNameColumn(){
        return nameColumn;
    }

    private void initCanvas(){
        ZoomOperator zoomOperator = new ZoomOperator();

// Listen to scroll events (similarly you could listen to a button click, slider, ...)
        canvasPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                double zoomFactor = 1.5;
                if (event.getDeltaY() <= 0) {
                    // zoom out
                    zoomFactor = 1 / zoomFactor;

                }

                double x = event.getSceneX();
                double y = event.getSceneY();

                //if(!(event.getDeltaY()<=0 && (zoomOperator.getBounds().getHeight()<=1000||zoomOperator.getBounds().getWidth()<=1000))){
                /*if((event.getDeltaY()<=0 && (zoomOperator.getBounds().getMinX()>=1000||
                        zoomOperator.getBounds().getMinY()>=1000||
                        zoomOperator.getBounds().getMaxX()<=2000-1000||
                        zoomOperator.getBounds().getMaxY()<=2000-1000))) return;*/
                if((event.getDeltaY()<=0 && (zoomOperator.getBounds().getHeight()<=500||zoomOperator.getBounds().getWidth()<=500))) return;
                zoomOperator.zoom(canvasPane, zoomFactor, x, y);


            }
        });

        zoomOperator.draggable(canvasPane);
        canvasPane.setMinWidth(2000);
        canvasPane.setMinHeight(2000);
    }
    /**
     * Bind gui language.
     */
    private void bindGuiLanguage() {
        resourceFactory.setResources(ResourceBundle.getBundle
                (App.BUNDLE, localeMap.get(languageComboBox.getSelectionModel().getSelectedItem())));
        DateConverter.setPattern(resourceFactory.getRawString("DateFormat"));

        idColumn.textProperty().bind(resourceFactory.getStringBinding("IdColumn"));
        ownerColumn.textProperty().bind(resourceFactory.getStringBinding("OwnerColumn"));
        creationDateColumn.textProperty().bind(resourceFactory.getStringBinding("CreationDateColumn"));
        nameColumn.textProperty().bind(resourceFactory.getStringBinding("NameColumn"));
        coordinatesXColumn.textProperty().bind(resourceFactory.getStringBinding("CoordinatesXColumn"));
        coordinatesYColumn.textProperty().bind(resourceFactory.getStringBinding("CoordinatesYColumn"));
        salaryColumn.textProperty().bind(resourceFactory.getStringBinding("SalaryColumn"));
        endDateColumn.textProperty().bind(resourceFactory.getStringBinding("EndDateColumn"));
        positionColumn.textProperty().bind(resourceFactory.getStringBinding("PositionColumn"));
        statusColumn.textProperty().bind(resourceFactory.getStringBinding("StatusColumn"));
        organizationNameColumn.textProperty().bind(resourceFactory.getStringBinding("OrganizationNameColumn"));
        organizationTypeColumn.textProperty().bind(resourceFactory.getStringBinding("OrganizationTypeColumn"));

        tableTab.textProperty().bind(resourceFactory.getStringBinding("TableTab"));
        canvasTab.textProperty().bind(resourceFactory.getStringBinding("CanvasTab"));

        infoButton.textProperty().bind(resourceFactory.getStringBinding("InfoButton"));
        addButton.textProperty().bind(resourceFactory.getStringBinding("AddButton"));
        updateButton.textProperty().bind(resourceFactory.getStringBinding("UpdateButton"));
        removeButton.textProperty().bind(resourceFactory.getStringBinding("RemoveButton"));
        clearButton.textProperty().bind(resourceFactory.getStringBinding("ClearButton"));
        executeScriptButton.textProperty().bind(resourceFactory.getStringBinding("ExecuteScriptButton"));
        addIfMinButton.textProperty().bind(resourceFactory.getStringBinding("AddIfMinButton"));
        addIfMaxButton.textProperty().bind(resourceFactory.getStringBinding("AddIfMaxButton"));
        groupByEndDateButton.textProperty().bind(resourceFactory.getStringBinding("GroupByEndDateButton"));
        filterStartsWithNameButton.textProperty().bind(resourceFactory.getStringBinding("FilterStartsWithNameButton"));
        printUniqueSalariesButton.textProperty().bind(resourceFactory.getStringBinding("PrintUniqueSalariesButton"));
        removeFirstButton.textProperty().bind(resourceFactory.getStringBinding("RemoveFirstButton"));
        refreshButton.textProperty().bind(resourceFactory.getStringBinding("RefreshButton"));
       // helpButton.textProperty().bind(resourceFactory.getStringBinding("HelpButton"));

        infoButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("InfoButtonTooltip"));
        addButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("AddButtonTooltip"));
        updateButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("UpdateButtonTooltip"));
        removeButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("RemoveButtonTooltip"));
        clearButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("ClearButtonTooltip"));
        executeScriptButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("ExecuteScriptButtonTooltip"));
        addIfMinButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("AddIfMinButtonTooltip"));
        removeGreaterButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("RemoveGreaterButtonTooltip"));
        historyButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("HistoryButtonTooltip"));
        sumOfHealthButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("SumOfHealthButtonTooltip"));
        refreshButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("RefreshButtonTooltip"));
    }

    /**
     * Refresh button on action.
     */
    @FXML
    public void refreshButtonOnAction() {
        processAction(new CommandMsg("show"));
    }

    /**
     * Info button on action.
     */
    @FXML
    private void infoButtonOnAction() {
        processAction(new CommandMsg("info"));
    }


    /**
     * Update button on action.
     */
    @FXML
    private void updateButtonOnAction() {

        Worker worker = workerTable.getSelectionModel().getSelectedItem();
        if(worker!=null) {
            askWindowController.setWorker(worker);
            try {
                processAction(new CommandMsg("update").setArgument(Integer.toString(worker.getId())).setWorker(askWindowController.readWorker()));
            } catch (InvalidDataException e) {
                //e.printStackTrace();
            }
        }
    }

    /**
     * Remove button on action.
     */
    @FXML
    private void removeButtonOnAction() {
        Worker worker = workerTable.getSelectionModel().getSelectedItem();
        if(worker!=null) processAction(new CommandMsg("remove_by_id").setArgument(Integer.toString(worker.getId())));
    }

    @FXML
    private void removeFirstButtonOnAction() {
        processAction(new CommandMsg("remove_first"));
    }


    /**
     * Clear button on action.
     */
    @FXML
    private void clearButtonOnAction() {
        client.getCommandManager().runCommand(new CommandMsg("clear"));
    }

    /**
     * Execute script button on action.
     */
    @FXML
    private void executeScriptButtonOnAction() {
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) return;
        Response msg = null;
        try {
            msg = client.getCommandManager().runFile(selectedFile);
        } catch ( InvalidDataException | ConnectionException | FileException | CollectionException|CommandException e) {
            app.getOutputManager().error(e.getMessage());
        }
        if(msg!=null){
            System.out.println(msg.getMessage());
            if(msg.getStatus()== Response.Status.FINE)
            app.getOutputManager().info(msg.getMessage());
            else if (msg.getStatus()== Response.Status.ERROR)
                app.getOutputManager().error(msg.getMessage());
        }
    }

    /**
     * Add button on action.
     */
    @FXML
    private void addButtonOnAction() {
        //askWindowController.clearMarine();

        try {
            processAction(new CommandMsg("add").setWorker(askWindowController.readWorker()));
        } catch (InvalidDataException ignored) {

        }
    }
    /**
     * Add if min button on action.
     */
    @FXML
    private void addIfMinButtonOnAction() {
        try {
            Worker worker = askWindowController.readWorker();
            if(worker!=null) {
                processAction(new CommandMsg("add_if_min").setWorker(worker));
                /*workerTable.refresh();
                refreshCanvas();*/
            }
        } catch (InvalidDataException e) {

        }

    }

    @FXML
    private void addIfMaxButtonOnAction() {
        try {
            Worker worker = askWindowController.readWorker();
            if(worker!=null) {
                processAction(new CommandMsg("add_if_max").setWorker(worker));
                /*workerTable.refresh();
                refreshCanvas();*/
            }
        } catch (InvalidDataException e) {

        }
    }


    /**
     * Remove greater button on action.
     */
    @FXML
    private void groupByEndDateButtonOnAction() {
        processAction(new CommandMsg("group_counting_by_end_date"));
    }

    /**
     * History button on action.
     */
    @FXML
    private void printUniqueSalariesButtonOnAction() {
        processAction(new CommandMsg("print_unique_salary"));

    }

    /**
     * Sum of health button on action.
     */
    @FXML
    private void filterStartsWithNameButtonOnAction() {
        Label startsWithLabel = new Label();
        Stage stage = new Stage();
        Label nameLabel = new Label();
        TextField textField = new TextField();
        Button button = new Button();
        button.textProperty().bind(resourceFactory.getStringBinding("EnterButton"));
        button.setOnAction((e)->{
            String arg = textField.getText();
            if(arg!=null && !arg.equals("")) {
                processAction(new CommandMsg("filter_starts_with_name").setArgument(arg));
                stage.close();
            }

        });
        nameLabel.textProperty().bind(resourceFactory.getStringBinding("NameColumn"));
        button.setAlignment(Pos.CENTER);

        HBox hBox = new HBox(nameLabel,textField,button);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        Scene scene = new Scene(hBox);
        stage.setScene(scene);
        stage.setWidth(300);
        stage.setHeight(100);
        stage.setResizable(false);
        stage.showAndWait();
    }
    @FXML
    private void helpButtonOnAction(){
        processAction(new CommandMsg("help"));
    }

    /**
     * Request action.
     */

    private Response processAction(Request request){
        Response response = client.getCommandManager().runCommand(request);
        String msg = response.getMessage();

        return response;
    }


    public void refreshTable(){
        workerTable.refresh();
        tableFilter.updateFilters();
    }
    /**
     * Refreshes canvas.
     */

    public void refreshCanvas(ObservableList<Worker> collection, Collection<Worker> changes, CollectionOperation op) {

        for (Worker worker : changes) {
            if (!userColorMap.containsKey(worker.getUserLogin()))
                userColorMap.put(worker.getUserLogin(),
                        Color.color(randomGenerator.nextDouble(), randomGenerator.nextDouble(), randomGenerator.nextDouble()));
            //if(!changes.contains(worker)) continue;
            if(op==CollectionOperation.ADD) {
                addToCanvas(worker);
            }
            else if(op==CollectionOperation.REMOVE){
                removeFromCanvas(worker.getId());
            }
            else if(op==CollectionOperation.UPDATE){
                removeFromCanvas(worker.getId());
                addToCanvas(worker);
            }
        }
        List<Shape> circles = new ArrayList<Shape>(shapeMap.keySet());
        circles.sort((e1,e2)->((Circle)e1).getRadius()>((Circle)e2).getRadius()?-1:0);
        List<Shape> texts = new ArrayList<>(textMap.values());
        canvasPane.getChildren().setAll(circles);
        canvasPane.getChildren().addAll(texts);

    }

    private void removeFromCanvas(Integer id){
        Shape shape = MapUtils.getKeyByValue(shapeMap,id);
        Text text = textMap.get(id);
        shapeMap.values().remove(id);
        textMap.remove(id);
        canvasPane.getChildren().remove(shape);
        canvasPane.getChildren().remove(text);
    }
    private void addToCanvas(Worker worker){
        double size = Math.min(worker.getSalary() / 1000, MAX_SIZE);

        Shape circleObject = new Circle(size, userColorMap.get(worker.getUserLogin()));
        circleObject.setOnMouseClicked(this::shapeOnMouseClicked);
        circleObject.translateXProperty().bind(canvasPane.widthProperty().divide(2).add(worker.getCoordinates().getX()));
        circleObject.translateYProperty().bind(canvasPane.heightProperty().divide(2).subtract(worker.getCoordinates().getY()));

        circleObject.setOpacity(0.5);

        Text textObject = new Text(Integer.toString(worker.getId()));
        textObject.setOnMouseClicked(circleObject::fireEvent);
        textObject.setFont(Font.font(size / 3));
        textObject.setFill(userColorMap.get(worker.getUserLogin()).darker());
        textObject.translateXProperty().bind(circleObject.translateXProperty().subtract(textObject.getLayoutBounds().getWidth() / 2));
        textObject.translateYProperty().bind(circleObject.translateYProperty().add(textObject.getLayoutBounds().getHeight() / 4));

        canvasPane.getChildren().add(circleObject);
        canvasPane.getChildren().add(textObject);
        shapeMap.put(circleObject, worker.getId());
        textMap.put(worker.getId(), textObject);

        ScaleTransition circleAnimation = new ScaleTransition(ANIMATION_DURATION, circleObject);
        ScaleTransition textAnimation = new ScaleTransition(ANIMATION_DURATION, textObject);
        circleAnimation.setFromX(0);
        circleAnimation.setToX(1);
        circleAnimation.setFromY(0);
        circleAnimation.setToY(1);
        textAnimation.setFromX(0);
        textAnimation.setToX(1);
        textAnimation.setFromY(0);
        textAnimation.setToY(1);
        circleAnimation.play();
        textAnimation.play();
    }

    /**
     * Shape on mouse clicked.
     */
    private void shapeOnMouseClicked(MouseEvent event) {
        Shape shape = (Shape) event.getSource();
        //Tooltip.install(shape,shapeTooltip);
        long id = shapeMap.get(shape);
        for (Worker worker : workerTable.getItems()) {
            if (worker.getId() == id) {

                if(shapeTooltip!=null && shapeTooltip.isShowing()) shapeTooltip.hide();
                if(event.getButton()== MouseButton.SECONDARY) {
                    shapeTooltip = new Tooltip(worker.toString());
                    shapeTooltip.setAutoHide(true);
                    shapeTooltip.show(shape, event.getScreenX(), event.getScreenY());
                }
                workerTable.getSelectionModel().select(worker);
                //shapeTooltip.setText(worker.toString());
                //shapeTooltip.show(primaryStage);
                break;
            }
        }
        if (prevClicked != null) {
            prevClicked.setFill(prevColor);
        }
        prevClicked = shape;
        prevColor = (Color) shape.getFill();
        shape.setFill(prevColor.brighter());
    }

    public void setClient(Client client) {
        this.client = client;
        workerTable.setItems(client.getWorkerManager().getCollection());
        client.getWorkerManager().setController(this);
        client.setResourceFactory(resourceFactory);
    }

    public void setUsername(String username) {
        usernameLabel.setText(username);
    }

    public void setAskStage(Stage askStage) {
        this.askStage = askStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setAskWindowController(AskWindowController askWindowController) {
        this.askWindowController = askWindowController;
    }

    public void initLangs(ObservableResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        for (String localeName : localeMap.keySet()) {
            if (localeMap.get(localeName).equals(resourceFactory.getResources().getLocale()))
                languageComboBox.getSelectionModel().select(localeName);
        }
        if (languageComboBox.getSelectionModel().getSelectedItem().isEmpty()){
            if(localeMap.containsValue(Locale.getDefault())) languageComboBox.getSelectionModel().select(MapUtils.getKeyByValue(localeMap,Locale.getDefault()));
            else languageComboBox.getSelectionModel().selectFirst();
        }

        languageComboBox.setOnAction((event) ->{
            Locale locale = localeMap.get(languageComboBox.getValue());
            resourceFactory.setResources(ResourceBundle.getBundle
                    (App.BUNDLE, locale));
            DateConverter.setPattern(resourceFactory.getRawString("DateFormat"));
            workerTable.refresh();
        });
        bindGuiLanguage();
    }
    public void setApp(App a){
        app = a;
    }
}
