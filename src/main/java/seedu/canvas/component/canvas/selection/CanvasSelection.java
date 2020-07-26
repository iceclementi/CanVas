package seedu.canvas.component.canvas.selection;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.CanvasNode;
import seedu.canvas.component.canvas.Direction;
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

    public CanvasSelection(double x, double y, double width, double height) {
        super(x, y, width, height);

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
        canvas.interactSingle(this);
        interactMultiple();
    }

    public void focusSingle() {
        canvas.interactSingle(this);
        focusMultiple();
    }

    public void unfocus() {
        unfocusMultiple();
        canvas.removeNode(this);
    }

    public void interactMultiple() {
        selectedCanvasNodes.forEach(CanvasNode::interactMultiple);
    }

    public void focusMultiple() {
        selectedCanvasNodes.forEach(CanvasNode::focusMultiple);
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

    public CanvasSelection copy() {
        CanvasSelection copiedSelection = new CanvasSelection(getX(), getY(), getWidth(), getHeight());

        for (CanvasNode selectedCanvasNode : selectedCanvasNodes) {
            CanvasNode copiedCanvasNode = selectedCanvasNode.copy();
            copiedSelection.selectedCanvasNodes.add(copiedCanvasNode);
        }

        copiedSelection.setVisible(false);
        copiedSelection.isCompacted = true;

        return copiedSelection;
    }

    public void dragCopy(double mouseLocationX, double mouseLocationY, DragData dragData) {
        if (dragData.getCopiedCanvasNodes().isEmpty()) {
            return;
        }

        if (dragData.getCopyDirection() == null) {
            dragData.setCopyDirection(computeDirection(this, mouseLocationX, mouseLocationY));

            // Mouse is still within unit
            if (dragData.getCopyDirection() == null) {
                return;
            }
        }

        switch (dragData.getCopyDirection()) {
        case WEST:
            dragCopyWest(mouseLocationX, mouseLocationY, dragData);
            break;
        case EAST:
            dragCopyEast(mouseLocationX, mouseLocationY, dragData);
            break;
        case NORTH:
            dragCopyNorth(mouseLocationX, mouseLocationY, dragData);
            break;
        case SOUTH:
            dragCopySouth(mouseLocationX, mouseLocationY, dragData);
            break;
        default:
            break;
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

    private void dragCopyWest(double mouseLocationX, double mouseLocationY, DragData dragData) {
        ArrayList<CanvasNode> copiedSelections = dragData.getCopiedCanvasNodes();

        CanvasSelection targetSelection = (CanvasSelection) dragData.getRecentCanvasNode();
        Direction currentCopyDirection = computeDirection(targetSelection, mouseLocationX, mouseLocationY);

        if (currentCopyDirection == Direction.WEST) {
            addSelection(copiedSelections, targetSelection,
                    targetSelection.getCanvasStartX() - targetSelection.getWidth(),
                    targetSelection.getCanvasStartY(),
                    targetSelection.getWidth(), targetSelection.getHeight());
        } else if (currentCopyDirection == Direction.EAST) {
            removeSelection(targetSelection, dragData);
        }
    }

    private void dragCopyEast(double mouseLocationX, double mouseLocationY, DragData dragData) {
        ArrayList<CanvasNode> copiedSelections = dragData.getCopiedCanvasNodes();

        CanvasSelection targetSelection = (CanvasSelection) dragData.getRecentCanvasNode();
        Direction currentCopyDirection = computeDirection(targetSelection, mouseLocationX, mouseLocationY);

        if (currentCopyDirection == Direction.EAST) {
            addSelection(copiedSelections, targetSelection,
                    targetSelection.getCanvasEndX(),
                    targetSelection.getCanvasStartY(),
                    targetSelection.getWidth(), targetSelection.getHeight());
        } else if (currentCopyDirection == Direction.WEST) {
            removeSelection(targetSelection, dragData);
        }
    }

    private void dragCopyNorth(double mouseLocationX, double mouseLocationY, DragData dragData) {
        ArrayList<CanvasNode> copiedSelections = dragData.getCopiedCanvasNodes();

        CanvasSelection targetSelection = (CanvasSelection) dragData.getRecentCanvasNode();
        Direction currentCopyDirection = computeDirection(targetSelection, mouseLocationX, mouseLocationY);

        if (currentCopyDirection == Direction.NORTH) {
            addSelection(copiedSelections, targetSelection,
                    targetSelection.getCanvasStartX(),
                    targetSelection.getCanvasStartY() - targetSelection.getHeight(),
                    targetSelection.getWidth(), targetSelection.getHeight());
        } else if (currentCopyDirection == Direction.SOUTH) {
            removeSelection(targetSelection, dragData);
        }
    }

    private void dragCopySouth(double mouseLocationX, double mouseLocationY, DragData dragData) {
        ArrayList<CanvasNode> copiedSelections = dragData.getCopiedCanvasNodes();

        CanvasSelection targetSelection = (CanvasSelection) dragData.getRecentCanvasNode();
        Direction currentCopyDirection = computeDirection(targetSelection, mouseLocationX, mouseLocationY);

        if (currentCopyDirection == Direction.SOUTH) {
            addSelection(copiedSelections, targetSelection,
                    targetSelection.getCanvasStartX(),
                    targetSelection.getCanvasStartY() + targetSelection.getHeight(),
                    targetSelection.getWidth(), targetSelection.getHeight());
        } else if (currentCopyDirection == Direction.NORTH) {
            removeSelection(targetSelection, dragData);
        }
    }

    private void addSelection(ArrayList<CanvasNode> copiedSelections, CanvasSelection targetSelection,
            double newX, double newY, double newWidth, double newHeight) {

        if (canvas.isWithinCanvas(newX, newY, newX + newWidth, newY + newHeight)) {

            CanvasSelection newSelection = targetSelection.copy();
            newSelection.move(newX, newY);
            newSelection.interactSingle();

            copiedSelections.add(newSelection);
        }
    }

    private void removeSelection(CanvasSelection selection, DragData dragData) {
        ArrayList<CanvasNode> copiedSelections = dragData.getCopiedCanvasNodes();

        if (copiedSelections.size() > 1) {
            copiedSelections.remove(selection);
            selection.deleteSelection();
            canvas.removeNode(selection);
        }

        if (copiedSelections.size() == 1) {
            dragData.setCopyDirection(null);
        }

        dragData.getRecentCanvasNode().interactSingle();
    }

    private static Direction computeDirection(CanvasSelection selection, double mouseLocationX, double mouseLocationY) {
        if (CanvasGrid.toUnit(mouseLocationX - selection.getCanvasStartX()) < 0) {
            return Direction.WEST;
        } else if (CanvasGrid.toUnit(mouseLocationX - selection.getCanvasEndX()) > 0) {
            return Direction.EAST;
        } else if (CanvasGrid.toUnit(mouseLocationY - selection.getCanvasStartY()) < 0) {
            return Direction.NORTH;
        } else if (CanvasGrid.toUnit(mouseLocationY - selection.getCanvasEndY()) > 0) {
            return Direction.SOUTH;
        } else {
            // Within the selection
            return null;
        }
    }
}
