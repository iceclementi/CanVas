package seedu.canvas.component.canvas.unit;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.Direction;
import seedu.canvas.component.canvas.DragData;
import seedu.canvas.component.canvas.GridPoint;
import seedu.canvas.component.canvas.TheCanvas;

import java.util.ArrayList;

public class RectangleUnit extends Rectangle {

    private TheCanvas canvas = TheCanvas.getInstance();

    private double startX;
    private double startY;

    private int pointX;
    private int pointY;
    private int widthUnit;
    private int heightUnit;

    private int maxX;
    private int maxY;

    private GridPoint anchorPointNW;
    private GridPoint anchorPointNE;
    private GridPoint anchorPointSW;
    private GridPoint anchorPointSE;

    public RectangleUnit(double x, double y, double width, double height) {
        super(x, y, width, height);

        startX = x;
        startY = y;

        pointX = (int) Math.round(x / CanvasGrid.OFFSET);
        pointY = (int) Math.round(y / CanvasGrid.OFFSET);
        widthUnit = (int) Math.round(width / CanvasGrid.OFFSET);
        heightUnit = (int) Math.round(height / CanvasGrid.OFFSET);

        maxX = CanvasGrid.MAX_X - widthUnit;
        maxY = CanvasGrid.MAX_Y - heightUnit;

        initialiseStyle();
        initialiseEvents();
    }

    public int getPointX() {
        return pointX;
    }

    public int getPointY() {
        return pointY;
    }

    public int getWidthUnit() {
        return widthUnit;
    }

    public int getHeightUnit() {
        return heightUnit;
    }

    public GridPoint[] getAnchorPoints() {
        return new GridPoint[]{anchorPointNW, anchorPointNE, anchorPointSW, anchorPointSE};
    }

    public void setAnchorPoints(GridPoint anchorPointNW, GridPoint anchorPointNE,
            GridPoint anchorPointSW, GridPoint anchorPointSE) {
        this.anchorPointNW = anchorPointNW;
        this.anchorPointNE = anchorPointNE;
        this.anchorPointSW = anchorPointSW;
        this.anchorPointSE = anchorPointSE;
    }

    public void selectAnchorPoints() {
        CanvasGrid.selectRectangleAnchorPoints(this, pointX, pointY, widthUnit, heightUnit);
    }

    public void unselectAnchorPoints() {
        CanvasGrid.unselectRectangleAnchorPoints(this);
    }

    private void initialiseStyle() {
        setStroke(Color.MIDNIGHTBLUE);
        setStrokeWidth(2);
        setFill(Color.TRANSPARENT);
    }

    private void initialiseEvents() {
        UnitEventManager unitEventManager = new UnitEventManager();

        addEventFilter(MouseEvent.MOUSE_PRESSED, unitEventManager.getOnMousePressedRectangle());
        addEventFilter(MouseEvent.MOUSE_DRAGGED, unitEventManager.getOnMouseDraggedRectangle());
        addEventFilter(MouseEvent.MOUSE_RELEASED, unitEventManager.getOnMouseReleasedRectangle());
    }

    private int clamp(int value, int minValue, int maxValue) {
        return Math.min(Math.max(value, minValue), maxValue);
    }

    public void moveSnapX(double translateX) {
        int newPointX = clamp((int) Math.round((startX + translateX) / CanvasGrid.OFFSET), 0, maxX);

        // System.out.println(String.format("RectangleUnit X: %s %s", getTranslateX(), getTranslateY()));

        if (newPointX != pointX) {
            pointX = newPointX;
            selectAnchorPoints();
            setTranslateX(pointX * CanvasGrid.OFFSET - startX);
        }
    }

    public void moveSnapY(double translateY) {
        int newPointY = clamp((int) Math.round((startY + translateY) / CanvasGrid.OFFSET), 0, maxY);

        // System.out.println(String.format("RectangleUnit Y: %s %s", getTranslateX(), getTranslateY()));

        if (newPointY != pointY) {
            pointY = newPointY;
            selectAnchorPoints();
            setTranslateY(pointY * CanvasGrid.OFFSET - startY);
        }
    }

