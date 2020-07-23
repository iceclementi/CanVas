package seedu.canvas.component.canvas.selection;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.CanvasNode;
import seedu.canvas.component.canvas.Direction;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.component.canvas.unit.CanvasHandle;
import seedu.canvas.component.canvas.unit.CanvasUnit;
import seedu.canvas.util.CanvasMath;

import java.util.ArrayList;
import java.util.Arrays;

public class SelectionWrapper extends Rectangle implements CanvasNode {

    private TheCanvas canvas = TheCanvas.getInstance();

    private double startX = CanvasGrid.WIDTH;
    private double startY = CanvasGrid.HEIGHT;
    private double endX = 0;
    private double endY = 0;

    private double combinedDeltaX = 0;
    private double combinedDeltaY = 0;

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

    public double getMinX() {
        return getCanvasStartX() - startX;
    }

    public double getMinY() {
        return getCanvasStartY() - startY;
    }

    public double getMaxX() {
        return CanvasGrid.WIDTH - getWidth() + getCanvasEndX() - endX;
    }

    public double getMaxY() {
        return CanvasGrid.HEIGHT - getHeight() + getCanvasEndY() - endY;
    }

    public void interactSingle() {
        toFront();
        canvas.interactSingle(this);
        interactMultiple();
        getHandles().forEach(CanvasHandle::interact);
    }

    public void focusSingle() {
        toFront();
        canvas.interactSingle(this);
        focusMultiple();
        getHandles().forEach(CanvasHandle::focus);
    }

    public void unfocus() {
        unfocusMultiple();
        canvas.removeNode(this);
    }

    public void colourLine(Color lineColour) {
        selectedCanvasNodes.forEach(selectedCanvasNode -> selectedCanvasNode.colourLine(lineColour));
    }

    public void colourFill(Color fillColour) {
        selectedCanvasNodes.forEach(selectedCanvasNode -> selectedCanvasNode.colourFill(fillColour));
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
            endX = Math.max(endX, selectedCanvasNode.getCanvasEndX());
            endY = Math.max(endY, selectedCanvasNode.getCanvasEndY());
        }
    }

    public void move(double newX, double newY) {
        // System.out.println(String.format("%s %s %s %s", getMinX(), getMaxX(), getMinY(), getMaxY()));

        double newFinalX = CanvasMath.clamp(newX, getMinX(), getMaxX());
        double newFinalY = CanvasMath.clamp(newY, getMinY(), getMaxY());

        double deltaX = newFinalX - getX();
        double deltaY = newFinalY - getY();

        setX(newFinalX);
        setY(newFinalY);

        startX += deltaX;
        endX += deltaX;
        startY += deltaY;
        endY += deltaY;

        combinedDeltaX += deltaX;
        combinedDeltaY += deltaY;

        int unitDeltaX = CanvasGrid.toUnit(combinedDeltaX) - CanvasGrid.toUnit(combinedDeltaX - deltaX);
        int unitDeltaY = CanvasGrid.toUnit(combinedDeltaY) - CanvasGrid.toUnit(combinedDeltaY - deltaY);

        for (CanvasNode selectedCanvasNode : selectedCanvasNodes) {
            if (selectedCanvasNode instanceof CanvasUnit) {
                double newNodeX = selectedCanvasNode.getCanvasStartX() + CanvasGrid.toActual(unitDeltaX);
                double newNodeY = selectedCanvasNode.getCanvasStartY() + CanvasGrid.toActual(unitDeltaY);
                selectedCanvasNode.move(newNodeX, newNodeY);
            } else {
                double newNodeX = selectedCanvasNode.getCanvasStartX() + deltaX;
                double newNodeY = selectedCanvasNode.getCanvasStartY() + deltaY;
                selectedCanvasNode.move(newNodeX, newNodeY);
            }
        }
    }

    public void reset() {
        combinedDeltaX = 0;
        combinedDeltaY = 0;
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
