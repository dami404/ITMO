package collection;

import common.collection.WorkerManagerImpl;
import common.connection.CollectionOperation;
import common.connection.Response;
import common.data.Worker;
import common.exceptions.NoSuchIdException;
import controllers.MainWindowController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class WorkerObservableManager extends WorkerManagerImpl<ObservableList<Worker>> {
    private ObservableList<Worker> collection;
    private Set<Integer> uniqueIds;
    private MainWindowController controller;
    public WorkerObservableManager(){
        collection = FXCollections.observableArrayList();
        uniqueIds = ConcurrentHashMap.newKeySet();
    }

    public Set<Integer> getUniqueIds(){
        return uniqueIds;
    }
    public void applyChanges(Response response){
        CollectionOperation op = response.getCollectionOperation();
        Collection<Worker> changes = response.getCollection();
        ObservableList<Worker> old = FXCollections.observableArrayList(collection);

        if(op==CollectionOperation.ADD){
            for(Worker worker: changes){
                super.addWithoutIdGeneration(worker);
            }
        }
        if(op==CollectionOperation.UPDATE){
            for(Worker worker: changes){
                super.updateByID(worker.getId(),worker);
            }
        }

        if(op==CollectionOperation.REMOVE){
            for(Worker worker: changes){
                Collections.copy(old,collection);
                super.removeByID(worker.getId());
            }
        }
        if(controller!=null && op!=CollectionOperation.NONE && changes!=null && !changes.isEmpty()){
            Platform.runLater(()->{

                controller.refreshCanvas(op!=CollectionOperation.REMOVE?collection:old, changes, op);
                controller.refreshTable();
            });
        }

    }
    public ObservableList<Worker> getCollection(){
        return collection;
    }
    @Override
    public void updateByID(Integer id, Worker newWorker) {
        assertNotEmpty();
        Optional<Worker> worker = getCollection().stream()
                .filter(w -> w.getId() == id)
                .findFirst();
        if (!worker.isPresent()) {
            throw new NoSuchIdException(id);
        }
        Collections.replaceAll(collection,worker.get(),newWorker);
    }

    public void setController(MainWindowController c){
        controller = c;
    }
    public MainWindowController getController(){
        return controller;
    }
}
