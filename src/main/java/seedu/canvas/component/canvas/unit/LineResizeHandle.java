package seedu.canvas.component.canvas.unit;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.Direction;
import seedu.canvas.component.canvas.TheCanvas;

public class LineResizeHandle extends ResizeHandle {

    private LineUnit unit;

    public LineResizeHandle(LineUnit unit, Direction location) {
        super(location);
        this.unit = unit;

        initialiseStyle();
        initialiseEvents();
    }

    private void initialiseStyle() {
        focus();

        switch (location) {
        case WEST:
            centerXProperty().bind(unit.startXProperty());
            centerYProperty().bind(unit.startYProperty());
            break;
        case EAST:
            centerXProperty().bind(unit.endXProperty());
            centerYProperty().bind(unit.endYProperty());
            break;
        default:
            System.out.println("LineResizeHandle: Invalid location!");
            break;
        }
    }

    private void initialiseEvents() {
        setOnMouseEntered(mouseEvent -> updateCursor());

        setOnMouseExited(mouseEvent -> {
            if (!isResizing) {
                setCursor(Cursor.DEFAULT);
            }
        });

        setOnMousePressed(mouseEvent -> {
            mouseLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            previousHandleLocation = findPreviousHandleLocation();
            isResizing = true;
        });

        setOnMouseReleased(mouseEvent -> {
            mouseLocation = null;
            previousHandleLocation = null;
            isResizing = false;
        });

        switch (location) {
        case WEST:
            setOnMouseDragged(this::onDragWest);
            break;
        case EAST:
            setOnMouseDragged(this::onDragEast);
            break;
        default:
            System.out.println("LineResizeHandle: Invalid location!");
            break;
        }
    }

    private void onDragWest(MouseEvent mouseEvent) {
        if (mouseLocation == null || previousHandleLocation == null) {
            return;
        }

        updateCursor();

        double scale = TheCanvas.getInstance().getCanvasScale();

        int deltaX = CanvasGrid.toUnit((mouseEvent.getSceneX() - mouseLocation.getX()) / scale);
        int deltaY = CanvasGrid.toUnit((mouseEvent.getSceneY() - mouseLocation.getY()) / scale);

        updateWest(deltaX, deltaY);
    }

    private void onDragEast(MouseEvent mouseEvent) {
        if (mouseLocation == null || previousHandleLocation == null) {
            return;
        }

        updateCursor();

        double scale = TheCanvas.getInstance().getCanvasScale();

        int deltaX = CanvasGrid.toUnit((mouseEvent.getSceneX() - mouseLocation.getX()) / scale);
        int deltaY = CanvasGrid.toUnit((mouseEvent.getSceneY() - mouseLocation.getY()) / scale);

        updateEast(deltaX, deltaY);
    }

    private void updateWest(int deltaX, int deltaY) {
        int newUnitStartX = unit.getUnitStartX() + deltaX;
        unit.setUnitStartX(CanvasGrid.clamp(newUnitStartX, CanvasGrid.MIN_X, CanvasGrid.MAX_X));

        int newUnitStartY = unit.getUnitStartY() + deltaY;
        unit.setUnitStartY(CanvasGrid.clamp(newUnitStartY, CanvasGrid.MIN_Y, CanvasGrid.MAX_Y));
    }

    private void updateEast(int deltaX, int deltaY) {
        int newUnitEndX = unit.getUnitEndX() + deltaX;
        unit.setUnitEndX(CanvasGrid.clamp(newUnitEndX, CanvasGrid.MIN_X, CanvasGrid.MAX_X));

        int newUnitEndY = unit.getUnitEndY() + deltaY;
        unit.setUnitEndY(CanvasGrid.clamp(newUnitEndY, CanvasGrid.MIN_Y, CanvasGrid.MAX_Y));
    }

    private Direction computeDirection() {
        if (unit.getUnitStartX() < unit.getUnitEndX()) {
            if (unit.getUnitStartY() < unit.getUnitEndY()) {
                return Direction.NORTHWEST;
            } else if (unit.getUnitStartY() > unit.getUnitEndY()) {
                return Direction.NORTHEAST;
            } else {
                return Direction.EAST;
            }
        } else if (unit.getUnitStartX() > unit.getUnitEndX()) {
            if (unit.getUnitStartY() < unit.getUnitEndY()) {
                return Direction.NORTHEAST;
            } else if (unit.getUnitStartY() > unit.getUnitEndY()) {
                return Direction.NORTHWEST;
            } else {
                return Direction.EAST;
            }
        } else {
            return Direction.NORTH;
        }
    }

    private UnitPoint findPreviousHandleLocation() {
        switch (location) {
        case WEST:
            return new UnitPoint(unit.getUnitStartX(), unit.getUnitStartY());
        case EAST:
            return new UnitPoint(unit.getUnitEndX(), unit.getUnitEndY());
        default:
            System.out.println("LineResizeHandle: Invalid location!");
            return null;
        }
    }

    private void updateCursor() {
        switch (computeDirection()) {
        case NORTHWEST:
            setCursor(Cursor.NW_RESIZE);
            break;
        case NORTHEAST:
            setCursor(Cursor.NE_RESIZE);
            break;
        case EAST:
            setCursor(Cursor.H_RESIZE);
            break;
        case NORTH:
            setCursor(Cursor.V_RESIZE);
            break;
        default:
            System.out.println("LineResizeHandle: Invalid location!");
            break;
        }
    }
}
