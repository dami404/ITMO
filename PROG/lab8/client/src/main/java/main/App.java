package main;


import java.util.ResourceBundle;

import client.Client;
import common.connection.CommandMsg;
import common.connection.Request;
import common.exceptions.*;
import controllers.AskWindowController;
import controllers.LoginWindowController;
import controllers.MainWindowController;
import io.OutputterUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import controllers.tools.*;
import org.google.jhsheets.filtered.FilteredTableView;

import static common.io.ConsoleOutputter.*;


public class App extends Application {
    //public static Logger logger = LogManager.getLogger("logger");
    //static final Logger logger = LogManager.getRootLogger();
    private static final String APP_TITLE = "Worker Manager";
    public static final String BUNDLE = "bundles.gui";
    private Stage primaryStage;
    static Client client;
    static String address;
    static int port;
    private static ObservableResourceFactory resourceFactory;
    private OutputterUI outputter;
    public static void main(String[] args) {
        resourceFactory = new ObservableResourceFactory();
        resourceFactory.setResources(ResourceBundle.getBundle(BUNDLE));
        //OutputerUI.setResourceFactory(resourceFactory);
        //Outputer.setResourceFactory(resourceFactory);


        if (initialize(args)) launch(args);
        else System.exit(0);
    }
    private static boolean initialize(String[] args) {
        print("Client has started");

        address = "localhost";
        String strPort = "4445";
        port = 0;
        try {
            if (args.length == 2) {
                address = args[0];
                strPort = args[1];
            }
            if(args.length == 1){
                strPort = args[0];
                print("no address passed by arguments, setting default : " + address);
            }
            if(args.length == 0){
                print("no port and no address passed by arguments, setting default :" + address + "/" + strPort);
            }
            try {
                port = Integer.parseInt(strPort);
            } catch (NumberFormatException e) {
                throw new InvalidPortException();
            }

        } catch (ConnectionException e) {
            print(e.getMessage());
            return false;
        }
        return true;
    }
    @Override public void init(){
        resourceFactory = new ObservableResourceFactory();
        resourceFactory.setResources(ResourceBundle.getBundle(BUNDLE));
        outputter = new OutputterUI(resourceFactory);
        try {
            client = new Client(address,port);
            client.setOutputManager(outputter);
            client.setResourceFactory(resourceFactory);
            client.connectionTest();
            //client.start();
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void start(Stage stage) {
        try {

            primaryStage = stage;
            FXMLLoader loginWindowLoader = new FXMLLoader();
            loginWindowLoader.setLocation(getClass().getResource("/view/LoginWindow.fxml"));
            Parent loginWindowRootNode = loginWindowLoader.load();
            Scene loginWindowScene = new Scene(loginWindowRootNode);
            LoginWindowController loginWindowController = loginWindowLoader.getController();
            loginWindowController.setApp(this);
            loginWindowController.setClient(client);
            loginWindowController.initLangs(resourceFactory);

            stage.setTitle(APP_TITLE);

            stage.setScene(loginWindowScene);
            stage.setResizable(false);
            stage.show();
           /* stage.setOnShown((e)->{
                //if(!stage.isShowing()){
                    try {
                        client.send(new CommandMsg().setStatus(Request.Status.HELLO));
                    } catch (ConnectionException ex) {

                    }
                //}
            });*/


            //setMainWindow();
        } catch (Exception exception) {
            // TODO: Обработать ошибки
            System.out.println(exception);
            exception.printStackTrace();
        }
    }


    public void setMainWindow() {
        try {
            FXMLLoader mainWindowLoader = new FXMLLoader();
            mainWindowLoader.setLocation(getClass().getResource("/view/MainWindow.fxml"));
            Parent mainWindowRootNode = mainWindowLoader.load();
            Scene mainWindowScene = new Scene(mainWindowRootNode);
            MainWindowController mainWindowController = mainWindowLoader.getController();
            mainWindowController.initLangs(resourceFactory);

            FXMLLoader askWindowLoader = new FXMLLoader();
            askWindowLoader.setLocation(getClass().getResource("/view/AskWindow.fxml"));
            Parent askWindowRootNode = askWindowLoader.load();
            Scene askWindowScene = new Scene(askWindowRootNode);
            Stage askStage = new Stage();
            askStage.setTitle(APP_TITLE);
            askStage.setScene(askWindowScene);
            askStage.setResizable(false);
            askStage.initModality(Modality.WINDOW_MODAL);
            askStage.initOwner(primaryStage);
            AskWindowController askWindowController = askWindowLoader.getController();
            askWindowController.setAskStage(askStage);
            askWindowController.initLangs(resourceFactory);
            askWindowController.setApp(this);


            //mainWindowController.setUsername("aa");//client.getUser().getLogin());
            mainWindowController.setUsername(client.getUser()!=null?client.getUser().getLogin():"");
            mainWindowController.setAskStage(askStage);
            mainWindowController.setPrimaryStage(primaryStage);
            mainWindowController.setAskWindowController(askWindowController);
            //mainWindowController.refreshButtonOnAction();
            mainWindowController.initLangs(resourceFactory);
            mainWindowController.setClient(client);
            mainWindowController.initFilter();
            mainWindowController.setApp(this);


            primaryStage.setScene(mainWindowScene);
            primaryStage.setMinWidth(mainWindowScene.getWidth());
            primaryStage.setMinHeight(mainWindowScene.getHeight());
            primaryStage.setResizable(true);
            primaryStage.setOnCloseRequest((e)->{
                print("exiting...");
                client.close();
            });





        } catch (Exception exception) {
            // TODO: Обработать ошибки
            System.out.println(exception);
            exception.printStackTrace();
        }
    }

    public OutputterUI getOutputManager(){
        return outputter;
    }

}
