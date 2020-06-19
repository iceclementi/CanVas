package seedu.canvas.component.canvas.draw;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.CanvasMode;
import seedu.canvas.component.canvas.TheCanvas;

public class DrawingCanvasEventManager {

    private TheCanvas canvas = TheCanvas.getInstance();
    private Drawing drawing = null;

    private Point2D startLocation = null;

    public DrawingCanvasEventManager() {
    }

    public EventHandler<MouseEvent> getOnMousePressed() {
        return onMousePressed;
    }

    public EventHandler<MouseEvent> getOnMouseDragged() {
        return onMouseDragged;
    }

    public EventHandler<MouseEvent> getOnMouseReleased() {
        return onMouseReleased;
    }

    private EventHandler<MouseEvent> onMousePressed = mouseEvent -> {
        if (canvas.getCanvasMode() != CanvasMode.DRAW || !mouseEvent.isPrimaryButtonDown()) {
            return;
        }

        startLocation = new Point2D(mouseEvent.getX(), mouseEvent.getY());

        drawing = new Drawing();
        // drawing = new Drawing(new DrawingStroke(drawing, mouseEvent.getX(), mouseEvent.getY(),
        //         mouseEvent.getX(), mouseEvent.getY()));
        // drawing.addStroke(new DrawingStroke(mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getX(), mouseEvent.getY()));
    };

    private EventHandler<MouseEvent> onMouseDragged = mouseEvent -> {
        if (canvas.getCanvasMode() != CanvasMode.DRAW || !mouseEvent.isPrimaryButtonDown()) {
            return;
        }

        if (startLocation == null) {
            return;
        }

        double endX = mouseEvent.getX();
        double endY = mouseEvent.getY();

        drawing.addStroke(new DrawingStroke(drawing, startLocation.getX(), startLocation.getY(), endX, endY));

        startLocation = new Point2D(endX, endY);

        mouseEvent.consume();
    };

    private EventHandler<MouseEvent> onMouseReleased = mouseEvent -> {
        if (drawing != null) {
            drawing.finishDrawing();
        }

        drawing = null;
    };
}
