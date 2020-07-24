package seedu.canvas.component.canvas.draw;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.CanvasNode;
import seedu.canvas.component.canvas.Direction;
import seedu.canvas.component.canvas.DragData;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.util.CanvasMath;

import java.util.ArrayList;

public class Drawing implements CanvasNode {

    private TheCanvas canvas = TheCanvas.getInstance();
    private DrawingCanvas drawingCanvas = DrawingCanvas.getInstance();

    private ArrayList<DrawingStroke> drawingStrokes = new ArrayList<>();
    private Color lineColour = canvas.getLineColour();

    private DrawingWrapper wrapper = null;

    private DoubleProperty startX = new SimpleDoubleProperty(Double.MAX_VALUE);
    private DoubleProperty startY = new SimpleDoubleProperty(Double.MAX_VALUE);
    private DoubleProperty endX = new SimpleDoubleProperty(Double.MIN_VALUE);
    private DoubleProperty endY = new SimpleDoubleProperty(Double.MIN_VALUE);

    public Drawing() {
    }

    public double getStartX() {
        return startX.get();
    }

    public DoubleProperty startXProperty() {
        return startX;
    }

    public double getStartY() {
        return startY.get();
    }

    public DoubleProperty startYProperty() {
        return startY;
    }

    public double getEndX() {
        return endX.get();
    }

    public DoubleProperty endXProperty() {
        return endX;
    }

    public double getEndY() {
        return endY.get();
    }

    public DoubleProperty endYProperty() {
        return endY;
    }

    public double getWidth() {
        return getEndX() - getStartX();
    }

    public double getHeight() {
        return getEndY() - getStartY();
    }

    public double getCanvasStartX() {
        return getStartX();
    }

    public double getCanvasStartY() {
        return getStartY();
    }

    public double getCanvasEndX() {
        return getEndX();
    }

    public double getCanvasEndY() {
        return getEndY();
    }

    public ArrayList<Node> getGroup() {
        ArrayList<Node> drawingUnit = new ArrayList<>(drawingStrokes);
        drawingUnit.addAll(wrapper.getGroup());

        return drawingUnit;
    }

    public void addStroke(DrawingStroke stroke) {
        if (!isWithinCanvas(stroke.getStartX(), stroke.getStartY(), stroke.getEndX(), stroke.getEndY())) {
            return;
        }

        drawingStrokes.add(stroke);
        drawingCanvas.addStroke(stroke);

        updateBounds(stroke.getStartX(), stroke.getStartY(), stroke.getEndX(), stroke.getEndY());
    }

    public void finishDrawing() {
        wrapper = new DrawingWrapper(this);
        drawingCanvas.reset();
        canvas.addNode(this);

        wrapper.unfocus();
        unfocus();
    }

    public ArrayList<DrawingStroke> getDrawingStrokes() {
        return drawingStrokes;
    }

    public void interactSingle() {
        canvas.interactSingle(this);
        wrapper.interactSingle();
    }

    public void focusSingle() {
        canvas.interactSingle(this);
        wrapper.focusSingle();
    }

    public void interactMultiple() {
        wrapper.interactMultiple();
    }

    public void focusMultiple() {
        wrapper.focusMultiple();
    }

    public void unfocus() {
        wrapper.unfocus();
    }

    public void move(double newX, double newY) {
        double finalNewX = CanvasMath.clamp(newX, 0, CanvasGrid.WIDTH - getWidth());
        double finalNewY = CanvasMath.clamp(newY, 0, CanvasGrid.HEIGHT - getHeight());

        double deltaX = finalNewX - getStartX();
        double deltaY = finalNewY - getStartY();

        for (DrawingStroke stroke : drawingStrokes) {
            shift(stroke, deltaX, deltaY);
        }

        startX.set(finalNewX);
        startY.set(finalNewY);
        endX.set(endX.get() + deltaX);
        endY.set(endY.get() + deltaY);
    }

    public void colourLine(Color colour) {
        drawingStrokes.forEach(stroke -> stroke.colour(colour));
        lineColour = colour;
    }

    public void colourFill(Color colour) {
    }

