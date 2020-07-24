package seedu.canvas.component.canvas.unit;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.CanvasHandle;
import seedu.canvas.component.canvas.CanvasMode;
import seedu.canvas.component.canvas.DragData;
import seedu.canvas.component.canvas.Gesture;
import seedu.canvas.component.canvas.TheCanvas;

public class ModelMoveHandle extends CanvasHandle {


    private TheCanvas canvas = TheCanvas.getInstance();
    private DragData unitDragData = new DragData();
    private Point2D pivotLocation = null;
    private Gesture gesture = Gesture.MOVE;

    private ModelUnit unit;

    public ModelMoveHandle(ModelUnit unit) {
        super(null);
        this.unit = unit;

        initialiseStyle();
        initialiseEvents();
    }

    private void initialiseStyle() {
        focus();

        centerXProperty().bind(unit.xProperty().add(unit.widthProperty().divide(2)));
        centerYProperty().bind(unit.yProperty().add(unit.heightProperty().divide(2)));

        setCursor(Cursor.MOVE);
    }

    private void initialiseEvents() {
        addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown()) {

                if (canvas.getCanvasMode() == CanvasMode.SHAPE) {
                    canvas.changeMode(CanvasMode.POINT);
                }

                mouseLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                pivotLocation = new Point2D(unit.getCanvasStartX(), unit.getCanvasStartY());

                if (mouseEvent.isControlDown()) {
                    unitDragData.getCopiedCanvasNodes().add(unit);
                    gesture = Gesture.COPY;
                }

                unit.interactSingle();
                CanvasGrid.showGridPoints();

                unit.requestFocus();
                mouseEvent.consume();
            }
        });

        setOnMouseReleased(mouseEvent -> {
            mouseLocation = null;
            gesture = Gesture.MOVE;

            if (unitDragData.getRecentCanvasNode() != null) {
                unitDragData.getRecentCanvasNode().focusSingle();
                unitDragData.reset();
            } else {
                unit.focusSingle();
            }

            CanvasGrid.hideGridPoints();

            mouseEvent.consume();
        });

        setOnMouseDragged(mouseEvent -> {
            if (gesture == Gesture.MOVE) {
                double deltaX = canvas.toScale(mouseEvent.getSceneX() - mouseLocation.getX());
                double deltaY = canvas.toScale(mouseEvent.getSceneY() - mouseLocation.getY());

                double newStartX = pivotLocation.getX() + deltaX;
                double newStartY = pivotLocation.getY() + deltaY;

                unit.move(newStartX, newStartY);
            }

            if (mouseEvent.isControlDown() && gesture == Gesture.COPY) {
                unit.dragCopy(mouseEvent.getX(), mouseEvent.getY(), unitDragData);
            }

            mouseEvent.consume();
        });
    }
}
