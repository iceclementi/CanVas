package seedu.canvas.component.canvas.unit;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.*;

public class LineMoveHandle extends CanvasHandle {

    private TheCanvas canvas = TheCanvas.getInstance();
    private DragData unitDragData = new DragData();
    private Point2D pivotLocation = null;
    private Gesture gesture = Gesture.MOVE;

    private LineUnit unit;

    public LineMoveHandle(LineUnit unit) {
        super(null);
        this.unit = unit;

        initialiseStyle();
        initialiseEvents();
    }

    private void initialiseStyle() {
        focus();

        centerXProperty().bind(unit.startXProperty().add(unit.endXProperty()).divide(2));
        centerYProperty().bind(unit.startYProperty().add(unit.endYProperty()).divide(2));

        setCursor(Cursor.MOVE);
    }

    private void initialiseEvents() {
        addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown()) {

                if (canvas.getCanvasMode() == CanvasMode.SHAPE) {
                    canvas.changeMode(CanvasMode.POINT);
                }

                mouseLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                pivotLocation = new Point2D(unit.getCanvasStartX(), unit.getCanvasStartY());

                if (mouseEvent.isControlDown()) {
                    unitDragData.getCopiedCanvasNodes().add(unit);
                    gesture = Gesture.COPY;
                }

                unit.interactSingle();
                CanvasGrid.showGridPoints();

                unit.requestFocus();
                mouseEvent.consume();
            }
        });

        setOnMouseReleased(mouseEvent -> {
            mouseLocation = null;

            unitDragData.reset();
            gesture = Gesture.MOVE;

            unit.focusSingle();
            CanvasGrid.hideGridPoints();
        });

        setOnMouseDragged(mouseEvent -> {
            if (gesture == Gesture.MOVE) {
                double deltaX = canvas.toScale(mouseEvent.getSceneX() - mouseLocation.getX());
                double deltaY = canvas.toScale(mouseEvent.getSceneY() - mouseLocation.getY());

                double newStartX = pivotLocation.getX() + deltaX;
                double newStartY = pivotLocation.getY() + deltaY;

                unit.move(newStartX, newStartY);
            }

            if (mouseEvent.isControlDown() && gesture == Gesture.COPY) {
                unit.dragCopy(mouseEvent.getX(), mouseEvent.getY(), unitDragData);
            }

            mouseEvent.consume();
        });
    }
}
