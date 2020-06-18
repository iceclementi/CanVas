package seedu.canvas.component.canvas.draw;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import seedu.canvas.component.canvas.CanvasGrid;

import java.util.ArrayList;

public class DrawingCanvas extends Pane {

    private static DrawingCanvas drawingCanvas = null;

    private ArrayList<Drawing> drawings = new ArrayList<>();

    private DrawingCanvas() {
    }

    public static DrawingCanvas getInstance() {
        if (drawingCanvas == null) {
            drawingCanvas = new DrawingCanvas();
        }

        return drawingCanvas;
    }

    public void initialise() {
        initialiseStyle();
        initialiseEvents();
    }

    public void addStroke(Line stroke) {
        getChildren().add(stroke);
    }

    public void addDrawing(Drawing drawing) {
        drawings.add(drawing);
    }

    public void removeDrawing(Drawing drawing) {
        drawings.remove(drawing);
        getChildren().removeAll(drawing.getDrawing());
    }

    public void startDrawing() {
        toFront();
    }

    public void stopDrawing() {
        toBack();
    }

    private void initialiseStyle() {
        setPrefWidth(CanvasGrid.WIDTH);
        setPrefHeight(CanvasGrid.HEIGHT);
    }

    private void initialiseEvents() {
        DrawingCanvasEventManager drawingCanvasEventManager = new DrawingCanvasEventManager();

        addEventFilter(MouseEvent.MOUSE_PRESSED, drawingCanvasEventManager.getOnMousePressed());
        addEventFilter(MouseEvent.MOUSE_DRAGGED, drawingCanvasEventManager.getOnMouseDragged());
        addEventFilter(MouseEvent.MOUSE_RELEASED, drawingCanvasEventManager.getOnMouseReleased());
    }
}
