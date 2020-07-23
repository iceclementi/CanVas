package seedu.canvas.component.canvas.selection;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.component.canvas.unit.CanvasHandle;
import seedu.canvas.component.canvas.unit.UnitPoint;

public class SelectionMoveHandle extends CanvasHandle {

    private TheCanvas canvas = TheCanvas.getInstance();
    private Point2D previousAnchorPoint = null;
    private UnitPoint unitOffset = new UnitPoint();

    private SelectionWrapper wrapper;

    public SelectionMoveHandle(SelectionWrapper wrapper) {
        super(null);
        this.wrapper = wrapper;

        initialiseStyle();
        initialiseEvents();
    }

    private void initialiseStyle() {
        centerXProperty().bind(wrapper.xProperty().add(wrapper.widthProperty().divide(2)));
        centerYProperty().bind(wrapper.yProperty().add(wrapper.heightProperty().divide(2)));

        setCursor(Cursor.MOVE);
    }

    private void initialiseEvents() {
        addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown()) {
                mouseLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                previousAnchorPoint = new Point2D(wrapper.getX(), wrapper.getY());
                wrapper.interactSingle();

                mouseEvent.consume();
            }
        });

        setOnMouseReleased(mouseEvent -> {
            mouseLocation = null;
            wrapper.focusSingle();
            wrapper.reset();
        });

        setOnMouseDragged(mouseEvent -> {
            double deltaX = canvas.toScale(mouseEvent.getSceneX() - mouseLocation.getX());
            double deltaY = canvas.toScale(mouseEvent.getSceneY() - mouseLocation.getY());

            int unitDeltaX = CanvasGrid.toUnit(deltaX);
            int unitDeltaY = CanvasGrid.toUnit(deltaY);

            double newX = previousAnchorPoint.getX() + deltaX;
            double newY = previousAnchorPoint.getY() + deltaY;

            wrapper.move(newX, newY);

            mouseEvent.consume();
        });
    }
}
