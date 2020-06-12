package seedu.canvas.component.canvas.unit;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.CanvasMode;
import seedu.canvas.component.canvas.DragData;
import seedu.canvas.component.canvas.Gesture;
import seedu.canvas.component.canvas.TheCanvas;

public class UnitEventManager {

    private TheCanvas canvas = TheCanvas.getInstance();
    private DragData unitDragData = new DragData();
    private Gesture gesture = Gesture.MOVE;

    /**
     * Constructor for the event manager of the units in the canvas.
     */
    public UnitEventManager() {
    }

    /**
     * Gets the mouse pressed event handler.
     *
     * @return
     *  The mouse pressed event handler
     */
    public EventHandler<MouseEvent> getOnMousePressedRectangle() {
        return onMousePressedRectangle;
    }

    /**
     * Gets the mouse dragged event handler.
     *
     * @return
     *  The mouse dragged event handler
     */
    public EventHandler<MouseEvent> getOnMouseDraggedRectangle() {
        return onMouseDraggedRectangle;
    }

    /**
     * Gets the mouse released event handler.
     *
     * @return
     *  The mouse released event handler
     */
    public EventHandler<MouseEvent> getOnMouseReleasedRectangle() {
        return onMouseReleased;
    }

    private EventHandler<MouseEvent> onMousePressedRectangle = mouseEvent -> {
        if (canvas.getCanvasMode() != CanvasMode.POINT || !mouseEvent.isPrimaryButtonDown()) {
            return;
        }

        RectangleUnit unit = (RectangleUnit) mouseEvent.getSource();
        unit.selectAnchorPoints();

        unitDragData.setMouseAnchorX(mouseEvent.getSceneX());
        unitDragData.setMouseAnchorY(mouseEvent.getSceneY());

        unitDragData.setTranslateAnchorX(unit.getTranslateX());
        unitDragData.setTranslateAnchorY(unit.getTranslateY());

        if (mouseEvent.isControlDown()) {
            unitDragData.getCopiedRectangles().add(unit);
            gesture = Gesture.COPY;
        } else if (mouseEvent.isShiftDown()) {
            gesture = Gesture.RESIZE;
        }
    };

    private EventHandler<MouseEvent> onMouseDraggedRectangle = mouseEvent -> {
        if (canvas.getCanvasMode() != CanvasMode.POINT || !mouseEvent.isPrimaryButtonDown()) {
            return;
        }

        double scale = canvas.getCanvasScale();

        RectangleUnit unit = (RectangleUnit) mouseEvent.getSource();

        double translateDeltaX = (mouseEvent.getSceneX() - unitDragData.getMouseAnchorX()) / scale;
        double translateDeltaY = (mouseEvent.getSceneY() - unitDragData.getMouseAnchorY()) / scale;

        // unit.setTranslateX(unitDragData.getTranslateAnchorX() + translateDeltaX);
        // unit.setTranslateY(unitDragData.getTranslateAnchorY() + translateDeltaY);

        double translateX = unitDragData.getTranslateAnchorX() + translateDeltaX;
        double translateY = unitDragData.getTranslateAnchorY() + translateDeltaY;

        if (mouseEvent.isControlDown() && gesture == Gesture.COPY) {
            unit.dragCopy(mouseEvent.getX(), mouseEvent.getY(), unitDragData);
        } else if (mouseEvent.isShiftDown() && gesture == Gesture.RESIZE) {
            unit.resizeSnapX(mouseEvent.getX());
            unit.resizeSnapY(mouseEvent.getY());
        } else if (gesture == Gesture.MOVE) {
            unit.moveSnapX(translateX);
            unit.moveSnapY(translateY);
        }

        // System.out.println(String.format("EventManager: %s %s", unit.getTranslateX(), unit.getTranslateY()));

        mouseEvent.consume();
    };

    private EventHandler<MouseEvent> onMouseReleased = mouseEvent -> {
        unitDragData.reset();
        gesture = Gesture.MOVE;
    };
}
