package seedu.canvas.component.canvas.draw;

import javafx.scene.shape.Line;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.TheCanvas;

import java.util.ArrayList;

public class Drawing {

    private TheCanvas canvas = TheCanvas.getInstance();
    private DrawingCanvas drawingCanvas = DrawingCanvas.getInstance();

    private ArrayList<Line> drawing = new ArrayList<>();

    private double startX = Double.MAX_VALUE;
    private double startY = Double.MAX_VALUE;
    private double endX = Double.MIN_VALUE;
    private double endY = Double.MIN_VALUE;

    public Drawing() {
    }

    public ArrayList<Line> getDrawing() {
        return drawing;
    }

    public void addStroke(Line stroke) {
        if (!isWithinCanvas(stroke.getStartX(), stroke.getStartY(), stroke.getEndX(), stroke.getEndY())) {
            return;
        }

        stroke.setStroke(canvas.getLineColour());
        stroke.setStrokeWidth(1.5);

        drawing.add(stroke);
        drawingCanvas.addStroke(stroke);

        updateBounds(stroke.getStartX(), stroke.getStartY(), stroke.getEndX(), stroke.getEndY());
    }

    public void finishDrawing() {
        drawingCanvas.addDrawing(this);
    }

    private void updateBounds(double strokeStartX, double strokeStartY, double strokeEndX, double strokeEndY) {
        double newStartX = Math.min(strokeStartX, strokeEndX);
        double newEndX = Math.max(strokeStartX, strokeEndX);
        if (newStartX < startX) {
            startX = newStartX;
        }
        if (newEndX > endX) {
            endX = newEndX;
        }

        double newStartY = Math.min(strokeStartY, strokeEndY);
        double newEndY = Math.max(strokeStartY, strokeEndY);
        if (newStartY < startY) {
            startY = newStartY;
        }
        if (newEndY > endY) {
            endY = newEndY;
        }
    }

    private boolean isWithinCanvas(double strokeStartX, double strokeStartY, double strokeEndX, double strokeEndY) {
        return (strokeStartX >= 0 && strokeEndX <= CanvasGrid.WIDTH)
                && (strokeStartY >= 0 && strokeEndY <= CanvasGrid.HEIGHT);
    }
}
