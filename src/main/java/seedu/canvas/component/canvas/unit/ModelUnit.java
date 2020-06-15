package seedu.canvas.component.canvas.unit;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.Direction;
import seedu.canvas.component.canvas.DragData;
import seedu.canvas.component.canvas.GridPoint;
import seedu.canvas.component.canvas.TheCanvas;

import java.util.ArrayList;
import java.util.Arrays;

public class ModelUnit extends Rectangle {

    private TheCanvas canvas = TheCanvas.getInstance();

    private IntegerProperty unitX = new SimpleIntegerProperty();
    private IntegerProperty unitY = new SimpleIntegerProperty();
    private IntegerProperty unitWidth = new SimpleIntegerProperty();
    private IntegerProperty unitHeight = new SimpleIntegerProperty();

    private GridPoint anchorPointNW;
    private GridPoint anchorPointNE;
    private GridPoint anchorPointSW;
    private GridPoint anchorPointSE;

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

        canvas.addUnit(this);

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

    public ArrayList<Node> getUnitGroup() {
        return new ArrayList<>(Arrays.asList(this, resizeHandleNW, resizeHandleNE, resizeHandleSW, resizeHandleSE));
    }

    /**
     * Gets the anchor points of this rectangle unit.
     *
     * @return
     *  The anchor points
     */
    public GridPoint[] getAnchorPoints() {
        return new GridPoint[]{anchorPointNW, anchorPointNE, anchorPointSW, anchorPointSE};
    }

    /**
     * Sets the anchor points of this rectangle unit.
     *
     * @param anchorPointNW
     *  The NW anchor point
     * @param anchorPointNE
     *  The NE anchor point
     * @param anchorPointSW
     *  The SW anchor point
     * @param anchorPointSE
     *  The SE anchor point
     */
    public void setAnchorPoints(GridPoint anchorPointNW, GridPoint anchorPointNE,
            GridPoint anchorPointSW, GridPoint anchorPointSE) {
        this.anchorPointNW = anchorPointNW;
        this.anchorPointNE = anchorPointNE;
        this.anchorPointSW = anchorPointSW;
        this.anchorPointSE = anchorPointSE;
    }

    /**
     * Selects/Reselects and highlights the anchor points of this rectangle unit.
     */
    public void selectAnchorPoints() {
        CanvasGrid.selectRectangleAnchorPoints(this, unitX.get(), unitY.get(), unitWidth.get(), unitHeight.get());
    }

    /**
     * Unselects the anchor points of this rectangle unit.
     */
    public void unselectAnchorPoints() {
        CanvasGrid.unselectRectangleAnchorPoints(this);
    }

    public void interact() {
        toFront();

        resizeHandleNW.interact();
        resizeHandleNE.interact();
        resizeHandleSW.interact();
        resizeHandleSE.interact();
    }

    public void focus() {
        toFront();

        resizeHandleNW.focus();
        resizeHandleNE.focus();
        resizeHandleSW.focus();
        resizeHandleSE.focus();
    }

    public void unfocus() {
        resizeHandleNW.unfocus();
        resizeHandleNE.unfocus();
        resizeHandleSW.unfocus();
        resizeHandleSE.unfocus();
    }

    public void scale(int newUnitWidth, int newUnitHeight) {
        unitWidth.set(clamp(newUnitWidth, 0, CanvasGrid.MAX_X - unitX.get()));
        unitHeight.set(clamp(newUnitHeight, 0, CanvasGrid.MAX_Y - unitY.get()));
    }

    public void move(int newUnitX, int newUnitY) {
        unitX.set(clamp(newUnitX, 0, CanvasGrid.MAX_X - unitWidth.get()));
        unitY.set(clamp(newUnitY, 0, CanvasGrid.MAX_Y - unitHeight.get()));
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
        ArrayList<ModelUnit> copiedUnits = dragData.getCopiedUnits();
        Direction copyDirection = dragData.getCopyDirection();
        // System.out.println(copyDirection);

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
    }

    private void colour() {
        setStroke(canvas.getLineColour());
        setFill(canvas.getFillColour());
    }

    private void colour(Paint lineColour, Paint fillColor) {
        setStroke(lineColour);
        setFill(fillColor);
    }

    private void initialiseEvents() {
        ModelUnitEventManager modelUnitEventManager = new ModelUnitEventManager();

        addEventFilter(MouseEvent.MOUSE_PRESSED, modelUnitEventManager.getOnMousePressed());
        addEventFilter(MouseEvent.MOUSE_DRAGGED, modelUnitEventManager.getOnMouseDragged());
        addEventFilter(MouseEvent.MOUSE_RELEASED, modelUnitEventManager.getOnMouseReleased());
        addEventFilter(MouseEvent.MOUSE_CLICKED, modelUnitEventManager.getOnMouseClicked());
    }

    private void dragCopyWest(int mouseUnitX, int mouseUnitY, DragData dragData) {
        ArrayList<ModelUnit> copiedUnits = dragData.getCopiedUnits();

        ModelUnit targetUnit = copiedUnits.get(copiedUnits.size() - 1);
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
        ArrayList<ModelUnit> copiedUnits = dragData.getCopiedUnits();

        ModelUnit targetUnit = copiedUnits.get(copiedUnits.size() - 1);
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
        ArrayList<ModelUnit> copiedUnits = dragData.getCopiedUnits();

        ModelUnit targetUnit = copiedUnits.get(copiedUnits.size() - 1);
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
        ArrayList<ModelUnit> copiedUnits = dragData.getCopiedUnits();

        ModelUnit targetUnit = copiedUnits.get(copiedUnits.size() - 1);
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

    private void addUnit(ArrayList<ModelUnit> copiedUnits, ModelUnit targetUnit,
                         int newUnitX, int newUnitY, int newUnitWidth, int newUnitHeight) {
        if (isUnitWithinCanvas(newUnitX, newUnitY, newUnitWidth, newUnitHeight)) {
            ModelUnit newUnit = new ModelUnit(
                    newUnitX,
                    newUnitY,
                    newUnitWidth,
                    newUnitHeight);

            newUnit.colour(targetUnit.getStroke(), targetUnit.getFill());

            copiedUnits.add(newUnit);

            targetUnit.unselectAnchorPoints();
            newUnit.selectAnchorPoints();
        }
    }

    private void removeUnit(ModelUnit targetUnit, DragData dragData) {
        ArrayList<ModelUnit> copiedUnits = dragData.getCopiedUnits();

        if (copiedUnits.size() > 1) {
            targetUnit.unselectAnchorPoints();

            copiedUnits.remove(targetUnit);
            canvas.removeUnit(targetUnit);

            copiedUnits.get(copiedUnits.size() - 1).selectAnchorPoints();
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
            // Within the rectangle
            return null;
        }
    }

    public static boolean isUnitWithinCanvas(int unitX, int unitY, int unitWidth, int unitHeight) {
        return (unitX >= 0) && (unitX + unitWidth <= CanvasGrid.MAX_X)
                && (unitY >= 0) && (unitY + unitHeight <= CanvasGrid.MAX_Y);
    }
}
