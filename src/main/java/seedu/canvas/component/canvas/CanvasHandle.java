package seedu.canvas.component.canvas;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import seedu.canvas.component.canvas.selection.CanvasSelection;
import seedu.canvas.component.canvas.unit.UnitPoint;

public abstract class CanvasHandle extends Circle {

    private TheCanvas canvas = TheCanvas.getInstance();

    protected Point2D mouseLocation = null;
    protected UnitPoint previousHandleLocation = null;
    protected DragData dragData = new DragData();
    protected Point2D pivotLocation = null;
    protected CanvasSelection selection = null;
    protected Gesture gesture = Gesture.MOVE;

    protected Direction location;

    public CanvasHandle(Direction location) {
        super(5);

        this.location = location;

        initialiseStyle();
    }

    public void interactSingle() {
        show(Color.CORNFLOWERBLUE);
    }

    public void focusSingle() {
        show(Color.LIGHTGREEN);
    }

    public void interactMultiple() {
        show(Color.CADETBLUE);
    }

    public void focusMultiple() {
        show(Color.GREEN);
    }

    public void unfocus() {
        setVisible(false);
    }

    protected void addMousePressEvent(CanvasNode canvasNode, MouseEvent mouseEvent) {
        selection = canvas.getSelection();
        mouseLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());

        if (selection == null) {
            pivotLocation = new Point2D(canvasNode.getCanvasStartX(), canvasNode.getCanvasStartY());

            if (mouseEvent.isControlDown()) {
                dragData.getCopiedCanvasNodes().add(canvasNode);
                gesture = Gesture.COPY;
            }

            canvasNode.interactSingle();
        } else {
            pivotLocation = new Point2D(selection.getCanvasStartX(), selection.getCanvasStartY());
            selection.interactMultiple();

            if (mouseEvent.isControlDown()) {
                dragData.getCopiedCanvasNodes().add(selection);
                gesture = Gesture.COPY;
            }
        }
    }

    protected void addMouseDragEvent(CanvasNode canvasNode, MouseEvent mouseEvent) {
        if (gesture == Gesture.MOVE) {
            double deltaX = canvas.toScale(mouseEvent.getSceneX() - mouseLocation.getX());
            double deltaY = canvas.toScale(mouseEvent.getSceneY() - mouseLocation.getY());

            double newStartX = pivotLocation.getX() + deltaX;
            double newStartY = pivotLocation.getY() + deltaY;

            if (selection == null) {
                canvasNode.move(newStartX, newStartY);
            } else {
                selection.move(newStartX, newStartY);
            }
        }

        if (mouseEvent.isControlDown() && gesture == Gesture.COPY) {
            if (selection == null) {
                canvasNode.dragCopy(mouseEvent.getX(), mouseEvent.getY(), dragData);
            } else {
                selection.dragCopy(mouseEvent.getX(), mouseEvent.getY(), dragData);
            }
        }
    }

    protected void addMouseReleaseEvent(CanvasNode canvasNode) {
        if (dragData.getRecentCanvasNode() != null) {
            dragData.getRecentCanvasNode().focusSingle();
            dragData.reset();
        } else {
            if (selection == null) {
                canvasNode.focusSingle();
            } else {
                selection.focusMultiple();
                selection.reset();
            }
        }

        mouseLocation = null;
        reset();
    }

    private void initialiseStyle() {
        setOpacity(0.8);
    }

    private void show(Color handleColour) {
        setFill(handleColour);

        setVisible(true);
        toFront();
    }

    private void reset() {
        dragData = new DragData();
        pivotLocation = null;
        selection = null;
        gesture = Gesture.MOVE;
    }
}
