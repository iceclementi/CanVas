package seedu.canvas.component.canvas.unit;

import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
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

        canvas.getChildren().add(this);

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
        CanvasGrid.selectRectangleAnchorPoints(this, pointX, pointY, widthUnit, heightUnit);
    }

    /**
     * Unselects the anchor points of this rectangle unit.
     */
    public void unselectAnchorPoints() {
        CanvasGrid.unselectRectangleAnchorPoints(this);
    }

    /**
     * Snaps the x value of this rectangle unit on MOVE.
     *
     * @param translateX
     *  The translate x value of the rectangle unit
     */
    public void moveSnapX(double translateX) {
        int newPointX = clamp((int) Math.round((startX + translateX) / CanvasGrid.OFFSET), 0, maxX);

        // System.out.println(String.format("RectangleUnit X: %s %s", getTranslateX(), getTranslateY()));

        if (newPointX != pointX) {
            pointX = newPointX;
            selectAnchorPoints();
            setTranslateX(pointX * CanvasGrid.OFFSET - startX);
        }
    }

    /**
     * Snaps the y value of this rectangle unit on MOVE.
     *
     * @param translateY
     *  The translate y value of the rectangle unit
     */
    public void moveSnapY(double translateY) {
        int newPointY = clamp((int) Math.round((startY + translateY) / CanvasGrid.OFFSET), 0, maxY);

        // System.out.println(String.format("RectangleUnit Y: %s %s", getTranslateX(), getTranslateY()));

        if (newPointY != pointY) {
            pointY = newPointY;
            selectAnchorPoints();
            setTranslateY(pointY * CanvasGrid.OFFSET - startY);
        }
    }

    /**
     * Snaps the x value of this rectangle unit on RESIZE.
     *
     * @param rawMouseX
     *  The x value of the mouse event
     */
    public void resizeSnapX(double rawMouseX) {
        double width = rawMouseX  - startX;
        int newWidthUnit = clamp((int) Math.round(width / CanvasGrid.OFFSET), 1, CanvasGrid.MAX_X - pointX);

        if (newWidthUnit != widthUnit) {
            widthUnit = newWidthUnit;
            maxX = CanvasGrid.MAX_X - widthUnit;
            setWidth(newWidthUnit * CanvasGrid.OFFSET);
            selectAnchorPoints();
        }
    }

    /**
     * Snaps the y value of this rectangle unit on RESIZE.
     *
     * @param rawMouseY
     *  The y value of the mouse event
     */
    public void resizeSnapY(double rawMouseY) {
        double height = rawMouseY - startY;
        int newHeightUnit = clamp((int) Math.round(height / CanvasGrid.OFFSET), 1, CanvasGrid.MAX_Y - pointY);

        if (newHeightUnit != heightUnit) {
            heightUnit = newHeightUnit;
            maxY = CanvasGrid.MAX_Y - heightUnit;
            setHeight(newHeightUnit * CanvasGrid.OFFSET);
            selectAnchorPoints();
        }
    }

    /**
     * Copies the selected rectangle unit along the drag direction.
     *
     * @param rawMouseX
     *  The x value of the mouse event
     * @param rawMouseY
     *  The y value of the mouse event
     * @param dragData
     *  The drag data of the rectangle unit
     */
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

        // getScene().setOnKeyPressed(keyEvent -> {
        //     if (keyEvent.getCode() == KeyCode.SHIFT) {
        //         System.out.println("yo!");
        //     }
        // });
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

    private int clamp(int value, int minValue, int maxValue) {
        return Math.min(Math.max(value, minValue), maxValue);
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
