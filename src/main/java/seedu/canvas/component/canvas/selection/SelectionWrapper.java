package seedu.canvas.component.canvas.selection;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.CanvasNode;
import seedu.canvas.component.canvas.Direction;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.component.canvas.unit.CanvasHandle;
import seedu.canvas.util.CanvasMath;

import java.util.ArrayList;
import java.util.Arrays;

public class SelectionWrapper extends Rectangle implements CanvasNode {

    private TheCanvas canvas = TheCanvas.getInstance();

    private double startX = CanvasGrid.WIDTH;
    private double startY = CanvasGrid.HEIGHT;
    private double endX = 0;
    private double endY = 0;

    private SelectionMoveHandle moveHandle;
    private SelectionResizeHandle resizeHandleNW;
    private SelectionResizeHandle resizeHandleNE;
    private SelectionResizeHandle resizeHandleSW;
    private SelectionResizeHandle resizeHandleSE;

    private ArrayList<CanvasNode> selectedCanvasNodes = new ArrayList<>();

    public SelectionWrapper(double x, double y) {
        setX(x);
        setY(y);

        moveHandle = new SelectionMoveHandle(this);
        resizeHandleNW = new SelectionResizeHandle(this, Direction.NORTHWEST);
        resizeHandleNE = new SelectionResizeHandle(this, Direction.NORTHEAST);
        resizeHandleSW = new SelectionResizeHandle(this, Direction.SOUTHWEST);
        resizeHandleSE = new SelectionResizeHandle(this, Direction.SOUTHEAST);

        canvas.addNode(this);

        initialiseStyle();
        initialiseEvents();
    }

    public ArrayList<Node> getGroup() {
        ArrayList<Node> group = new ArrayList<>();
        group.add(this);
        group.addAll(getHandles());

        return group;
    }

    public double getCanvasStartX() {
        return getX();
    }

    public double getCanvasStartY() {
        return getY();
    }

    public double getCanvasEndX() {
        return getX() + getWidth();
    }

    public double getCanvasEndY() {
        return getY() + getHeight();
    }

    public void interactSingle() {
        toFront();
        canvas.interactSingle(this);
        getHandles().forEach(CanvasHandle::interact);

        interactMultiple();
    }

    public void focusSingle() {
        toFront();
        canvas.interactSingle(this);
        getHandles().forEach(CanvasHandle::focus);

        focusMultiple();
    }

    public void unfocus() {
        unfocusMultiple();
        canvas.removeNode(this);
    }

    public void colourLine(Color lineColour) {
    }

    public void colourFill(Color fillColour) {
    }

    public void unfocusMultiple() {
        selectedCanvasNodes.forEach(CanvasNode::unfocus);
    }

    public void interactMultiple() {
        selectedCanvasNodes.forEach(CanvasNode::interactMultiple);
        toFront();
    }

    public void focusMultiple() {
        selectedCanvasNodes.forEach(CanvasNode::focusMultiple);
        toFront();
    }

    public void scale(double endX, double endY) {
        double newWidth = CanvasMath.clamp(endX - getX(), 0, CanvasGrid.WIDTH - getX());
        double newHeight = CanvasMath.clamp(endY - getY(), 0, CanvasGrid.HEIGHT - getY());

        setWidth(newWidth);
        setHeight(newHeight);
    }

    public void compact() {
        selectedCanvasNodes = canvas.getMultiSelectedCanvasNodes(
                getX(), getY(), getX() + getWidth(), getY() + getHeight()
        );

        if (selectedCanvasNodes.isEmpty()) {
            canvas.removeNode(this);
        } else {
            focusSingle();
        }

        for (CanvasNode selectedCanvasNode : selectedCanvasNodes) {
            startX = Math.min(startX, selectedCanvasNode.getCanvasStartX());
            startY = Math.min(startY, selectedCanvasNode.getCanvasStartY());
            endX = Math.min(endX, selectedCanvasNode.getCanvasEndX());
            endY = Math.min(endY, selectedCanvasNode.getCanvasEndY());
        }
    }

    public void deleteSelection() {
        selectedCanvasNodes.forEach(selectedCanvasNode -> canvas.removeNode(selectedCanvasNode));
    }

    private void initialiseStyle() {
        setStroke(Color.GREY);
        setStrokeWidth(1);
        setFill(Color.LIGHTGREY);

        setOpacity(0.5);

        getHandles().forEach(CanvasHandle::unfocus);
    }

    private void initialiseEvents() {

    }

    private ArrayList<CanvasHandle> getHandles() {
        return new ArrayList<>(
                Arrays.asList(moveHandle, resizeHandleNW, resizeHandleNE, resizeHandleSW, resizeHandleSE));
    }
}
