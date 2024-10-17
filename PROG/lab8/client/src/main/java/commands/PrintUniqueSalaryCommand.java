package commands;


import common.collection.WorkerManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.exceptions.EmptyCollectionException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class PrintUniqueSalaryCommand extends CommandImpl {
    private final WorkerManager collectionManager;

    public PrintUniqueSalaryCommand(WorkerManager cm) {
        super("print_unique_salary", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() {
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        ObservableList<Long> list = collectionManager.getUniqueSalaries().stream().collect(Collectors.toCollection(FXCollections::observableArrayList));
        Platform.runLater(()->{
            ListView<Long> listView = new ListView<>();
            listView.setItems(list);
            Stage stage = new Stage();
            Scene scene = new Scene(listView);
            stage.setScene(scene);
            stage.show();
        });
        return list.stream().map(n -> Long.toString(n)).reduce("", (a, b) -> a + b + "\n");
    }
}

