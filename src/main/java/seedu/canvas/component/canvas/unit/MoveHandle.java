package seedu.canvas.component.canvas.unit;

import javafx.geometry.Point2D;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.TheCanvas;

public class MoveHandle extends CanvasHandle {

    private TheCanvas canvas = TheCanvas.getInstance();

    private LineUnit unit;

    public MoveHandle(LineUnit unit) {
        super(null);
        this.unit = unit;

        initialiseStyle();
        initialiseEvents();
    }

    private void initialiseStyle() {
        focus();

        centerXProperty().bind(unit.startXProperty().add(unit.endXProperty()).divide(2));
        centerYProperty().bind(unit.startYProperty().add(unit.endYProperty()).divide(2));
    }

    private void initialiseEvents() {
        setOnMousePressed(mouseEvent -> {
            mouseLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            previousHandleLocation = new UnitPoint(unit.getUnitStartX(), unit.getUnitStartY());

            unit.interact();
            CanvasGrid.showGridPoints();
        });

        setOnMouseReleased(mouseEvent -> {
            mouseLocation = null;

            unit.focus();
            CanvasGrid.hideGridPoints();
        });

        setOnMouseDragged(mouseEvent -> {
            int deltaX = CanvasGrid.toUnit(canvas.toScale(mouseEvent.getSceneX() - mouseLocation.getX()));
            int deltaY = CanvasGrid.toUnit(canvas.toScale(mouseEvent.getSceneY() - mouseLocation.getY()));

            int newUnitStartX = previousHandleLocation.getUnitX() + deltaX;
            int newUnitStartY = previousHandleLocation.getUnitY() + deltaY;

            unit.move(newUnitStartX, newUnitStartY);
        });
    }
}
