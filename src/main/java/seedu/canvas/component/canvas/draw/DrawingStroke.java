package seedu.canvas.component.canvas.draw;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import seedu.canvas.component.canvas.TheCanvas;

public class DrawingStroke extends Line {

    private TheCanvas canvas = TheCanvas.getInstance();

    private Drawing drawing;

    public DrawingStroke(Drawing drawing, double startX, double startY, double endX, double endY) {
        super(startX, startY, endX, endY);
        this.drawing = drawing;

        initialiseStyle();
        initialiseEvents();
    }

    public Drawing getDrawing() {
        return drawing;
    }

    public void colour(Color colour) {
        setStroke(colour);
    }

    private void initialiseStyle() {
        setStroke(canvas.getLineColour());
        setStrokeWidth(1.5);
        setStrokeLineCap(StrokeLineCap.ROUND);

        updateCursor();
    }

    private void initialiseEvents() {
        DrawingStrokeEventManager drawingStrokeEventManager = new DrawingStrokeEventManager();

        addEventFilter(MouseEvent.MOUSE_PRESSED, drawingStrokeEventManager.getOnMousePressed());
        addEventFilter(MouseEvent.MOUSE_DRAGGED, drawingStrokeEventManager.getOnMouseDragged());
        addEventFilter(MouseEvent.MOUSE_RELEASED, drawingStrokeEventManager.getOnMouseReleased());

        canvas.getCanvasModeProperty().addListener(observable -> updateCursor());
    }

    private void updateCursor() {
        switch (canvas.getCanvasMode()) {
        case POINT:
            setCursor(Cursor.HAND);
            break;
        case SHAPE:
        case TEXT:
            setCursor(Cursor.CROSSHAIR);
            break;
        default:
            setCursor(Cursor.DEFAULT);
            break;
        }
    }
}
