package seedu.canvas.component.canvas.unit;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import seedu.canvas.component.canvas.CanvasComponent;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.Direction;
import seedu.canvas.component.canvas.DragData;
import seedu.canvas.component.canvas.TheCanvas;

import java.util.ArrayList;
import java.util.Arrays;

public class LineUnit extends Line implements CanvasComponent {

    protected TheCanvas canvas = TheCanvas.getInstance();

    protected IntegerProperty unitStartX = new SimpleIntegerProperty();
    protected IntegerProperty unitStartY = new SimpleIntegerProperty();
    protected IntegerProperty unitEndX = new SimpleIntegerProperty();
    protected IntegerProperty unitEndY = new SimpleIntegerProperty();

    private LineResizeHandle resizeHandleWest = new LineResizeHandle(this, Direction.WEST);
    private LineResizeHandle resizeHandleEast = new LineResizeHandle(this, Direction.EAST);
    private LineMoveHandle moveHandle = new LineMoveHandle(this);

    public LineUnit(int unitStartX, int unitStartY, int unitEndX, int unitEndY) {
        super();

        initialiseOther();

        initialiseStyle();

        this.unitStartX.set(unitStartX);
        this.unitStartY.set(unitStartY);
        this.unitEndX.set(unitEndX);
        this.unitEndY.set(unitEndY);

        canvas.addUnit(this);

        initialiseEvents();
    }

    public int getUnitStartX() {
        return unitStartX.get();
    }

    public void setUnitStartX(int unitStartX) {
        this.unitStartX.set(unitStartX);
    }

    public int getUnitStartY() {
        return unitStartY.get();
    }

    public void setUnitStartY(int unitStartY) {
        this.unitStartY.set(unitStartY);
    }

    public int getUnitEndX() {
        return unitEndX.get();
    }

    public void setUnitEndX(int unitEndX) {
        this.unitEndX.set(unitEndX);
    }

    public int getUnitEndY() {
        return unitEndY.get();
    }

    public void setUnitEndY(int unitEndY) {
        this.unitEndY.set(unitEndY);
    }

    public ArrayList<Node> getUnitGroup() {
        return new ArrayList<>(Arrays.asList(this, resizeHandleWest, resizeHandleEast, moveHandle));
    }

    public void interact() {
        canvas.interactUnit(this);

        toFront();

        resizeHandleWest.interact();
        resizeHandleEast.interact();
        moveHandle.interact();
    }

    public void focus() {
        canvas.focusUnit(this);

        toFront();

        resizeHandleWest.focus();
        resizeHandleEast.focus();
        moveHandle.focus();
    }

    public void unfocus() {
        resizeHandleWest.unfocus();
        resizeHandleEast.unfocus();
        moveHandle.unfocus();
    }

    public void scale(int newUnitEndX, int newUnitEndY) {
        unitEndX.set(clamp(newUnitEndX, CanvasGrid.MIN_X, CanvasGrid.MAX_X));
        unitEndY.set(clamp(newUnitEndY, CanvasGrid.MIN_Y, CanvasGrid.MAX_Y));
    }

    public void move(int newUnitStartX, int newUnitStartY) {

        int width = unitEndX.get() - unitStartX.get();

        if (width >= 0) {
            unitStartX.set(clamp(newUnitStartX, 0, CanvasGrid.MAX_X - width));
        } else {
            unitStartX.set(clamp(newUnitStartX, -width, CanvasGrid.MAX_X));
        }

        unitEndX.set(unitStartX.get() + width);

        int height = unitEndY.get() - unitStartY.get();

        if (height >= 0) {
            unitStartY.set(clamp(newUnitStartY, 0, CanvasGrid.MAX_Y - height));
        } else {
            unitStartY.set(clamp(newUnitStartY, -height, CanvasGrid.MAX_Y));
        }

        unitEndY.set(unitStartY.get() + height);
    }

