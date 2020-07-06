package seedu.canvas.component.canvas.unit;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.Direction;
import seedu.canvas.component.canvas.TheCanvas;

public class ModelResizeHandle extends CanvasHandle {

    private TheCanvas canvas = TheCanvas.getInstance();

    private ModelUnit unit;

    public ModelResizeHandle(ModelUnit unit, Direction location) {
        super(location);
        this.unit = unit;

        initialiseStyle();
        initialiseEvents();
    }

    private void initialiseStyle() {
        focus();

        switch (location) {
        case NORTHWEST:
            centerXProperty().bind(unit.xProperty());
            centerYProperty().bind(unit.yProperty());
            setCursor(Cursor.NW_RESIZE);
            break;
        case NORTHEAST:
            centerXProperty().bind(unit.xProperty().add(unit.widthProperty()));
            centerYProperty().bind(unit.yProperty());
            setCursor(Cursor.NE_RESIZE);
            break;
        case SOUTHWEST:
            centerXProperty().bind(unit.xProperty());
            centerYProperty().bind(unit.yProperty().add(unit.heightProperty()));
            setCursor(Cursor.SW_RESIZE);
            break;
        case SOUTHEAST:
            centerXProperty().bind(unit.xProperty().add(unit.widthProperty()));
            centerYProperty().bind(unit.yProperty().add(unit.heightProperty()));
            setCursor(Cursor.SE_RESIZE);
            break;
        default:
            System.out.println("ModelResizeHandle: Invalid location!");
            break;
        }
    }

    private void initialiseEvents() {
        addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown()) {
                unit.interact();
                CanvasGrid.showGridPoints();

                mouseLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                previousHandleLocation = findPreviousHandleLocation();

                unit.requestFocus();
                mouseEvent.consume();
            }
        });

        setOnMouseReleased(mouseEvent -> {
            unit.focus();
            CanvasGrid.hideGridPoints();

            mouseLocation = null;
            previousHandleLocation = null;
        });

        switch (location) {
        case NORTHWEST:
            setOnMouseDragged(this::onDragNW);
            break;
        case NORTHEAST:
            setOnMouseDragged(this::onDragNE);
            break;
        case SOUTHWEST:
            setOnMouseDragged(this::onDragSW);
            break;
        case SOUTHEAST:
            setOnMouseDragged(this::onDragSE);
            break;
        default:
            System.out.println("ModelResizeHandle: Invalid location!");
            break;
        }
    }

    private void onDragNW(MouseEvent mouseEvent) {
        if (mouseLocation == null || previousHandleLocation == null) {
            return;
        }

        int deltaX = CanvasGrid.toUnit(canvas.toScale(mouseEvent.getSceneX() - mouseLocation.getX()));
        int deltaY = CanvasGrid.toUnit(canvas.toScale(mouseEvent.getSceneY() - mouseLocation.getY()));
        updateWestX(deltaX);
        updateNorthY(deltaY);
    }

    private void onDragNE(MouseEvent mouseEvent) {
        if (mouseLocation == null || previousHandleLocation == null) {
            return;
        }

        int deltaX = CanvasGrid.toUnit(canvas.toScale(mouseEvent.getSceneX() - mouseLocation.getX()));
        int deltaY = CanvasGrid.toUnit(canvas.toScale(mouseEvent.getSceneY() - mouseLocation.getY()));
        updateEastX(deltaX);
        updateNorthY(deltaY);
    }

    private void onDragSW(MouseEvent mouseEvent) {
        if (mouseLocation == null || previousHandleLocation == null) {
            return;
        }

        int deltaX = CanvasGrid.toUnit(canvas.toScale(mouseEvent.getSceneX() - mouseLocation.getX()));
        int deltaY = CanvasGrid.toUnit(canvas.toScale(mouseEvent.getSceneY() - mouseLocation.getY()));
        updateWestX(deltaX);
        updateSouthY(deltaY);
    }

    private void onDragSE(MouseEvent mouseEvent) {
        if (mouseLocation == null || previousHandleLocation == null) {
            return;
        }

        int deltaX = CanvasGrid.toUnit(canvas.toScale(mouseEvent.getSceneX() - mouseLocation.getX()));
        int deltaY = CanvasGrid.toUnit(canvas.toScale(mouseEvent.getSceneY() - mouseLocation.getY()));
        updateEastX(deltaX);
        updateSouthY(deltaY);
    }

    private void updateWestX(int deltaX) {
        int newUnitX = CanvasGrid.clamp(
                previousHandleLocation.getUnitX() + deltaX,
                0, unit.getUnitX() + unit.getUnitWidth() - 1);

        int newUnitWidth = unit.getUnitWidth() + (unit.getUnitX() - newUnitX);

        if (newUnitWidth != unit.getUnitWidth() && newUnitWidth > 0) {
            unit.setUnitX(newUnitX);
            unit.setUnitWidth(newUnitWidth);
        }
    }

    private void updateEastX(int deltaX) {
        int newUnitWidth = CanvasGrid.clamp(
                previousHandleLocation.getUnitX() - unit.getUnitX() + deltaX,
                1, CanvasGrid.MAX_X - unit.getUnitX());

        if (newUnitWidth != unit.getUnitWidth() && newUnitWidth > 0) {
            unit.setUnitWidth(newUnitWidth);
        }
    }

    private void updateNorthY(int deltaY) {
        int newUnitY = CanvasGrid.clamp(
                previousHandleLocation.getUnitY() + deltaY,
                0, unit.getUnitY() + unit.getUnitHeight() - 1);

        int newUnitHeight = unit.getUnitHeight() + (unit.getUnitY() - newUnitY);

        if (newUnitHeight != unit.getUnitHeight() && newUnitHeight > 0) {
            unit.setUnitY(newUnitY);
            unit.setUnitHeight(newUnitHeight);
        }
    }

    private void updateSouthY(int deltaY) {
        int newUnitHeight = CanvasGrid.clamp(
                previousHandleLocation.getUnitY() - unit.getUnitY() + deltaY,
                1, CanvasGrid.MAX_Y - unit.getUnitY());

        if (newUnitHeight != unit.getUnitHeight() && newUnitHeight > 0) {
            unit.setUnitHeight(newUnitHeight);
        }
    }

    private UnitPoint findPreviousHandleLocation() {
        switch (location) {
        case NORTHWEST:
            return new UnitPoint(unit.getUnitX(), unit.getUnitY());
        case NORTHEAST:
            return new UnitPoint(unit.getUnitX() + unit.getUnitWidth(), unit.getUnitY());
        case SOUTHWEST:
            return new UnitPoint(unit.getUnitX(), unit.getUnitY() + unit.getUnitHeight());
        case SOUTHEAST:
            return new UnitPoint(unit.getUnitX() + unit.getUnitWidth(), unit.getUnitY() + unit.getUnitHeight());
        default:
            System.out.println("ModelResizeHandle: Invalid location!");
            return null;
        }
    }
}
