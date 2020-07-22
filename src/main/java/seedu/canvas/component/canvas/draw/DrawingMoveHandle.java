package seedu.canvas.component.canvas.draw;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.component.canvas.unit.CanvasHandle;

public class DrawingMoveHandle extends CanvasHandle {

    private Point2D previousAnchorPoint = null;

    private DrawingWrapper selectionBox;

    public DrawingMoveHandle(DrawingWrapper selectionBox) {
        super(null);
        this.selectionBox = selectionBox;

        initialiseStyle();
        initialiseEvents();
    }

    private void initialiseStyle() {
        centerXProperty().bind(selectionBox.xProperty().add(selectionBox.widthProperty().divide(2)));
        centerYProperty().bind(selectionBox.yProperty().add(selectionBox.heightProperty().divide(2)));

        setCursor(Cursor.MOVE);
    }

    private void initialiseEvents() {
        addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown()) {
                mouseLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                previousAnchorPoint = new Point2D(selectionBox.getX(), selectionBox.getY());
                selectionBox.getDrawing().interactSingle();

                mouseEvent.consume();
            }
        });

        setOnMouseReleased(mouseEvent -> {
            mouseLocation = null;
            selectionBox.getDrawing().focusSingle();
        });

        setOnMouseDragged(mouseEvent -> {
            double deltaX = TheCanvas.getInstance().toScale(mouseEvent.getSceneX() - mouseLocation.getX());
            double deltaY = TheCanvas.getInstance().toScale(mouseEvent.getSceneY() - mouseLocation.getY());

            double newX = previousAnchorPoint.getX() + deltaX;
            double newY = previousAnchorPoint.getY() + deltaY;

            selectionBox.getDrawing().move(newX, newY);

            mouseEvent.consume();
        });
    }
}
