package seedu.canvas.component.canvas.draw;

import javafx.geometry.Point2D;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.component.canvas.unit.CanvasHandle;

public class DrawingMoveHandle extends CanvasHandle {

    private Point2D previousAnchorPoint = null;

    private DrawingSelectionBox selectionBox;

    public DrawingMoveHandle(DrawingSelectionBox selectionBox) {
        super(null);
        this.selectionBox = selectionBox;

        initialiseStyle();
        initialiseEvents();
    }

    private void initialiseStyle() {
        focus();

        centerXProperty().bind(selectionBox.xProperty().add(selectionBox.widthProperty().divide(2)));
        centerYProperty().bind(selectionBox.yProperty().add(selectionBox.heightProperty().divide(2)));
    }

    private void initialiseEvents() {
        setOnMousePressed(mouseEvent -> {
            mouseLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            previousAnchorPoint = new Point2D(getCenterX(), getCenterY());
            selectionBox.getDrawing().interactSingle();
        });

        setOnMouseReleased(mouseEvent -> {
            mouseLocation = null;
            selectionBox.getDrawing().focusSingle();
        });

        setOnMouseDragged(mouseEvent -> {
            double scale = TheCanvas.getInstance().getCanvasScale();

            double deltaX = (mouseEvent.getSceneX() - mouseLocation.getX()) / scale;
            double deltaY = (mouseEvent.getSceneY() - mouseLocation.getY()) / scale;

            double newX = previousAnchorPoint.getX() + deltaX;
            double newY = previousAnchorPoint.getY() + deltaY;

            selectionBox.move(newX, newY);

            mouseEvent.consume();
        });
    }
}
