package seedu.canvas.component.canvas;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import seedu.canvas.component.canvas.unit.ModelUnit;

public class CanvasEventManager {

    private TheCanvas canvas = TheCanvas.getInstance();
    private DragData unitDragData = new DragData();
    private DragData canvasDragData = new DragData();
    private ModelUnit modelUnit;

    private static final double MIN_SCALE = 0.5d;
    private static final double MAX_SCALE = 4.0d;
    private static final double SCROLL_DELTA = 0.25d;

    /**
     * Constructor for the event manager of the canvas scene.
     */
    public CanvasEventManager() {
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

    public EventHandler<ScrollEvent> getOnScroll() {
        return onScroll;
    }

    private EventHandler<MouseEvent> onMousePressed = mouseEvent -> {

        if (mouseEvent.isPrimaryButtonDown()) {

            // Checks if focus is not on model unit
            if (canvas.getCanvasMode() == CanvasMode.POINT) {
                int mouseUnitX = CanvasGrid.toUnit(mouseEvent.getX());
                int mouseUnitY = CanvasGrid.toUnit(mouseEvent.getY());

                if (!canvas.isIntersectUnit(mouseUnitX, mouseUnitY)) {
                    canvas.focusUnit(null);
                }
            }

            if (canvas.getCanvasMode() != CanvasMode.MODEL) {
                return;
            }

            GridPoint targetPoint = CanvasGrid.getTargetGridPoint();

            if (targetPoint == null) {
                canvas.focusUnit(null);
                return;
            }

            double x = targetPoint.getCenterX();
            double y = targetPoint.getCenterY();

            modelUnit = new ModelUnit(CanvasGrid.toUnit(x), CanvasGrid.toUnit(y), 0, 0);
            modelUnit.interact();

            unitDragData.setMouseAnchorX(mouseEvent.getSceneX());
            unitDragData.setMouseAnchorY(mouseEvent.getSceneY());

        } else if (mouseEvent.isSecondaryButtonDown()) {
            canvasDragData.setMouseAnchorX(mouseEvent.getSceneX());
            canvasDragData.setMouseAnchorY(mouseEvent.getSceneY());

            canvasDragData.setTranslateAnchorX(canvas.getTranslateX());
            canvasDragData.setTranslateAnchorY(canvas.getTranslateY());
        }
    };

    private EventHandler<MouseEvent> onMouseDragged = mouseEvent -> {

        if (mouseEvent.isPrimaryButtonDown()) {
            if (canvas.getCanvasMode() != CanvasMode.MODEL) {
                return;
            }

            if (modelUnit == null) {
                return;
            }

            double scale = TheCanvas.getInstance().getCanvasScale();

            int newUnitWidth = CanvasGrid.toUnit((mouseEvent.getSceneX() - unitDragData.getMouseAnchorX()) / scale);
            int newUnitHeight = CanvasGrid.toUnit((mouseEvent.getSceneY() - unitDragData.getMouseAnchorY()) / scale);

            modelUnit.scale(newUnitWidth, newUnitHeight);

            mouseEvent.consume();
        } else if (mouseEvent.isSecondaryButtonDown()) {

            double translateDeltaX = mouseEvent.getSceneX() - canvasDragData.getMouseAnchorX();
            double translateDeltaY = mouseEvent.getSceneY() - canvasDragData.getMouseAnchorY();

            canvas.setTranslateX(canvasDragData.getTranslateAnchorX() + translateDeltaX);
            canvas.setTranslateY(canvasDragData.getTranslateAnchorY() + translateDeltaY);

            mouseEvent.consume();
        }
    };

    private EventHandler<MouseEvent> onMouseReleased = mouseEvent -> {
        if (modelUnit != null) {
            if (modelUnit.getWidth() == 0 || modelUnit.getHeight() == 0) {
                canvas.getChildren().remove(modelUnit);
            } else {
                canvas.focusUnit(modelUnit);
            }
        }

        modelUnit = null;
    };

    private EventHandler<ScrollEvent> onScroll = scrollEvent -> {
        if (!scrollEvent.isControlDown()) {
            return;
        }

        double scale = canvas.getCanvasScale();
        double previousScale = scale;

        if (scrollEvent.getDeltaY() < 0) {
            scale -= SCROLL_DELTA;
        } else {
            scale += SCROLL_DELTA;
        }

        scale = clamp(scale);

        double pivotFactor = (scale / previousScale) - 1;

        double deltaX = scrollEvent.getSceneX()
                - (canvas.getBoundsInParent().getWidth() / 2 + canvas.getBoundsInParent().getMinX());
        double deltaY = scrollEvent.getSceneY()
                - (canvas.getBoundsInParent().getHeight() / 2 + canvas.getBoundsInParent().getMinY());

        canvas.setCanvasScale(scale);
        canvas.setPivot(pivotFactor * deltaX,pivotFactor * deltaY);

        scrollEvent.consume();
    };

    private double clamp(double value) {
        return Math.min(Math.max(value, MIN_SCALE), MAX_SCALE);
    }
}
