package seedu.canvas.component.canvas;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import seedu.canvas.component.canvas.text.TextBox;
import seedu.canvas.component.canvas.unit.AnchorLineUnit;
import seedu.canvas.component.canvas.unit.GroupLineUnit;
import seedu.canvas.component.canvas.unit.LineUnit;
import seedu.canvas.component.canvas.unit.ModelUnit;

public class CanvasEventManager {

    private TheCanvas canvas = TheCanvas.getInstance();
    private DragData unitDragData = new DragData();
    private DragData canvasDragData = new DragData();

    private ModelUnit modelUnit;
    private LineUnit lineUnit;
    private TextBox textBox;

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

            // Checks if focus is on direct canvas
            if (canvas.getCanvasMode() == CanvasMode.POINT) {
                // if (mouseEvent.getClickCount() == 2) {
                //     canvas.focusNone();
                // }
                canvas.focusNone();
                return;
            }

            if (canvas.getCanvasMode() == CanvasMode.TEXT) {
                double x = mouseEvent.getX();
                double y = mouseEvent.getY();

                textBox = new TextBox(x, y);

                return;
            }

            if (canvas.getCanvasMode() != CanvasMode.SHAPE) {
                return;
            }

            GridPoint targetPoint = CanvasGrid.getTargetGridPoint();

            if (targetPoint == null) {
                return;
            }

            double x = targetPoint.getCenterX();
            double y = targetPoint.getCenterY();

            switch (canvas.getUnitShape()) {
            case MODEL:
                modelUnit = new ModelUnit(CanvasGrid.toUnit(x), CanvasGrid.toUnit(y), 0, 0);
                modelUnit.interact();
                break;
            case LINE:
                lineUnit = new LineUnit(CanvasGrid.toUnit(x), CanvasGrid.toUnit(y),
                        CanvasGrid.toUnit(x), CanvasGrid.toUnit(y));
                lineUnit.interact();
                break;
            case ANCHOR_LINE:
                lineUnit = new AnchorLineUnit(CanvasGrid.toUnit(x), CanvasGrid.toUnit(y),
                        CanvasGrid.toUnit(x), CanvasGrid.toUnit(y));
                lineUnit.interact();
                break;
            case GROUP_LINE:
                lineUnit = new GroupLineUnit(CanvasGrid.toUnit(x), CanvasGrid.toUnit(y),
                        CanvasGrid.toUnit(x), CanvasGrid.toUnit(y));
                lineUnit.interact();
                break;
            default:
                return;
            }

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

            if (canvas.getCanvasMode() == CanvasMode.TEXT) {
                if (textBox == null) {
                    return;
                }

                textBox.scale(mouseEvent.getX(), mouseEvent.getY());
                return;
            }

            if (canvas.getCanvasMode() != CanvasMode.SHAPE) {
                return;
            }

            if (modelUnit == null && lineUnit == null) {
                return;
            }

            switch (canvas.getUnitShape()) {
            case MODEL:
                int newUnitWidth =
                        CanvasGrid.toUnit(canvas.toScale(mouseEvent.getSceneX() - unitDragData.getMouseAnchorX()));
                int newUnitHeight =
                        CanvasGrid.toUnit(canvas.toScale(mouseEvent.getSceneY() - unitDragData.getMouseAnchorY()));

                modelUnit.scale(newUnitWidth, newUnitHeight);
                break;
            case LINE:
            case ANCHOR_LINE:
            case GROUP_LINE:
                int newUnitEndX = CanvasGrid.toUnit(mouseEvent.getX());
                int newUnitEndY = CanvasGrid.toUnit(mouseEvent.getY());

                lineUnit.scale(newUnitEndX, newUnitEndY);
                break;
            default:
                return;
            }

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
            if (modelUnit.getUnitWidth() == 0 || modelUnit.getUnitHeight() == 0) {
                canvas.removeNode(modelUnit);
            } else {
                modelUnit.unfocus();
            }
        }

        if (lineUnit != null) {
            if ((lineUnit.getUnitStartX() == lineUnit.getUnitEndX())
                    && (lineUnit.getUnitStartY() == lineUnit.getUnitEndY())) {
                canvas.removeNode(lineUnit);
            } else {
                lineUnit.unfocus();
            }
        }

        if (textBox != null) {
            if (textBox.getWidth() < TextBox.MIN_WIDTH || textBox.getHeight() < TextBox.MIN_HEIGHT) {
                textBox.setDefaultSize();
            }

            textBox.focus();
            canvas.changeMode(CanvasMode.POINT);
        }

        modelUnit = null;
        lineUnit = null;
        textBox = null;
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
