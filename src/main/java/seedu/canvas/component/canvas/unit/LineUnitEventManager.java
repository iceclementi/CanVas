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
    private Point2D mouseAnchorLocation = null;
    private DragData unitDragData = new DragData();
    private LineUnit lineUnit = null;
    private UnitPoint previousPivotLocation = null;
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

    private EventHandler<MouseEvent> onMousePressed = mouseEvent -> {
        if (canvas.getCanvasMode() != CanvasMode.POINT || !mouseEvent.isPrimaryButtonDown()) {
            return;
        }

        lineUnit = (LineUnit) mouseEvent.getSource();
        lineUnit.interact();

        mouseAnchorLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());

        previousPivotLocation = new UnitPoint(lineUnit.getUnitStartX(), lineUnit.getUnitStartY());

        // if (mouseEvent.isControlDown()) {
        //     unitDragData.getCopiedUnits().add(lineUnit);
        //     gesture = Gesture.COPY;
        // }
    };

    private EventHandler<MouseEvent> onMouseDraggedRectangle = mouseEvent -> {
        if (canvas.getCanvasMode() != CanvasMode.POINT || !mouseEvent.isPrimaryButtonDown()) {
            return;
        }

        if (previousPivotLocation == null) {
            return;
        }

        double scale = canvas.getCanvasScale();

        lineUnit = (LineUnit) mouseEvent.getSource();

        if (gesture == Gesture.MOVE) {
            int deltaX = CanvasGrid.toUnit((mouseEvent.getSceneX() - mouseAnchorLocation.getX()) / scale);
            int deltaY = CanvasGrid.toUnit((mouseEvent.getSceneY() - mouseAnchorLocation.getY()) / scale);

            int newUnitX = previousPivotLocation.getUnitX() + deltaX;
            int newUnitY = previousPivotLocation.getUnitY() + deltaY;

            lineUnit.move(newUnitX, newUnitY);
        }

        // if (mouseEvent.isControlDown() && gesture == Gesture.COPY) {
        //     int mouseUnitX = CanvasGrid.toUnit(mouseEvent.getX());
        //     int mouseUnitY = CanvasGrid.toUnit(mouseEvent.getY());
        //
        //     lineUnit.dragCopy(mouseUnitX, mouseUnitY, unitDragData);
        // }

        mouseEvent.consume();
    };

    private EventHandler<MouseEvent> onMouseReleased = mouseEvent -> {
        unitDragData.reset();
        gesture = Gesture.MOVE;
    };

    private EventHandler<MouseEvent> onMouseClicked = mouseEvent -> {
        if (lineUnit != null) {
            canvas.focusUnit(lineUnit);
        }

        lineUnit = null;
    };
}
