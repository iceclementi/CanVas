package seedu.canvas.component.canvas.unit;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.GridPoint;

public class RectangleUnit extends Rectangle {

    private GridPoint anchorPointNW;
    private GridPoint anchorPointNE;
    private GridPoint anchorPointSW;
    private GridPoint anchorPointSE;

    private double startX;
    private double startY;

    private int pointX;
    private int pointY;
    private int widthUnit;
    private int heightUnit;

    private int maxX;
    private int maxY;

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

    // public int getWidthUnit() {
    //     return widthUnit.get();
    // }
    //
    // public void setWidthUnit(int widthUnit) {
    //     this.widthUnit.set(widthUnit);
    // }
    //
    // public int getHeightUnit() {
    //     return heightUnit.get();
    // }
    //
    // public void setHeightUnit(int heightUnit) {
    //     this.heightUnit.set(heightUnit);
    // }

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

    private void initialiseStyle() {
        setStroke(Color.MIDNIGHTBLUE);
        setStrokeWidth(2);
        setFill(Color.TRANSPARENT);
    }

    private void initialiseEvents() {
        UnitEventManager unitEventManager = new UnitEventManager();

        addEventFilter(MouseEvent.MOUSE_PRESSED, unitEventManager.getOnMousePressedRectangle());
        addEventFilter(MouseEvent.MOUSE_DRAGGED, unitEventManager.getOnMouseDraggedRectangle());
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
}
