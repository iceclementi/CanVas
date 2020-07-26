package seedu.canvas.component.canvas.selection;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.CanvasNode;
import seedu.canvas.component.canvas.DragData;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.component.canvas.unit.CanvasUnit;
import seedu.canvas.util.CanvasMath;

import java.util.ArrayList;

public class CanvasSelection extends Rectangle implements CanvasNode {

    private TheCanvas canvas = TheCanvas.getInstance();

    private Point2D pivotPoint;

    private double combinedDeltaX = 0;
    private double combinedDeltaY = 0;

    private boolean isCompacted = false;

    private ArrayList<CanvasNode> selectedCanvasNodes = new ArrayList<>();

    public CanvasSelection(double x, double y) {
        setX(x);
        setY(y);

        pivotPoint = new Point2D(x, y);

        canvas.addNode(this);

        initialiseStyle();
        initialiseEvents();
    }

    public boolean isCompacted() {
        return isCompacted;
    }

    public ArrayList<Node> getGroup() {
        ArrayList<Node> group = new ArrayList<>();
        group.add(this);

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
        interactMultiple();
    }

    public void focusSingle() {
        toFront();
        canvas.interactSingle(this);
        focusMultiple();
    }

    public void unfocus() {
        unfocusMultiple();
        canvas.removeNode(this);
    }

    public void interactMultiple() {
        selectedCanvasNodes.forEach(CanvasNode::interactMultiple);
        toFront();
    }

    public void focusMultiple() {
        selectedCanvasNodes.forEach(CanvasNode::focusMultiple);
        toFront();
    }

    public void unfocusMultiple() {
        selectedCanvasNodes.forEach(CanvasNode::unfocus);
    }

    public void colourLine(Color lineColour) {
        selectedCanvasNodes.forEach(selectedCanvasNode -> selectedCanvasNode.colourLine(lineColour));
    }

    public void colourFill(Color fillColour) {
        selectedCanvasNodes.forEach(selectedCanvasNode -> selectedCanvasNode.colourFill(fillColour));
    }

    public void scale(double endX, double endY) {
        if (pivotPoint.getX() <= endX) {
            scaleEast(endX);
        } else {
            scaleWest(endX);
        }
        if (pivotPoint.getY() <= endY) {
            scaleSouth(endY);
        } else {
            scaleNorth(endY);
        }
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

        double newStartX = getCanvasEndX();
        double newStartY = getCanvasEndY();
        double newEndX = getCanvasStartX();
        double newEndY = getCanvasStartY();

        for (CanvasNode selectedCanvasNode : selectedCanvasNodes) {
            newStartX = Math.min(newStartX, selectedCanvasNode.getCanvasStartX());
            newStartY = Math.min(newStartY, selectedCanvasNode.getCanvasStartY());
            newEndX = Math.max(newEndX, selectedCanvasNode.getCanvasEndX());
            newEndY = Math.max(newEndY, selectedCanvasNode.getCanvasEndY());
        }

        setX(newStartX);
        setY(newStartY);
        setWidth(newEndX - newStartX);
        setHeight(newEndY - newStartY);

        setVisible(false);
        isCompacted = true;
    }

    public void move(double newX, double newY) {
        // System.out.println(String.format("%s %s %s %s", getMinX(), getMaxX(), getMinY(), getMaxY()));

        double newFinalX = CanvasMath.clamp(newX, 0, CanvasGrid.WIDTH - getWidth());
        double newFinalY = CanvasMath.clamp(newY, 0, CanvasGrid.HEIGHT - getHeight());

        double deltaX = newFinalX - getX();
        double deltaY = newFinalY - getY();

        combinedDeltaX += deltaX;
        combinedDeltaY += deltaY;

        setX(newFinalX);
        setY(newFinalY);

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

    public void dragCopy(double mouseLocationX, double mouseLocationY, DragData dragData) {

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

        setOpacity(0.4);
    }

    private void initialiseEvents() {
    }


    private void scaleEast(double endX) {
        double newWidth = CanvasMath.clamp(endX - pivotPoint.getX(), 0, CanvasGrid.WIDTH - pivotPoint.getX());

        setWidth(newWidth);
    }

    private void scaleSouth(double endY) {
        double newHeight = CanvasMath.clamp(endY - pivotPoint.getY(), 0, CanvasGrid.HEIGHT - pivotPoint.getY());

        setHeight(newHeight);
    }

    private void scaleWest(double endX) {
        double newX = CanvasMath.clamp(endX, 0, pivotPoint.getX());
        double newWidth = CanvasMath.clamp(pivotPoint.getX() - newX, 0, pivotPoint.getX());

        setX(newX);
        setWidth(newWidth);
    }

    private void scaleNorth(double endY) {
        double newY = CanvasMath.clamp(endY, 0, pivotPoint.getY());
        double newHeight = CanvasMath.clamp(pivotPoint.getY() - newY, 0, pivotPoint.getY());

        setY(newY);
        setHeight(newHeight);
    }
}
