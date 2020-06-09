package seedu.canvas.component.canvas.unit;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.CanvasMode;
import seedu.canvas.component.canvas.DragData;
import seedu.canvas.component.canvas.TheCanvas;

public class UnitEventManager {

    private TheCanvas canvas = TheCanvas.getInstance();
    private DragData unitDragData = new DragData();

    /**
     * Constructor for the event manager of the units in the canvas.
     */
    public UnitEventManager() {
    }

    /**
     * Gets the mouse pressed event handler that MOVES the rectangle unit.
     *
     * @return
     *  The mouse pressed event handler
     */
    public EventHandler<MouseEvent> getOnMousePressedRectangle() {
        return onMousePressedRectangle;
    }

    /**
     * Gets the mouse dragged event handler that MOVES the rectangle unit.
     *
     * @return
     *  The mouse dragged event handler
     */
    public EventHandler<MouseEvent> getOnMouseDraggedRectangle() {
        return onMouseDraggedRectangle;
    }

    private EventHandler<MouseEvent> onMousePressedRectangle = mouseEvent -> {
        if (canvas.getCanvasMode() != CanvasMode.POINT || !mouseEvent.isPrimaryButtonDown()) {
            return;
        }

        unitDragData.setMouseAnchorX(mouseEvent.getSceneX());
        unitDragData.setMouseAnchorY(mouseEvent.getSceneY());

        Node unit = (Node) mouseEvent.getSource();

        unitDragData.setTranslateAnchorX(unit.getTranslateX());
        unitDragData.setTranslateAnchorY(unit.getTranslateY());
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

        unit.snapX(translateX);
        unit.snapY(translateY);


        // System.out.println(String.format("EventManager: %s %s", unit.getTranslateX(), unit.getTranslateY()));

        mouseEvent.consume();
    };
}
