package controllers.tools;

import common.data.Position;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;



public class ZoomOperator {

    private Timeline timeline;
    private Bounds bounds;
    private final double maxW = 2000;
    private final double maxH = 2000;
    public ZoomOperator() {
        this.timeline = new Timeline(60);
        bounds = new BoundingBox(0,0,0,0);
    }

    class Position {
        double x,y;
    }

    public void draggable(Node node) {
        /*final Position pos = new Position();

        //Prompt the user that the node can be clicked
        node.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> node.setCursor(Cursor.HAND));
        node.addEventHandler(MouseEvent.MOUSE_EXITED, event -> node.setCursor(Cursor.DEFAULT));

        //Prompt the user that the node can be dragged
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            node.setCursor(Cursor.MOVE);

            //When a press event occurs, the location coordinates of the event are cached
            pos.x = event.getX();
            pos.y = event.getY();
        });
        node.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> node.setCursor(Cursor.DEFAULT));

        //Realize drag and drop function
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            double distanceX = event.getX() - pos.x;
            double distanceY = event.getY() - pos.y;

            double x = node.getLayoutX() + distanceX;
            double y = node.getLayoutY() + distanceY;
            //After calculating X and y, relocate the node to the specified coordinate point (x, y)
            System.out.println(x);
            node.relocate(x, y);
        });*/

        Position mouse = new Position();
        node.setOnMousePressed(event -> {
            mouse.x = event.getSceneX() ;
            mouse.y = event.getSceneY() ;
            node.setCursor(Cursor.MOVE);
        });

        node.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - mouse.x ;
            double deltaY = event.getSceneY() - mouse.y ;
            node.setTranslateX(node.getTranslateX() + deltaX);
            node.setTranslateY(node.getTranslateY() + deltaY);
            mouse.x = event.getSceneX() ;
            mouse.y = event.getSceneY() ;
        });

        node.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> node.setCursor(Cursor.DEFAULT));

    }
    public void zoom(Node node, double factor, double x, double y) {
        // determine scale
        double oldScale = node.getScaleX();
        double scale = oldScale * factor;
        double f = (scale / oldScale) - 1;

        // determine offset that we will have to move the node
        bounds = node.localToScene(node.getBoundsInLocal());
        double dx = (x - (bounds.getWidth() / 2 + bounds.getMinX()));
        double dy = (y - (bounds.getHeight() / 2 + bounds.getMinY()));
        /*
        if(f<0) {
            if ((bounds.getMaxX() <= maxW) || (bounds.getMinX() >= 0)) {
                dx=0;
                scale=0;
            }
            if ((bounds.getMaxY() <= maxH) || (bounds.getMinY() >= 0)) {
                dy=0;
                scale=0;
            }
        }*/
        // timeline that scales and moves the node
        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(200), new KeyValue(node.translateXProperty(), node.getTranslateX() - f * dx)),
                new KeyFrame(Duration.millis(200), new KeyValue(node.translateYProperty(), node.getTranslateY() - f * dy)),
                new KeyFrame(Duration.millis(200), new KeyValue(node.scaleXProperty(), scale)),
                new KeyFrame(Duration.millis(200), new KeyValue(node.scaleYProperty(), scale))
        );
        timeline.play();
    }
    public Bounds getBounds(){
        return bounds;
    }
}