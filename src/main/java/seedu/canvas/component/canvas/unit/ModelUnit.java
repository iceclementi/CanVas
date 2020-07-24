package seedu.canvas.component.canvas.unit;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.CanvasNode;
import seedu.canvas.component.canvas.Direction;
import seedu.canvas.component.canvas.DragData;
import seedu.canvas.component.canvas.TheCanvas;
import seedu.canvas.util.CanvasMath;

import java.util.ArrayList;

public class ModelUnit extends Rectangle implements CanvasNode, CanvasUnit {

    private TheCanvas canvas = TheCanvas.getInstance();
    private ModelUnitWrapper wrapper = new ModelUnitWrapper(this);

    private UnitPoint pivotPoint;

    private IntegerProperty unitX = new SimpleIntegerProperty();
    private IntegerProperty unitY = new SimpleIntegerProperty();
    private IntegerProperty unitWidth = new SimpleIntegerProperty();
    private IntegerProperty unitHeight = new SimpleIntegerProperty();

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

        unitX.set(CanvasMath.clamp(newUnitX, 0, CanvasGrid.MAX_X - unitWidth.get()));
        unitY.set(CanvasMath.clamp(newUnitY, 0, CanvasGrid.MAX_Y - unitHeight.get()));
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
     * @param mouseLocationX
     *  The x value of the mouse event
     * @param mouseLocationY
     *  The y value of the mouse event
     * @param dragData
     *  The drag data of the rectangle unit
     */
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
        int newUnitWidth =
                CanvasMath.clamp(endUnitX - pivotPoint.getUnitX(), 0, CanvasGrid.MAX_X - pivotPoint.getUnitX());

        if (newUnitWidth == 0) {
            unitX.set(pivotPoint.getUnitX());
        }

        unitWidth.set(newUnitWidth);
    }

    private void scaleSouth(int endUnitY) {
        int newUnitHeight =
                CanvasMath.clamp(endUnitY - pivotPoint.getUnitY(), 0, CanvasGrid.MAX_Y - pivotPoint.getUnitY());

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

    private void initialiseEvents() {
        ModelUnitEventManager modelUnitEventManager = new ModelUnitEventManager();

        addEventFilter(MouseEvent.MOUSE_PRESSED, modelUnitEventManager.getOnMousePressed());
        addEventFilter(MouseEvent.MOUSE_DRAGGED, modelUnitEventManager.getOnMouseDragged());
        addEventFilter(MouseEvent.MOUSE_RELEASED, modelUnitEventManager.getOnMouseReleased());

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

        ModelUnit targetUnit = (ModelUnit) dragData.getRecentCanvasNode();
        Direction currentCopyDirection = computeDirection(targetUnit, mouseLocationX, mouseLocationY);

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

    private void dragCopyEast(double mouseLocationX, double mouseLocationY, DragData dragData) {
        ArrayList<CanvasNode> copiedUnits = dragData.getCopiedCanvasNodes();

        ModelUnit targetUnit = (ModelUnit) dragData.getRecentCanvasNode();
        Direction currentCopyDirection = computeDirection(targetUnit, mouseLocationX, mouseLocationY);

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

    private void dragCopyNorth(double mouseLocationX, double mouseLocationY, DragData dragData) {
        ArrayList<CanvasNode> copiedUnits = dragData.getCopiedCanvasNodes();

        ModelUnit targetUnit = (ModelUnit) dragData.getRecentCanvasNode();
        Direction currentCopyDirection = computeDirection(targetUnit, mouseLocationX, mouseLocationY);

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

    private void dragCopySouth(double mouseLocationX, double mouseLocationY, DragData dragData) {
        ArrayList<CanvasNode> copiedUnits = dragData.getCopiedCanvasNodes();

        ModelUnit targetUnit = (ModelUnit) dragData.getRecentCanvasNode();
        Direction currentCopyDirection = computeDirection(targetUnit, mouseLocationX, mouseLocationY);

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

    private void addUnit(ArrayList<CanvasNode> copiedUnits, ModelUnit targetUnit,
            int newUnitX, int newUnitY, int newUnitWidth, int newUnitHeight) {
        if (canvas.isWithinCanvas(newUnitX, newUnitY, newUnitX + newUnitWidth, newUnitY + newUnitHeight)) {
            ModelUnit newUnit = new ModelUnit(
                    newUnitX,
                    newUnitY,
                    newUnitWidth,
                    newUnitHeight);

            newUnit.colour(targetUnit.getStroke(), targetUnit.getFill());
            newUnit.interactSingle();

            copiedUnits.add(newUnit);
        }
    }

    private void removeUnit(ModelUnit targetUnit, DragData dragData) {
        ArrayList<CanvasNode> copiedUnits = dragData.getCopiedCanvasNodes();

        if (copiedUnits.size() > 1) {
            copiedUnits.remove(targetUnit);
            canvas.removeNode(targetUnit);
        }

        if (copiedUnits.size() == 1) {
            dragData.setCopyDirection(null);
        }

        dragData.getRecentCanvasNode().interactSingle();
    }

    private static Direction computeDirection(ModelUnit unit, double mouseLocationX, double mouseLocationY) {
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
