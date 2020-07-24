package seedu.canvas.component.canvas.unit;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import seedu.canvas.component.canvas.CanvasNode;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.Direction;
import seedu.canvas.component.canvas.DragData;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.util.CanvasMath;

import java.util.ArrayList;
import java.util.Arrays;

public class ModelUnit extends Rectangle implements CanvasNode, CanvasUnit {

    private TheCanvas canvas = TheCanvas.getInstance();

    private UnitPoint pivotPoint;

    private IntegerProperty unitX = new SimpleIntegerProperty();
    private IntegerProperty unitY = new SimpleIntegerProperty();
    private IntegerProperty unitWidth = new SimpleIntegerProperty();
    private IntegerProperty unitHeight = new SimpleIntegerProperty();

    private ModelResizeHandle resizeHandleNW = new ModelResizeHandle(this, Direction.NORTHWEST);
    private ModelResizeHandle resizeHandleNE = new ModelResizeHandle(this, Direction.NORTHEAST);
    private ModelResizeHandle resizeHandleSW = new ModelResizeHandle(this, Direction.SOUTHWEST);
    private ModelResizeHandle resizeHandleSE = new ModelResizeHandle(this, Direction.SOUTHEAST);

    public ModelUnit(int unitX, int unitY, int unitWidth, int unitHeight) {
        super();

        initialiseStyle();

        this.unitX.set(unitX);
        this.unitY.set(unitY);
        this.unitWidth.set(unitWidth);
        this.unitHeight.set(unitHeight);

        pivotPoint = new UnitPoint(unitX, unitY);

        canvas.addNode(this);

        initialiseEvents();
    }

    public int getUnitX() {
        return unitX.get();
    }

    public void setUnitX(int unitX) {
        this.unitX.set(unitX);
    }

    public int getUnitY() {
        return unitY.get();
    }

    public void setUnitY(int unitY) {
        this.unitY.set(unitY);
    }

    public int getUnitWidth() {
        return unitWidth.get();
    }

    public void setUnitWidth(int unitWidth) {
        this.unitWidth.set(unitWidth);
    }

    public int getUnitHeight() {
        return unitHeight.get();
    }

    public void setUnitHeight(int unitHeight) {
        this.unitHeight.set(unitHeight);
    }

    public double getCanvasStartX() {
        return CanvasGrid.toActual(getUnitX());
    }

    public double getCanvasStartY() {
        return CanvasGrid.toActual(getUnitY());
    }

    public double getCanvasEndX() {
        return CanvasGrid.toActual(getUnitX() + getUnitWidth());
    }

    public double getCanvasEndY() {
        return CanvasGrid.toActual(getUnitY() + getUnitHeight());
    }

    public ArrayList<Node> getGroup() {
        return new ArrayList<>(Arrays.asList(this, resizeHandleNW, resizeHandleNE, resizeHandleSW, resizeHandleSE));
    }

    public void interactSingle() {
        canvas.interactSingle(this);
        toFront();
        getHandles().forEach(CanvasHandle::interact);
    }

    public void focusSingle() {
        canvas.interactSingle(this);
        toFront();
        getHandles().forEach(CanvasHandle::focus);
    }

    public void interactMultiple() {
        toFront();
        // getHandles().forEach(CanvasHandle::interact);
    }

    public void focusMultiple() {
        toFront();
        // getHandles().forEach(CanvasHandle::focus);
    }

    public void unfocus() {
        getHandles().forEach(CanvasHandle::unfocus);
    }

    public void scale(int unitEndX, int unitEndY) {
        if (pivotPoint.getUnitX() <= unitEndX) {
            scaleEast(unitEndX);
        } else {
            scaleWest(unitEndX);
        }
        if (pivotPoint.getUnitY() <= unitEndY) {
            scaleSouth(unitEndY);
        } else {
            scaleNorth(unitEndY);
        }
    }

    public void move(double newX, double newY) {
        int newUnitX = CanvasGrid.toUnit(newX);
        int newUnitY = CanvasGrid.toUnit(newY);

        unitX.set(clamp(newUnitX, 0, CanvasGrid.MAX_X - unitWidth.get()));
        unitY.set(clamp(newUnitY, 0, CanvasGrid.MAX_Y - unitHeight.get()));
    }

    public void colourLine(Color lineColour) {
        setStroke(lineColour);
    }

    public void colourFill(Color fillColour) {
        setFill(fillColour);
    }