    public void resizeSnapX(double width) {
        int newWidthUnit = clamp((int) Math.round(width / CanvasGrid.OFFSET), 1, CanvasGrid.MAX_X - pointX);

        if (newWidthUnit != widthUnit) {
            widthUnit = newWidthUnit;
            maxX = CanvasGrid.MAX_X - widthUnit;
            setWidth(newWidthUnit * CanvasGrid.OFFSET);
            selectAnchorPoints();
        }
    }

    public void resizeSnapY(double height) {
        int newHeightUnit = clamp((int) Math.round(height / CanvasGrid.OFFSET), 1, CanvasGrid.MAX_Y - pointY);

        if (newHeightUnit != heightUnit) {
            heightUnit = newHeightUnit;
            maxY = CanvasGrid.MAX_Y - heightUnit;
            setHeight(newHeightUnit * CanvasGrid.OFFSET);
            selectAnchorPoints();
        }
    }

    public void dragCopy(double rawMouseX, double rawMouseY, DragData dragData) {
        ArrayList<RectangleUnit> copiedRectangles = dragData.getCopiedRectangles();
        Direction copyDirection = dragData.getCopyDirection();
        // System.out.println(copyDirection);

        if (copiedRectangles.isEmpty()) {
            return;
        }

        double mouseX = rawMouseX + (pointX * CanvasGrid.OFFSET - startX);
        double mouseY = rawMouseY + (pointY * CanvasGrid.OFFSET - startY);

        if (copyDirection == null) {
            copyDirection = computeDirection(mouseX, mouseY, this);

            // Mouse is still within unit
            if (copyDirection == null) {
                return;
            }

            dragData.setCopyDirection(copyDirection);
        }

        switch (copyDirection) {
        case LEFT:
            dragCopyLeft(mouseX, mouseY, dragData);
            break;
        case RIGHT:
            dragCopyRight(mouseX, mouseY, dragData);
            break;
        case UP:
            dragCopyUp(mouseX, mouseY, dragData);
            break;
        case DOWN:
            dragCopyDown(mouseX, mouseY, dragData);
            break;
        }
    }

    private void dragCopyLeft(double mouseX, double mouseY, DragData dragData) {
        ArrayList<RectangleUnit> copiedRectangles = dragData.getCopiedRectangles();

        RectangleUnit targetRectangle = copiedRectangles.get(copiedRectangles.size() - 1);
        Direction currentCopyDirection = computeDirection(mouseX, mouseY, targetRectangle);

        if (currentCopyDirection == Direction.LEFT) {
            addRectangle(copiedRectangles, targetRectangle,
                    targetRectangle.getPointX() - targetRectangle.getWidthUnit(),
                    targetRectangle.getPointY(),
                    targetRectangle.getWidthUnit(),
                    targetRectangle.getHeightUnit());
        } else if (currentCopyDirection == Direction.RIGHT) {
            removeRectangle(targetRectangle, dragData);
        }
    }

    private void dragCopyRight(double mouseX, double mouseY, DragData dragData) {
        ArrayList<RectangleUnit> copiedRectangles = dragData.getCopiedRectangles();

        RectangleUnit targetRectangle = copiedRectangles.get(copiedRectangles.size() - 1);
        Direction currentCopyDirection = computeDirection(mouseX, mouseY, targetRectangle);

        if (currentCopyDirection == Direction.RIGHT) {
            addRectangle(copiedRectangles, targetRectangle,
                    targetRectangle.getPointX() + targetRectangle.getWidthUnit(),
                    targetRectangle.getPointY(),
                    targetRectangle.getWidthUnit(),
                    targetRectangle.getHeightUnit());
        } else if (currentCopyDirection == Direction.LEFT) {
            removeRectangle(targetRectangle, dragData);
        }
    }