    public void dragCopy(double mouseLocationX, double mouseLocationY, DragData dragData) {
        ArrayList<CanvasNode> copiedUnits = dragData.getCopiedCanvasNodes();

        if (copiedUnits.isEmpty()) {
            return;
        }

        if (dragData.getCopyDirection() == null) {
            dragData.setCopyDirection(computeDirection(this, mouseLocationX, mouseLocationY));

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

    public void toFront() {
        drawingStrokes.forEach(DrawingStroke::toFront);
    }

    private void updateBounds(double strokeStartX, double strokeStartY, double strokeEndX, double strokeEndY) {
        double newStartX = Math.min(strokeStartX, strokeEndX);
        double newEndX = Math.max(strokeStartX, strokeEndX);
        if (newStartX < startX.get()) {
            startX.set(newStartX);
        }
        if (newEndX > endX.get()) {
            endX.set(newEndX);
        }

        double newStartY = Math.min(strokeStartY, strokeEndY);
        double newEndY = Math.max(strokeStartY, strokeEndY);
        if (newStartY < startY.get()) {
            startY.set(newStartY);
        }
        if (newEndY > endY.get()) {
            endY.set(newEndY);
        }
    }

    private void shift(DrawingStroke stroke, double deltaX, double deltaY) {
        stroke.setStartX(stroke.getStartX() + deltaX);
        stroke.setStartY(stroke.getStartY() + deltaY);
        stroke.setEndX(stroke.getEndX() + deltaX);
        stroke.setEndY(stroke.getEndY() + deltaY);
    }

    private void dragCopyWest(double mouseLocationX, double mouseLocationY, DragData dragData) {
        ArrayList<CanvasNode> copiedUnits = dragData.getCopiedCanvasNodes();

        Drawing targetDrawing = (Drawing) copiedUnits.get(copiedUnits.size() - 1);
        Direction currentCopyDirection = computeDirection(targetDrawing, mouseLocationX, mouseLocationY);

        double widthOffset = Math.max(targetDrawing.getWidth(), CanvasGrid.OFFSET);

        if (currentCopyDirection == Direction.WEST) {
            addDrawing(copiedUnits, targetDrawing, -widthOffset, 0);
        } else if (currentCopyDirection == Direction.EAST) {
            removeDrawing(targetDrawing, dragData);
        }
    }

    private void dragCopyEast(double mouseLocationX, double mouseLocationY, DragData dragData) {
        ArrayList<CanvasNode> copiedUnits = dragData.getCopiedCanvasNodes();

        Drawing targetDrawing = (Drawing) copiedUnits.get(copiedUnits.size() - 1);
        Direction currentCopyDirection = computeDirection(targetDrawing, mouseLocationX, mouseLocationY);

        double widthOffset = Math.max(targetDrawing.getWidth(), CanvasGrid.OFFSET);

        if (currentCopyDirection == Direction.EAST) {
            addDrawing(copiedUnits, targetDrawing, widthOffset, 0);
        } else if (currentCopyDirection == Direction.WEST) {
            removeDrawing(targetDrawing, dragData);
        }
    }

    private void dragCopyNorth(double mouseLocationX, double mouseLocationY, DragData dragData) {
        ArrayList<CanvasNode> copiedUnits = dragData.getCopiedCanvasNodes();

        Drawing targetDrawing = (Drawing) copiedUnits.get(copiedUnits.size() - 1);
        Direction currentCopyDirection = computeDirection(targetDrawing, mouseLocationX, mouseLocationY);

        double heightOffset = Math.max(targetDrawing.getHeight(), CanvasGrid.OFFSET);

        if (currentCopyDirection == Direction.NORTH) {
            addDrawing(copiedUnits, targetDrawing, 0, -heightOffset);
        } else if (currentCopyDirection == Direction.SOUTH) {
            removeDrawing(targetDrawing, dragData);
        }
    }

    private void dragCopySouth(double mouseLocationX, double mouseLocationY, DragData dragData) {
        ArrayList<CanvasNode> copiedUnits = dragData.getCopiedCanvasNodes();

        Drawing targetDrawing = (Drawing) copiedUnits.get(copiedUnits.size() - 1);
        Direction currentCopyDirection = computeDirection(targetDrawing, mouseLocationX, mouseLocationY);

        double heightOffset = Math.max(targetDrawing.getHeight(), CanvasGrid.OFFSET);

        if (currentCopyDirection == Direction.SOUTH) {
            addDrawing(copiedUnits, targetDrawing, 0, heightOffset);
        } else if (currentCopyDirection == Direction.NORTH) {
            removeDrawing(targetDrawing, dragData);
        }
    }

    private Direction computeDirection(Drawing drawing, double mouseLocationX, double mouseLocationY) {
        if (CanvasGrid.toUnit(mouseLocationX - drawing.getCanvasStartX()) < 0) {
            return Direction.WEST;
        } else if (CanvasGrid.toUnit(mouseLocationX - drawing.getCanvasEndX()) > 0) {
            return Direction.EAST;
        } else if (CanvasGrid.toUnit(mouseLocationY - drawing.getCanvasStartY()) < 0) {
            return Direction.NORTH;
        } else if (CanvasGrid.toUnit(mouseLocationY - drawing.getCanvasEndY()) > 0) {
            return Direction.SOUTH;
        } else {
            // Within the drawing
            return null;
        }
    }

    private void addDrawing(ArrayList<CanvasNode> copiedDrawings, Drawing targetDrawing, double deltaX, double deltaY) {
        if (isWithinCanvas(targetDrawing.getStartX() + deltaX, targetDrawing.getStartY() + deltaY,
                targetDrawing.getEndX() + deltaX, targetDrawing.getEndY() + deltaY)) {

            Drawing newDrawing = new Drawing();

            for (DrawingStroke stroke : targetDrawing.drawingStrokes) {
                DrawingStroke newStroke = new DrawingStroke(
                        newDrawing,
                        stroke.getStartX() + deltaX,
                        stroke.getStartY() + deltaY,
                        stroke.getEndX() + deltaX,
                        stroke.getEndY() + deltaY
                );

                newDrawing.addStroke(newStroke);
            }

            newDrawing.finishDrawing();
            newDrawing.colourLine(targetDrawing.lineColour);
            newDrawing.interactSingle();

            copiedDrawings.add(newDrawing);
        }
    }

    private void removeDrawing(Drawing targetDrawing, DragData dragData) {
        ArrayList<CanvasNode> copiedDrawings = dragData.getCopiedCanvasNodes();

        if (copiedDrawings.size() > 1) {
            copiedDrawings.remove(targetDrawing);
            canvas.removeNode(targetDrawing);
        }

        if (copiedDrawings.size() == 1) {
            dragData.setCopyDirection(null);
        }

        copiedDrawings.get(copiedDrawings.size() - 1).interactSingle();
    }

    private boolean isWithinCanvas(double startX, double startY, double endX, double endY) {
        return (startX >= 0 && endX <= CanvasGrid.WIDTH)
                && (startY >= 0 && endY <= CanvasGrid.HEIGHT);
    }
}
