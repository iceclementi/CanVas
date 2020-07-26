package seedu.canvas.component.canvas.selection;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.CanvasHandle;
import seedu.canvas.component.canvas.CanvasMode;
import seedu.canvas.component.canvas.CanvasNode;
import seedu.canvas.component.canvas.Gesture;
import seedu.canvas.component.canvas.TheCanvas;

public class SelectionMoveHandle extends CanvasHandle {

    private TheCanvas canvas = TheCanvas.getInstance();
    private Point2D pivotLocation = null;
    private Gesture gesture = Gesture.MOVE;

    private CanvasNode canvasNode;

    public SelectionMoveHandle(CanvasNode canvasNode) {
        super(null);
        this.canvasNode = canvasNode;
    }

    private void initialiseStyle() {
        setCursor(Cursor.MOVE);
    }

    private void initialiseEvents() {
        addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown()) {

                if (canvas.getCanvasMode() != CanvasMode.POINT) {
                    return;
                }

                CanvasSelection selection = canvas.getSelection();

                mouseLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                pivotLocation = new Point2D(selection.getCanvasStartX(), selection.getCanvasStartY());

                if (mouseEvent.isControlDown()) {
                     // Multiple drag copy
                    gesture = Gesture.COPY;
                }

                selection.interactMultiple();
                CanvasGrid.showGridPoints();

                mouseEvent.consume();
            }
        });

        setOnMouseReleased(mouseEvent -> {
            mouseLocation = null;
            gesture = Gesture.MOVE;

            CanvasSelection selection = canvas.getSelection();
            selection.focusMultiple();

            CanvasGrid.hideGridPoints();

            mouseEvent.consume();
        });

        setOnMouseDragged(mouseEvent -> {
            CanvasSelection selection = canvas.getSelection();

            if (gesture == Gesture.MOVE) {
                double deltaX = canvas.toScale(mouseEvent.getSceneX() - mouseLocation.getX());
                double deltaY = canvas.toScale(mouseEvent.getSceneY() - mouseLocation.getY());

                double newStartX = pivotLocation.getX() + deltaX;
                double newStartY = pivotLocation.getY() + deltaY;

                selection.move(newStartX, newStartY);
            }

            if (mouseEvent.isControlDown() && gesture == Gesture.COPY) {
                // Multiple drag copy
            }

            mouseEvent.consume();
        });
    }
}
