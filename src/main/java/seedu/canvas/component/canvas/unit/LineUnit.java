package seedu.canvas.component.canvas.unit;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.CanvasNode;
import seedu.canvas.component.canvas.Direction;
import seedu.canvas.component.canvas.DragData;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.util.CanvasMath;

import java.util.ArrayList;

public class LineUnit extends Line implements CanvasNode, CanvasUnit {

    protected TheCanvas canvas = TheCanvas.getInstance();
    private LineUnitWrapper wrapper = new LineUnitWrapper(this);

    protected IntegerProperty unitStartX = new SimpleIntegerProperty();
    protected IntegerProperty unitStartY = new SimpleIntegerProperty();
    protected IntegerProperty unitEndX = new SimpleIntegerProperty();
    protected IntegerProperty unitEndY = new SimpleIntegerProperty();

    public LineUnit(int unitStartX, int unitStartY, int unitEndX, int unitEndY) {
        super();

        initialiseOther();

        initialiseStyle();

        this.unitStartX.set(unitStartX);
        this.unitStartY.set(unitStartY);
        this.unitEndX.set(unitEndX);
        this.unitEndY.set(unitEndY);

        canvas.addNode(this);

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

    public int getUnitWidth() {
        return CanvasGrid.toUnit(getWidth());
    }

    public int getUnitHeight() {
        return CanvasGrid.toUnit(getHeight());
    }

    public double getCanvasStartX() {
        return Math.min(getStartX(), getEndX());
    }

    public double getCanvasStartY() {
        return Math.min(getStartY(), getEndY());
    }

    public double getCanvasEndX() {
        return Math.max(getStartX(), getEndX());
    }

    public double getCanvasEndY() {
        return Math.max(getStartY(), getEndY());
    }

    public double getWidth() {
        return getCanvasEndX() - getCanvasStartX();
    }

    public double getHeight() {
        return getCanvasEndY() - getCanvasStartY();
    }

    public ArrayList<Node> getGroup() {
        ArrayList<Node> group = new ArrayList<>();
        group.add(this);
        group.addAll(wrapper.getGroup());

        return group;
    }

    public void interactSingle() {
        canvas.interactSingle(this);
        wrapper.interactSingle();
    }

    public void focusSingle() {
        canvas.interactSingle(this);
        wrapper.focusSingle();
    }

    public void interactMultiple() {
        wrapper.interactMultiple();
    }

    public void focusMultiple() {
        wrapper.focusMultiple();
    }

    public void unfocus() {
        wrapper.unfocus();
    }

    public void scale(int endUnitX, int endUnitY) {
        unitEndX.set(CanvasMath.clamp(endUnitX, CanvasGrid.MIN_X, CanvasGrid.MAX_X));
        unitEndY.set(CanvasMath.clamp(endUnitY, CanvasGrid.MIN_Y, CanvasGrid.MAX_Y));
    }

    public void move(double newStartX, double newStartY) {
        int newUnitStartX = CanvasMath.clamp(CanvasGrid.toUnit(newStartX), 0, CanvasGrid.MAX_X - getUnitWidth());
        int newUnitStartY = CanvasGrid.clamp(CanvasGrid.toUnit(newStartY), 0, CanvasGrid.MAX_Y - getUnitHeight());

        int unitDeltaX = newUnitStartX - CanvasGrid.toUnit(getCanvasStartX());
        int unitDeltaY = newUnitStartY - CanvasGrid.toUnit(getCanvasStartY());

        setUnitStartX(getUnitStartX() + unitDeltaX);
        setUnitStartY(getUnitStartY() + unitDeltaY);
        setUnitEndX(getUnitEndX() + unitDeltaX);
        setUnitEndY(getUnitEndY() + unitDeltaY);
    }

    public LineUnit copy() {
        LineUnit copiedUnit = new LineUnit(getUnitStartX(), getUnitStartY(), getUnitEndX(), getUnitEndY());
        copiedUnit.colourLine((Color) getStroke());

        return copiedUnit;
    }

    public void dragCopy(double mouseLocationX, double mouseLocationY, DragData dragData) {
        if (dragData.getCopiedCanvasNodes().isEmpty()) {
            return;
        }

        if (dragData.getCopyDirection() == null) {
            dragData.setCopyDirection(computeDirection(this, mouseLocationX, mouseLocationY));

            // Mouse is still within unit
            if (dragData.getCopyDirection() == null) {
                return;
            }
        }

        switch (dragData.getCopyDirection()) {
        case WEST:
            dragCopyWest(mouseLocationX, mouseLocationY, dragData);
            break;
        case EAST:
            dragCopyEast(mouseLocationX, mouseLocationY, dragData);
            break;
        case NORTH:
            dragCopyNorth(mouseLocationX, mouseLocationY, dragData);
            break;
        case SOUTH:
            dragCopySouth(mouseLocationX, mouseLocationY, dragData);
            break;
        default:
            break;
        }
    }

    public void colourLine(Color lineColour) {
        setStroke(lineColour);
    }

    public void colourFill(Color fillColour) {
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

        canvas.getCanvasModeProperty().addListener(observable -> {
            switch (canvas.getCanvasMode()) {
            case POINT:
            case SHAPE:
                setCursor(Cursor.HAND);
                break;
            case TEXT:
                setCursor(Cursor.CROSSHAIR);
                break;
            default:
                setCursor(Cursor.DEFAULT);
                break;
            }
        });
    }

    private void dragCopyWest(double mouseLocationX, double mouseLocationY, DragData dragData) {
        ArrayList<CanvasNode> copiedUnits = dragData.getCopiedCanvasNodes();

        LineUnit targetUnit = (LineUnit) dragData.getRecentCanvasNode();
        Direction currentCopyDirection = computeDirection(targetUnit, mouseLocationX, mouseLocationY);

        int unitWidth = Math.max(targetUnit.getUnitWidth(), 1);

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

    private void dragCopyEast(double mouseLocationX, double mouseLocationY, DragData dragData) {
        ArrayList<CanvasNode> copiedUnits = dragData.getCopiedCanvasNodes();

        LineUnit targetUnit = (LineUnit) dragData.getRecentCanvasNode();
        Direction currentCopyDirection = computeDirection(targetUnit, mouseLocationX, mouseLocationY);

        int unitWidth = Math.max(targetUnit.getUnitWidth(), 1);

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

    private void dragCopyNorth(double mouseLocationX, double mouseLocationY, DragData dragData) {
        ArrayList<CanvasNode> copiedUnits = dragData.getCopiedCanvasNodes();

        LineUnit targetUnit = (LineUnit) dragData.getRecentCanvasNode();
        Direction currentCopyDirection = computeDirection(targetUnit, mouseLocationX, mouseLocationY);

        int unitHeight = Math.max(targetUnit.getUnitHeight(), 1);

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

    private void dragCopySouth(double mouseLocationX, double mouseLocationY, DragData dragData) {
        ArrayList<CanvasNode> copiedUnits = dragData.getCopiedCanvasNodes();

        LineUnit targetUnit = (LineUnit) dragData.getRecentCanvasNode();
        Direction currentCopyDirection = computeDirection(targetUnit, mouseLocationX, mouseLocationY);

        int unitHeight = Math.max(targetUnit.getUnitHeight(), 1);

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

    private void addUnit(ArrayList<CanvasNode> copiedUnits, LineUnit targetUnit,
             int newUnitStartX, int newUnitStartY, int newUnitEndX, int newUnitEndY) {
        if (canvas.isWithinCanvas(newUnitStartX, newUnitStartY, newUnitEndX, newUnitEndY)) {
            LineUnit newUnit = targetUnit.copy();
            newUnit.move(CanvasGrid.toActual(newUnitStartX), CanvasGrid.toActual(newUnitStartY));
            newUnit.interactSingle();

            copiedUnits.add(newUnit);
        }
    }

    private void removeUnit(LineUnit unit, DragData dragData) {
        ArrayList<CanvasNode> copiedUnits = dragData.getCopiedCanvasNodes();

        if (copiedUnits.size() > 1) {
            copiedUnits.remove(unit);
            canvas.removeNode(unit);
        }

        if (copiedUnits.size() == 1) {
            dragData.setCopyDirection(null);
        }

        dragData.getRecentCanvasNode().interactSingle();
    }

    private static Direction computeDirection(LineUnit unit, double mouseLocationX, double mouseLocationY) {
        if (CanvasGrid.toUnit(mouseLocationX - unit.getCanvasStartX()) < 0) {
            return Direction.WEST;
        } else if (CanvasGrid.toUnit(mouseLocationX - unit.getCanvasEndX()) > 0) {
            return Direction.EAST;
        } else if (CanvasGrid.toUnit(mouseLocationY - unit.getCanvasStartY()) < 0) {
            return Direction.NORTH;
        } else if (CanvasGrid.toUnit(mouseLocationY - unit.getCanvasEndY()) > 0) {
            return Direction.SOUTH;
        } else {
            // Within the unit
            return null;
        }
    }
}