    private void dragCopyUp(double mouseX, double mouseY, DragData dragData) {
        ArrayList<RectangleUnit> copiedRectangles = dragData.getCopiedRectangles();

        RectangleUnit targetRectangle = copiedRectangles.get(copiedRectangles.size() - 1);
        Direction currentCopyDirection = computeDirection(mouseX, mouseY, targetRectangle);

        if (currentCopyDirection == Direction.UP) {
            addRectangle(copiedRectangles, targetRectangle,
                    targetRectangle.getPointX(),
                    targetRectangle.getPointY() - targetRectangle.getHeightUnit(),
                    targetRectangle.getWidthUnit(),
                    targetRectangle.getHeightUnit());
        } else if (currentCopyDirection == Direction.DOWN) {
            removeRectangle(targetRectangle, dragData);
        }
    }

    private void dragCopyDown(double mouseX, double mouseY, DragData dragData) {
        ArrayList<RectangleUnit> copiedRectangles = dragData.getCopiedRectangles();

        RectangleUnit targetRectangle = copiedRectangles.get(copiedRectangles.size() - 1);
        Direction currentCopyDirection = computeDirection(mouseX, mouseY, targetRectangle);

        if (currentCopyDirection == Direction.DOWN) {
            addRectangle(copiedRectangles, targetRectangle,
                    targetRectangle.getPointX(),
                    targetRectangle.getPointY() + targetRectangle.getHeightUnit(),
                    targetRectangle.getWidthUnit(),
                    targetRectangle.getHeightUnit());
        } else if (currentCopyDirection == Direction.UP) {
            removeRectangle(targetRectangle, dragData);
        }
    }

    private void addRectangle(ArrayList<RectangleUnit> copiedRectangles, RectangleUnit targetRectangle,
            int newPointX, int newPointY, int newWidthUnit, int newHeightUnit) {
        if (isRectangleWithinCanvas(newPointX, newPointY, newWidthUnit, newHeightUnit)) {
            RectangleUnit newRectangle = new RectangleUnit(
                    newPointX * CanvasGrid.OFFSET,
                    newPointY * CanvasGrid.OFFSET,
                    newWidthUnit * CanvasGrid.OFFSET,
                    newHeightUnit * CanvasGrid.OFFSET);

            copiedRectangles.add(newRectangle);
            canvas.getChildren().add(newRectangle);

            targetRectangle.unselectAnchorPoints();
            newRectangle.selectAnchorPoints();
        }
    }

    private void removeRectangle(RectangleUnit targetRectangle, DragData dragData) {
        ArrayList<RectangleUnit> copiedRectangles = dragData.getCopiedRectangles();

        if (copiedRectangles.size() > 1) {
            targetRectangle.unselectAnchorPoints();

            copiedRectangles.remove(targetRectangle);
            canvas.getChildren().remove(targetRectangle);

            copiedRectangles.get(copiedRectangles.size() - 1).selectAnchorPoints();
        }

        if (copiedRectangles.size() == 1) {
            dragData.setCopyDirection(null);
        }
    }

    private static Direction computeDirection(double mouseX, double mouseY, RectangleUnit unit) {
        if (mouseX < unit.getPointX() * CanvasGrid.OFFSET) {
            return Direction.LEFT;
        } else if (mouseX > (unit.getPointX() + unit.getWidthUnit()) * CanvasGrid.OFFSET) {
            return Direction.RIGHT;
        } else if (mouseY < unit.getPointY() * CanvasGrid.OFFSET) {
            return Direction.UP;
        } else if (mouseY > (unit.getPointY() + unit.getHeightUnit()) * CanvasGrid.OFFSET) {
            return Direction.DOWN;
        } else {
            return null;
        }
    }

    private static boolean isRectangleWithinCanvas(int pointX, int pointY, int widthUnit, int heightUnit) {
        return (pointX >= 0) && (pointX + widthUnit <= CanvasGrid.MAX_X) &&
                (pointY >= 0) && (pointY + heightUnit <= CanvasGrid.MAX_Y);
    }
}
