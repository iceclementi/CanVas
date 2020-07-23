package seedu.canvas.component.canvas.unit;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.CanvasMode;
import seedu.canvas.component.canvas.DragData;
import seedu.canvas.component.canvas.Gesture;
import seedu.canvas.component.canvas.TheCanvas;

public class LineUnitEventManager {

    private TheCanvas canvas = TheCanvas.getInstance();
    private DragData unitDragData = new DragData();
    private LineUnit lineUnit = null;
    private Point2D mouseAnchorLocation = null;
    private Point2D previousPivotLocation = null;
    private Gesture gesture = Gesture.MOVE;

    /**
     * Constructor for the event manager of the units in the canvas.
     */
    public LineUnitEventManager() {
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
                lineUnit = (LineUnit) mouseEvent.getSource();
                canvas.changeMode(CanvasMode.POINT);
            }

            if (canvas.getCanvasMode() != CanvasMode.POINT) {
                return;
            }

            lineUnit = (LineUnit) mouseEvent.getSource();
            lineUnit.interactSingle();

            mouseAnchorLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());

            previousPivotLocation = new Point2D(lineUnit.getStartX(), lineUnit.getStartY());

            if (mouseEvent.isControlDown()) {
                unitDragData.getCopiedUnits().add(lineUnit);
                gesture = Gesture.COPY;
            }

            lineUnit.requestFocus();
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

        lineUnit = (LineUnit) mouseEvent.getSource();

        if (gesture == Gesture.MOVE) {
            double deltaX = canvas.toScale(mouseEvent.getSceneX() - mouseAnchorLocation.getX());
            double deltaY = canvas.toScale(mouseEvent.getSceneY() - mouseAnchorLocation.getY());

            double newStartX = previousPivotLocation.getX() + deltaX;
            double newStartY = previousPivotLocation.getY() + deltaY;

            lineUnit.move(newStartX, newStartY);
        }

        if (mouseEvent.isControlDown() && gesture == Gesture.COPY) {
            int mouseUnitX = CanvasGrid.toUnit(mouseEvent.getX());
            int mouseUnitY = CanvasGrid.toUnit(mouseEvent.getY());

            lineUnit.dragCopy(mouseUnitX, mouseUnitY, unitDragData);
        }

        mouseEvent.consume();
    };

    private EventHandler<MouseEvent> onMouseReleased = mouseEvent -> {
        unitDragData.reset();
        gesture = Gesture.MOVE;

        if (lineUnit != null) {
            lineUnit.focusSingle();
        }

        lineUnit = null;

        CanvasGrid.hideGridPoints();
    };
}