    public void dragCopy(int mouseUnitX, int mouseUnitY, DragData dragData) {
        ArrayList<Node> copiedUnits = dragData.getCopiedUnits();
        Direction copyDirection = dragData.getCopyDirection();

        if (copiedUnits.isEmpty()) {
            return;
        }

        if (copyDirection == null) {
            copyDirection = computeDirection(this, mouseUnitX, mouseUnitY);

            // Mouse is still within unit
            if (copyDirection == null) {
                return;
            }

            dragData.setCopyDirection(copyDirection);
        }

        switch (copyDirection) {
        case WEST:
            dragCopyWest(mouseUnitX, mouseUnitY, dragData);
            break;
        case EAST:
            dragCopyEast(mouseUnitX, mouseUnitY, dragData);
            break;
        case NORTH:
            dragCopyNorth(mouseUnitX, mouseUnitY, dragData);
            break;
        case SOUTH:
            dragCopySouth(mouseUnitX, mouseUnitY, dragData);
            break;
        default:
            break;
        }
    }

    public void colour(Paint lineColour) {
        setStroke(lineColour);
    }

    protected void colour() {
        setStroke(canvas.getLineColour());
    }

    protected void initialiseOther() {
    }

    private void initialiseStyle() {
        colour();
        setStrokeWidth(3);
        setStrokeLineCap(StrokeLineCap.ROUND);

        startXProperty().bind(unitStartX.multiply(CanvasGrid.OFFSET));
        startYProperty().bind(unitStartY.multiply(CanvasGrid.OFFSET));
        endXProperty().bind(unitEndX.multiply((CanvasGrid.OFFSET)));
        endYProperty().bind(unitEndY.multiply(CanvasGrid.OFFSET));

        setCursor(Cursor.HAND);

        unfocus();
    }

    private void initialiseEvents() {
        LineUnitEventManager lineUnitEventManager = new LineUnitEventManager();

        addEventFilter(MouseEvent.MOUSE_PRESSED, lineUnitEventManager.getOnMousePressed());
        addEventFilter(MouseEvent.MOUSE_DRAGGED, lineUnitEventManager.getOnMouseDragged());
        addEventFilter(MouseEvent.MOUSE_RELEASED, lineUnitEventManager.getOnMouseReleased());
    }

    private void dragCopyWest(int mouseUnitX, int mouseUnitY, DragData dragData) {
        ArrayList<Node> copiedUnits = dragData.getCopiedUnits();

        LineUnit targetUnit = (LineUnit) copiedUnits.get(copiedUnits.size() - 1);
        Direction currentCopyDirection = computeDirection(targetUnit, mouseUnitX, mouseUnitY);

        int unitWidth = Math.max(Math.abs(targetUnit.getUnitEndX() - targetUnit.getUnitStartX()), 1);

        if (currentCopyDirection == Direction.WEST) {
            addUnit(copiedUnits, targetUnit,
                    targetUnit.getUnitStartX() - unitWidth,
                    targetUnit.getUnitStartY(),
                    targetUnit.getUnitEndX() - unitWidth,
                    targetUnit.getUnitEndY());
        } else if (currentCopyDirection == Direction.EAST) {
            removeUnit(targetUnit, dragData);
        }
    }

    private void dragCopyEast(int mouseUnitX, int mouseUnitY, DragData dragData) {
        ArrayList<Node> copiedUnits = dragData.getCopiedUnits();

        LineUnit targetUnit = (LineUnit) copiedUnits.get(copiedUnits.size() - 1);
        Direction currentCopyDirection = computeDirection(targetUnit, mouseUnitX, mouseUnitY);

        int unitWidth = Math.max(Math.abs(targetUnit.getUnitEndX() - targetUnit.getUnitStartX()), 1);

        if (currentCopyDirection == Direction.EAST) {
            addUnit(copiedUnits, targetUnit,
                    targetUnit.getUnitStartX() + unitWidth,
                    targetUnit.getUnitStartY(),
                    targetUnit.getUnitEndX() + unitWidth,
                    targetUnit.getUnitEndY());
        } else if (currentCopyDirection == Direction.WEST) {
            removeUnit(targetUnit, dragData);
        }
    }

    private void dragCopyNorth(int mouseUnitX, int mouseUnitY, DragData dragData) {
        ArrayList<Node> copiedUnits = dragData.getCopiedUnits();

        LineUnit targetUnit = (LineUnit) copiedUnits.get(copiedUnits.size() - 1);
        Direction currentCopyDirection = computeDirection(targetUnit, mouseUnitX, mouseUnitY);

        int unitHeight = Math.max(Math.abs(targetUnit.getUnitEndY() - targetUnit.getUnitStartY()), 1);

        if (currentCopyDirection == Direction.NORTH) {
            addUnit(copiedUnits, targetUnit,
                    targetUnit.getUnitStartX(),
                    targetUnit.getUnitStartY() - unitHeight,
                    targetUnit.getUnitEndX(),
                    targetUnit.getUnitEndY() - unitHeight);
        } else if (currentCopyDirection == Direction.SOUTH) {
            removeUnit(targetUnit, dragData);
        }
    }

