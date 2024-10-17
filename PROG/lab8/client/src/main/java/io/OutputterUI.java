package io;

import common.io.ConsoleOutputter;
import common.io.OutputManager;
import controllers.tools.ObservableResourceFactory;
import controllers.tools.ResourceException;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.Arrays;

public class OutputterUI implements OutputManager {
    private ObservableResourceFactory resourceFactory;
    public OutputterUI(ObservableResourceFactory rf){
        resourceFactory = rf;
    }

    public void error(String s){
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            //alert.setTitle("error");
            //alert.setHeaderText(s);
            String msg = resourceFactory.getString(s);
            if(msg!=null) {
                alert.setContentText(msg);
                alert.setHeaderText(null);
            }
            else {
                alert.setHeaderText(resourceFactory.getString("InternalError"));
                alert.setContentText(s);
            }
            alert.getDialogPane().setStyle("-fx-font-size: 13");
            alert.show();

        });
    }

    public void info(String s){
        Platform.runLater(()->{
            try {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                //alert.setTitle("info");
                //alert.setHeaderText(s);
                alert.setContentText(resourceFactory.getString(s));
                alert.getDialogPane().setStyle("-fx-font-size: 13");
                alert.setHeaderText(null);
                alert.show();
            }catch (ResourceException ignored){
                //ConsoleOutputter.printErr(e.getMessage());
            }
        });
    }

}
