package seedu.canvas.component.canvas.unit;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.*;

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
        return onMousePressedRectangle;
    }

    /**
     * Gets the mouse dragged event handler.
     *
     * @return
     *  The mouse dragged event handler
     */
    public EventHandler<MouseEvent> getOnMouseDragged() {
        return onMouseDraggedRectangle;
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

    public EventHandler<MouseEvent> getOnMouseClicked() {
        return onMouseClicked;
    }

    private EventHandler<MouseEvent> onMousePressedRectangle = mouseEvent -> {
        if (canvas.getCanvasMode() != CanvasMode.POINT || !mouseEvent.isPrimaryButtonDown()) {
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
    };

    private EventHandler<MouseEvent> onMouseDraggedRectangle = mouseEvent -> {
        if (canvas.getCanvasMode() != CanvasMode.POINT || !mouseEvent.isPrimaryButtonDown()) {
            return;
        }

        if (previousPivotLocation == null) {
            return;
        }

        double scale = canvas.getCanvasScale();

        modelUnit = (ModelUnit) mouseEvent.getSource();

        if (gesture == Gesture.MOVE) {
            int deltaX = CanvasGrid.toUnit((mouseEvent.getSceneX() - unitDragData.getMouseAnchorX()) / scale);
            int deltaY = CanvasGrid.toUnit((mouseEvent.getSceneY() - unitDragData.getMouseAnchorY()) / scale);

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
    };

    private EventHandler<MouseEvent> onMouseClicked = mouseEvent -> {
        if (modelUnit != null) {
            canvas.focusUnit(modelUnit);
        }

        modelUnit = null;
    };
}
