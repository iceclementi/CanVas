package seedu.canvas.component.canvas.draw;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.TheCanvas;

import java.util.ArrayList;

public class Drawing {

    private TheCanvas canvas = TheCanvas.getInstance();
    private DrawingCanvas drawingCanvas = DrawingCanvas.getInstance();

    private ArrayList<DrawingStroke> drawing = new ArrayList<>();

    private DrawingSelectionBox selectionBox = null;

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

    public ArrayList<Node> getDrawing() {
        ArrayList<Node> drawingUnit = new ArrayList<>(drawing);
        drawingUnit.addAll(selectionBox.getGroup());

        return drawingUnit;
    }

    public void addStroke(DrawingStroke stroke) {
        if (!isWithinCanvas(stroke.getStartX(), stroke.getStartY(), stroke.getEndX(), stroke.getEndY())) {
            return;
        }

        drawing.add(stroke);
        drawingCanvas.addStroke(stroke);

        updateBounds(stroke.getStartX(), stroke.getStartY(), stroke.getEndX(), stroke.getEndY());
    }

    public void finishDrawing() {
        selectionBox = new DrawingSelectionBox(this);
        drawingCanvas.reset();
        canvas.addDrawing(this);

        selectionBox.unfocus();
        unfocus();
    }

    public void interact() {
        canvas.interactDrawing(this);
        toFront();
        selectionBox.interact();
    }

    public void focus() {
        canvas.focusDrawing(this);
        toFront();
        selectionBox.focus();
    }

    public void unfocus() {
        selectionBox.unfocus();
    }

    public void move(double deltaX, double deltaY) {

        for (DrawingStroke stroke : drawing) {
            shift(stroke, deltaX, deltaY);
        }

        startX.set(startX.get() + deltaX);
        startY.set(startY.get() + deltaY);
        endX.set(endX.get() + deltaX);
        endY.set(endY.get() + deltaY);
    }

    public void colour(Color colour) {
        drawing.forEach(stroke -> stroke.colour(colour));
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


    private boolean isWithinCanvas(double strokeStartX, double strokeStartY, double strokeEndX, double strokeEndY) {
        return (strokeStartX >= 0 && strokeEndX <= CanvasGrid.WIDTH)
                && (strokeStartY >= 0 && strokeEndY <= CanvasGrid.HEIGHT);
    }

    private void toFront() {
        drawing.forEach(DrawingStroke::toFront);
    }
}
