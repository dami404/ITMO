package commands;

import client.Client;
import common.collection.WorkerManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import controllers.tools.ObservableResourceFactory;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

import static common.utils.DateConverter.dateToString;

public class GroupCountingByEndDateCommand extends CommandImpl {
    private final Client client;
    public GroupCountingByEndDateCommand(Client client) {
        super("group_counting_by_end_date", CommandType.NORMAL);
        this.client=client;
    }

    @Override
    public String execute() {
        Map<LocalDate, Integer> map = client.getWorkerManager().groupByEndDate();
        if (map.isEmpty()) return "none of the elements have endDate field";


        String res = "";
        ObservableList<Map.Entry<LocalDate, Integer>> list = FXCollections.observableArrayList();
        for (Map.Entry<LocalDate, Integer> pair : map.entrySet()) {
            LocalDate endDate = pair.getKey();
            int quantity = map.get(endDate);
            String cur = dateToString(endDate) + " : " + quantity;
            res += cur + "\n";
            list.add(pair);
        }
        Platform.runLater(()->{

            TableColumn<Map.Entry<LocalDate, Integer>,LocalDate> dateColumn = new TableColumn<>();
            TableColumn<Map.Entry<LocalDate, Integer>,Integer> quantityColumn = new TableColumn<>();
            dateColumn.setCellValueFactory(cellData ->
                    new ReadOnlyObjectWrapper<>(cellData.getValue().getKey()));
            quantityColumn.setCellValueFactory(cellData ->
                    new ReadOnlyObjectWrapper<>(cellData.getValue().getValue()));
            dateColumn.textProperty().bind(client.getResourceFactory().getStringBinding("EndDateColumn"));
            quantityColumn.textProperty().bind(client.getResourceFactory().getStringBinding("QuantityColumn"));
            TableView<Map.Entry<LocalDate, Integer>> tableView = new TableView<>();
            tableView.getColumns().add(dateColumn);
            tableView.getColumns().add(quantityColumn);
            tableView.setItems(list);
            Stage stage = new Stage();
            Scene scene = new Scene(tableView);
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            stage.setScene(scene);
            stage.show();
        });

        return res;
    }
}