    private void dragCopySouth(int mouseUnitX, int mouseUnitY, DragData dragData) {
        ArrayList<Node> copiedUnits = dragData.getCopiedUnits();

        LineUnit targetUnit = (LineUnit) copiedUnits.get(copiedUnits.size() - 1);
        Direction currentCopyDirection = computeDirection(targetUnit, mouseUnitX, mouseUnitY);

        int unitHeight = Math.max(Math.abs(targetUnit.getUnitEndY() - targetUnit.getUnitStartY()), 1);

        if (currentCopyDirection == Direction.SOUTH) {
            addUnit(copiedUnits, targetUnit,
                    targetUnit.getUnitStartX(),
                    targetUnit.getUnitStartY() + unitHeight,
                    targetUnit.getUnitEndX(),
                    targetUnit.getUnitEndY() + unitHeight);
        } else if (currentCopyDirection == Direction.NORTH) {
            removeUnit(targetUnit, dragData);
        }
    }

    private void addUnit(ArrayList<Node> copiedUnits, LineUnit targetUnit,
             int newUnitStartX, int newUnitStartY, int newUnitEndX, int newUnitEndY) {
        if (isUnitWithinCanvas(newUnitStartX, newUnitStartY, newUnitEndX, newUnitEndY)) {
            LineUnit newUnit;

            if (targetUnit instanceof AnchorLineUnit) {
                newUnit = new AnchorLineUnit(newUnitStartX, newUnitStartY, newUnitEndX, newUnitEndY);
            } else if (targetUnit instanceof GroupLineUnit) {
                newUnit = new GroupLineUnit(newUnitStartX, newUnitStartY, newUnitEndX, newUnitEndY);
            } else {
                newUnit = new LineUnit(newUnitStartX, newUnitStartY, newUnitEndX, newUnitEndY);
            }

            newUnit.colour(targetUnit.getStroke());

            copiedUnits.add(newUnit);
        }
    }

    private void removeUnit(LineUnit unit, DragData dragData) {
        ArrayList<Node> copiedUnits = dragData.getCopiedUnits();

        if (copiedUnits.size() > 1) {
            copiedUnits.remove(unit);
            canvas.removeUnit(unit);
        }

        if (copiedUnits.size() == 1) {
            dragData.setCopyDirection(null);
        }
    }

    private static Direction computeDirection(LineUnit unit, int mouseUnitX, int mouseUnitY) {
        int unitMinX = Math.min(unit.unitStartX.get(), unit.unitEndX.get());
        int unitMinY = Math.min(unit.unitStartY.get(), unit.unitEndY.get());
        int unitMaxX = Math.max(unit.unitStartX.get(), unit.unitEndX.get());
        int unitMaxY = Math.max(unit.unitStartY.get(), unit.unitEndY.get());

        if (mouseUnitX < unitMinX) {
            return Direction.WEST;
        } else if (mouseUnitX > unitMaxX) {
            return Direction.EAST;
        } else if (mouseUnitY < unitMinY) {
            return Direction.NORTH;
        } else if (mouseUnitY > unitMaxY) {
            return Direction.SOUTH;
        } else {
            // Within the unit
            return null;
        }
    }

    private static boolean isUnitWithinCanvas(int unitStartX, int unitStartY, int unitEndX, int unitEndY) {
        return (unitStartX >= 0) && (unitStartX <= CanvasGrid.MAX_X)
                && (unitStartY >= 0) && (unitStartY <= CanvasGrid.MAX_Y)
                && (unitEndX >= 0) && (unitEndX <= CanvasGrid.MAX_X)
                && (unitEndY >= 0) && (unitEndY <= CanvasGrid.MAX_Y);
    }

    private static int clamp(int value, int minValue, int maxValue) {
        return Math.min(Math.max(value, minValue), maxValue);
    }
}