    public boolean isIntersect(int pointX, int pointY) {
        return (pointX >= unitX.get() && pointX <= unitX.get() + unitWidth.get())
                && (pointY >= unitY.get() && pointY <= unitY.get() + unitHeight.get());
    }

    /**
     * Copies the selected rectangle unit along the drag direction.
     *
     * @param mouseUnitX
     *  The x value of the mouse event
     * @param mouseUnitY
     *  The y value of the mouse event
     * @param dragData
     *  The drag data of the rectangle unit
     */
    public void dragCopy(int mouseUnitX, int mouseUnitY, DragData dragData) {
        ArrayList<Node> copiedUnits = dragData.getCopiedUnits();
        Direction copyDirection = dragData.getCopyDirection();

        if (copiedUnits.isEmpty()) {
            return;
        }

        if (copyDirection == null) {
            copyDirection = computeDirection(mouseUnitX, mouseUnitY, this);

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

    private void initialiseStyle() {
        colour();
        setStrokeWidth(3);

        xProperty().bind(unitX.multiply(CanvasGrid.OFFSET));
        yProperty().bind(unitY.multiply(CanvasGrid.OFFSET));
        widthProperty().bind(unitWidth.multiply((CanvasGrid.OFFSET)));
        heightProperty().bind(unitHeight.multiply(CanvasGrid.OFFSET));

        unfocus();

        setCursor(Cursor.HAND);
    }

    private void scaleEast(int endUnitX) {
        int newUnitWidth = CanvasMath.clamp(endUnitX - pivotPoint.getUnitX(), 0, CanvasGrid.MAX_X - pivotPoint.getUnitX());

        if (newUnitWidth == 0) {
            unitX.set(pivotPoint.getUnitX());
        }

        unitWidth.set(newUnitWidth);
    }

    private void scaleSouth(int endUnitY) {
        int newUnitHeight = CanvasMath.clamp(endUnitY - pivotPoint.getUnitY(), 0, CanvasGrid.MAX_Y - pivotPoint.getUnitY());

        if (newUnitHeight == 0) {
            unitY.set(pivotPoint.getUnitY());
        }

        unitHeight.set(newUnitHeight);
    }

    private void scaleWest(int endUnitX) {
        int newX = CanvasMath.clamp(endUnitX, 0, pivotPoint.getUnitX());
        int newUnitWidth = CanvasMath.clamp(pivotPoint.getUnitX() - newX, 0, pivotPoint.getUnitX());

        unitX.set(newX);
        unitWidth.set(newUnitWidth);
    }

    private void scaleNorth(int endUnitY) {
        int newY = CanvasMath.clamp(endUnitY, 0, pivotPoint.getUnitY());
        int newUnitHeight = CanvasMath.clamp(pivotPoint.getUnitY() - newY, 0, pivotPoint.getUnitY());

        unitY.set(newY);
        unitHeight.set(newUnitHeight);
    }

    private void colour() {
        setStroke(canvas.getLineColour());
        setFill(canvas.getFillColour());
    }

    private void colour(Paint lineColour, Paint fillColor) {
        setStroke(lineColour);
        setFill(fillColor);
    }

    private ArrayList<CanvasHandle> getHandles() {
        return new ArrayList<>(Arrays.asList(resizeHandleNW, resizeHandleNE, resizeHandleSW, resizeHandleSE));
    }

    private void initialiseEvents() {
        ModelUnitEventManager modelUnitEventManager = new ModelUnitEventManager();

        addEventFilter(MouseEvent.MOUSE_PRESSED, modelUnitEventManager.getOnMousePressed());
        addEventFilter(MouseEvent.MOUSE_DRAGGED, modelUnitEventManager.getOnMouseDragged());
        addEventFilter(MouseEvent.MOUSE_RELEASED, modelUnitEventManager.getOnMouseReleased());
    }

    private void dragCopyWest(int mouseUnitX, int mouseUnitY, DragData dragData) {
        ArrayList<Node> copiedUnits = dragData.getCopiedUnits();

        ModelUnit targetUnit = (ModelUnit) copiedUnits.get(copiedUnits.size() - 1);
        Direction currentCopyDirection = computeDirection(mouseUnitX, mouseUnitY, targetUnit);

        if (currentCopyDirection == Direction.WEST) {
            addUnit(copiedUnits, targetUnit,
                    targetUnit.getUnitX() - targetUnit.getUnitWidth(),
                    targetUnit.getUnitY(),
                    targetUnit.getUnitWidth(),
                    targetUnit.getUnitHeight());
        } else if (currentCopyDirection == Direction.EAST) {
            removeUnit(targetUnit, dragData);
        }
    }

    private void dragCopyEast(int mouseUnitX, int mouseUnitY, DragData dragData) {
        ArrayList<Node> copiedUnits = dragData.getCopiedUnits();

        ModelUnit targetUnit = (ModelUnit) copiedUnits.get(copiedUnits.size() - 1);
        Direction currentCopyDirection = computeDirection(mouseUnitX, mouseUnitY, targetUnit);

        if (currentCopyDirection == Direction.EAST) {
            addUnit(copiedUnits, targetUnit,
                    targetUnit.getUnitX() + targetUnit.getUnitWidth(),
                    targetUnit.getUnitY(),
                    targetUnit.getUnitWidth(),
                    targetUnit.getUnitHeight());
        } else if (currentCopyDirection == Direction.WEST) {
            removeUnit(targetUnit, dragData);
        }
    }

    private void dragCopyNorth(int mouseUnitX, int mouseUnitY, DragData dragData) {
        ArrayList<Node> copiedUnits = dragData.getCopiedUnits();

        ModelUnit targetUnit = (ModelUnit) copiedUnits.get(copiedUnits.size() - 1);
        Direction currentCopyDirection = computeDirection(mouseUnitX, mouseUnitY, targetUnit);

        if (currentCopyDirection == Direction.NORTH) {
            addUnit(copiedUnits, targetUnit,
                    targetUnit.getUnitX(),
                    targetUnit.getUnitY() - targetUnit.getUnitHeight(),
                    targetUnit.getUnitWidth(),
                    targetUnit.getUnitHeight());
        } else if (currentCopyDirection == Direction.SOUTH) {
            removeUnit(targetUnit, dragData);
        }
    }

    private void dragCopySouth(int mouseUnitX, int mouseUnitY, DragData dragData) {
        ArrayList<Node> copiedUnits = dragData.getCopiedUnits();

        ModelUnit targetUnit = (ModelUnit) copiedUnits.get(copiedUnits.size() - 1);
        Direction currentCopyDirection = computeDirection(mouseUnitX, mouseUnitY, targetUnit);

        if (currentCopyDirection == Direction.SOUTH) {
            addUnit(copiedUnits, targetUnit,
                    targetUnit.getUnitX(),
                    targetUnit.getUnitY() + targetUnit.getUnitHeight(),
                    targetUnit.getUnitWidth(),
                    targetUnit.getUnitHeight());
        } else if (currentCopyDirection == Direction.NORTH) {
            removeUnit(targetUnit, dragData);
        }
    }

    private void addUnit(ArrayList<Node> copiedUnits, ModelUnit targetUnit,
            int newUnitX, int newUnitY, int newUnitWidth, int newUnitHeight) {
        if (isUnitWithinCanvas(newUnitX, newUnitY, newUnitWidth, newUnitHeight)) {
            ModelUnit newUnit = new ModelUnit(
                    newUnitX,
                    newUnitY,
                    newUnitWidth,
                    newUnitHeight);

            newUnit.colour(targetUnit.getStroke(), targetUnit.getFill());

            copiedUnits.add(newUnit);
        }
    }

    private void removeUnit(ModelUnit targetUnit, DragData dragData) {
        ArrayList<Node> copiedUnits = dragData.getCopiedUnits();

        if (copiedUnits.size() > 1) {
            copiedUnits.remove(targetUnit);
            canvas.removeNode(targetUnit);
        }

        if (copiedUnits.size() == 1) {
            dragData.setCopyDirection(null);
        }
    }

    private int clamp(int value, int minValue, int maxValue) {
        return Math.min(Math.max(value, minValue), maxValue);
    }

    private static Direction computeDirection(int mouseUnitX, int mouseUnitY, ModelUnit unit) {
        if (mouseUnitX < unit.getUnitX()) {
            return Direction.WEST;
        } else if (mouseUnitX > (unit.getUnitX() + unit.getUnitWidth())) {
            return Direction.EAST;
        } else if (mouseUnitY < unit.getUnitY()) {
            return Direction.NORTH;
        } else if (mouseUnitY > (unit.getUnitY() + unit.getUnitHeight())) {
            return Direction.SOUTH;
        } else {
            // Within the unit
            return null;
        }
    }

    private static boolean isUnitWithinCanvas(int unitX, int unitY, int unitWidth, int unitHeight) {
        return (unitX >= 0) && (unitX + unitWidth <= CanvasGrid.MAX_X)
                && (unitY >= 0) && (unitY + unitHeight <= CanvasGrid.MAX_Y);
    }
}
