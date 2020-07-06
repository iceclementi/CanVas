package seedu.canvas.component.canvas.selection;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.CanvasNode;
import seedu.canvas.component.canvas.Direction;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.component.canvas.text.TextBoxMoveHandle;
import seedu.canvas.component.canvas.text.TextBoxResizeHandle;

import java.util.ArrayList;

public class SelectionWrapper extends Rectangle {

    private TheCanvas canvas = TheCanvas.getInstance();

    private SelectionMoveHandle moveHandle;
    private SelectionResizeHandle resizeHandleNW;
    private SelectionResizeHandle resizeHandleNE;
    private SelectionResizeHandle resizeHandleSW;
    private SelectionResizeHandle resizeHandleSE;

    private ArrayList<CanvasNode> selectedCanvasNodes = null;

    public SelectionWrapper() {
        super();

        moveHandle = new SelectionMoveHandle(this);
        resizeHandleNW = new SelectionResizeHandle(this, Direction.NORTHWEST);
        resizeHandleNE = new SelectionResizeHandle(this, Direction.NORTHEAST);
        resizeHandleSW = new SelectionResizeHandle(this, Direction.SOUTHWEST);
        resizeHandleSE = new SelectionResizeHandle(this, Direction.SOUTHEAST);

        // canvas.addSelection or node?

        initialiseStyle();
        initialiseEvents();
    }

    public void compact() {
        selectedCanvasNodes = canvas.getMultiSelectedCanvasNodes(
                getX(), getY(), getX() + getWidth(), getY() + getHeight()
        );

        double newStartX = CanvasGrid.WIDTH;
        double newStartY = CanvasGrid.HEIGHT;
        double newEndX = 0;
        double newEndY = 0;

        for (CanvasNode selectedCanvasNode : selectedCanvasNodes) {
            newStartX = Math.min(newStartX, selectedCanvasNode.getCanvasStartX());
            newStartY = Math.min(newStartY, selectedCanvasNode.getCanvasStartY());
            newEndX = Math.min(newEndX, selectedCanvasNode.getCanvasEndX());
            newEndY = Math.min(newEndY, selectedCanvasNode.getCanvasEndY());
        }

        setX(newStartX);
        setY(newStartY);
        setWidth(newEndX - newStartX);
        setHeight(newEndY - newStartY);
    }

    public void interact() {

    }

    public void focus() {

    }

    public void unfocus() {

    }

    public void unfocusMultiple() {
        for (CanvasNode selectedCanvasNode : selectedCanvasNodes) {
            // unfocus multiple
        }
    }

    private void interactMultiple() {
        for (CanvasNode selectedCanvasNode : selectedCanvasNodes) {
            // interact multiple
        }
    }

    private void focusMultiple() {
        for (CanvasNode selectedCanvasNode : selectedCanvasNodes) {
            // focus multiple
        }
    }

    private void initialiseStyle() {
        setStroke(Color.GREY);
        setStrokeWidth(2);
        setFill(Color.LIGHTGREY);

        setOpacity(0.6);
    }

    private void initialiseEvents() {

    }
}
