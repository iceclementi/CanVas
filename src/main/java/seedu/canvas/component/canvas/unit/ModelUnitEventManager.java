package seedu.canvas.component.canvas.unit;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.CanvasMode;
import seedu.canvas.component.canvas.DragData;
import seedu.canvas.component.canvas.Gesture;
import seedu.canvas.component.canvas.TheCanvas;

public class ModelUnitEventManager {

    private TheCanvas canvas = TheCanvas.getInstance();
    private DragData unitDragData = new DragData();
    private ModelUnit modelUnit = null;
    private Point2D mouseAnchorLocation = null;
    private Point2D previousPivotLocation = null;
    private Gesture gesture = Gesture.MOVE;

    /**
     * Constructor for the event manager of the units in the canvas.
     */
    public ModelUnitEventManager() {
    }

    /**
     * Gets the mouse pressed event handler.
     *
     * @return
     *  The mouse pressed event handler
     */
    public EventHandler<MouseEvent> getOnMousePressed() {
        return onMousePressed;
    }

    /**
     * Gets the mouse dragged event handler.
     *
     * @return
     *  The mouse dragged event handler
     */
    public EventHandler<MouseEvent> getOnMouseDragged() {
        return onMouseDragged;
    }

    /**
     * Gets the mouse released event handler.
     *
     * @return
     *  The mouse released event handler
     */
    public EventHandler<MouseEvent> getOnMouseReleased() {
        return onMouseReleased;
    }

    private EventHandler<MouseEvent> onMousePressed = mouseEvent -> {

        if (mouseEvent.isPrimaryButtonDown()) {

            if (canvas.getCanvasMode() == CanvasMode.SHAPE) {
                modelUnit = (ModelUnit) mouseEvent.getSource();
                canvas.changeMode(CanvasMode.POINT);
            }

            if (canvas.getCanvasMode() != CanvasMode.POINT) {
                return;
            }

            modelUnit = (ModelUnit) mouseEvent.getSource();
            modelUnit.interactSingle();

            mouseAnchorLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());

            previousPivotLocation = new Point2D(modelUnit.getX(), modelUnit.getY());

            if (mouseEvent.isControlDown()) {
                unitDragData.getCopiedCanvasNodes().add(modelUnit);
                gesture = Gesture.COPY;
            }

            modelUnit.requestFocus();
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

        CanvasGrid.showGridPoints();

        modelUnit = (ModelUnit) mouseEvent.getSource();

        if (gesture == Gesture.MOVE) {
            double deltaX = canvas.toScale(mouseEvent.getSceneX() - mouseAnchorLocation.getX());
            double deltaY = canvas.toScale(mouseEvent.getSceneY() - mouseAnchorLocation.getY());

            double newX = previousPivotLocation.getX() + deltaX;
            double newY = previousPivotLocation.getY() + deltaY;

            modelUnit.move(newX, newY);
        }

        if (mouseEvent.isControlDown() && gesture == Gesture.COPY) {
            modelUnit.dragCopy(mouseEvent.getX(), mouseEvent.getY(), unitDragData);
        }

        mouseEvent.consume();
    };

    private EventHandler<MouseEvent> onMouseReleased = mouseEvent -> {
        gesture = Gesture.MOVE;

        if (unitDragData.getRecentCanvasNode() != null) {
            unitDragData.getRecentCanvasNode().focusSingle();
            unitDragData.reset();
        } else if (modelUnit != null) {
            modelUnit.focusSingle();
        }

        modelUnit = null;

        CanvasGrid.hideGridPoints();
        mouseEvent.consume();
    };
}
