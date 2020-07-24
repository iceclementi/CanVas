package seedu.canvas.component.canvas.draw;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.CanvasMode;
import seedu.canvas.component.canvas.DragData;
import seedu.canvas.component.canvas.Gesture;
import seedu.canvas.component.canvas.TheCanvas;

public class DrawingStrokeEventManager {

    private TheCanvas canvas = TheCanvas.getInstance();
    private Drawing drawing = null;
    private DragData drawingDragData = new DragData();
    private Point2D mouseAnchorLocation = null;
    private Point2D previousPivotLocation = null;
    private Gesture gesture = Gesture.MOVE;

    public DrawingStrokeEventManager() {
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

        if (mouseEvent.isPrimaryButtonDown()) {

            if (canvas.getCanvasMode() != CanvasMode.POINT) {
                return;
            }

            drawing = ((DrawingStroke) mouseEvent.getSource()).getDrawing();
            drawing.interactSingle();

            mouseAnchorLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            previousPivotLocation = new Point2D(drawing.getStartX(), drawing.getStartY());

            if (mouseEvent.isControlDown()) {
                drawingDragData.getCopiedCanvasNodes().add(drawing);
                gesture = Gesture.COPY;
            }

            mouseEvent.consume();
        }
    };

    private EventHandler<MouseEvent> onMouseDragged = mouseEvent -> {
        if (canvas.getCanvasMode() != CanvasMode.POINT || !mouseEvent.isPrimaryButtonDown()) {
            return;
        }

        if (previousPivotLocation == null) {
            return;
        }

        drawing = ((DrawingStroke) mouseEvent.getSource()).getDrawing();

        if (gesture == Gesture.MOVE) {
            double deltaX = canvas.toScale(mouseEvent.getSceneX() - mouseAnchorLocation.getX());
            double deltaY = canvas.toScale(mouseEvent.getSceneY() - mouseAnchorLocation.getY());

            double newUnitX = previousPivotLocation.getX() + deltaX;
            double newUnitY = previousPivotLocation.getY() + deltaY;

            drawing.move(newUnitX, newUnitY);
        }

        if (mouseEvent.isControlDown() && gesture == Gesture.COPY) {
            drawing.dragCopy(mouseEvent.getX(), mouseEvent.getY(), drawingDragData);
        }

        mouseEvent.consume();
    };

    private EventHandler<MouseEvent> onMouseReleased = mouseEvent -> {
        gesture = Gesture.MOVE;

        if (drawingDragData.getRecentCanvasNode() != null) {
            drawingDragData.getRecentCanvasNode().focusSingle();
            drawingDragData.reset();
        } else if (drawing != null) {
            drawing.focusSingle();
        }

        drawing = null;
    };
}
