package seedu.canvas.component.canvas.unit;

import javafx.event.EventHandler;
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
    private UnitPoint previousPivotLocation = null;
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
                return;
            }

            if (canvas.getCanvasMode() != CanvasMode.POINT) {
                return;
            }

            modelUnit = (ModelUnit) mouseEvent.getSource();
            modelUnit.interact();

            unitDragData.setMouseAnchorX(mouseEvent.getSceneX());
            unitDragData.setMouseAnchorY(mouseEvent.getSceneY());

            previousPivotLocation = new UnitPoint(modelUnit.getUnitX(), modelUnit.getUnitY());

            if (mouseEvent.isControlDown()) {
                unitDragData.getCopiedUnits().add(modelUnit);
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
            int deltaX = CanvasGrid.toUnit(canvas.toScale(mouseEvent.getSceneX() - unitDragData.getMouseAnchorX()));
            int deltaY = CanvasGrid.toUnit(canvas.toScale(mouseEvent.getSceneY() - unitDragData.getMouseAnchorY()));

            int newUnitX = previousPivotLocation.getUnitX() + deltaX;
            int newUnitY = previousPivotLocation.getUnitY() + deltaY;

            modelUnit.move(newUnitX, newUnitY);
        }

        if (mouseEvent.isControlDown() && gesture == Gesture.COPY) {
            int mouseUnitX = CanvasGrid.toUnit(mouseEvent.getX());
            int mouseUnitY = CanvasGrid.toUnit(mouseEvent.getY());

            modelUnit.dragCopy(mouseUnitX, mouseUnitY, unitDragData);
        }

        mouseEvent.consume();
    };

    private EventHandler<MouseEvent> onMouseReleased = mouseEvent -> {
        unitDragData.reset();
        gesture = Gesture.MOVE;

        if (modelUnit != null) {
            modelUnit.focus();
        }

        modelUnit = null;

        CanvasGrid.hideGridPoints();
    };
}
