package commands;

import common.commands.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class HelpCommand extends CommandImpl {
    public HelpCommand() {
        super("help", CommandType.NORMAL);
    }

    @Override
    public String execute() {
        /*Platform.runLater(()->{
            Text text = new Text(CommandManager.getHelp());
            TextFlow textFlow = new TextFlow(text);
            Stage stage = new Stage();
            Scene scene = new Scene(textFlow);
            stage.setScene(scene);
            stage.setTitle("s");
            stage.showAndWait();
        });*/
        return CommandManager.getHelp();
    }
}
