package seedu.canvas.component.canvas.unit;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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

    private IntegerProperty maxX = new SimpleIntegerProperty();
    private IntegerProperty maxY = new SimpleIntegerProperty();

    public RectangleUnit(double x, double y, double width, double height) {
        super(x, y, width, height);

        startX = x;
        startY = y;

        pointX = ((int) Math.round(x / CanvasGrid.OFFSET));
        pointY = ((int) Math.round(y / CanvasGrid.OFFSET));
        widthUnit = (int) Math.round(width / CanvasGrid.OFFSET);
        heightUnit = (int) Math.round(height / CanvasGrid.OFFSET);

        maxX.set(CanvasGrid.MAX_X - widthUnit);
        maxY.set(CanvasGrid.MAX_Y - heightUnit);

        CanvasGrid.selectRectangleAnchorPoints(this, pointX, pointY, widthUnit, heightUnit);

        initialiseStyle();
        initialiseEvents();
    }

    public int getWidthUnit() {
        return widthUnit;
    }

    public void setWidthUnit(int widthUnit) {
        this.widthUnit = widthUnit;
    }

    public int getHeightUnit() {
        return heightUnit;
    }

    public void setHeightUnit(int heightUnit) {
        this.heightUnit = heightUnit;
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

    private int clampX(int value) {
        return Math.min(Math.max(value, 0), maxX.get());
    }

    private int clampY(int value) {
        return Math.min(Math.max(value, 0), maxY.get());
    }

    public void snapX(double translateX) {
        int newPointX = clampX((int) Math.round((startX + translateX) / CanvasGrid.OFFSET));

        // System.out.println(String.format("RectangleUnit X: %s %s", getTranslateX(), getTranslateY()));

        if (newPointX != pointX) {
            pointX = newPointX;
            CanvasGrid.selectRectangleAnchorPoints(this, pointX, pointY, widthUnit, heightUnit);
            setTranslateX(pointX * CanvasGrid.OFFSET - startX);
        }
    }

    public void snapY(double translateY) {
        int newPointY = clampY((int) Math.round((startY + translateY) / CanvasGrid.OFFSET));

        // System.out.println(String.format("RectangleUnit Y: %s %s", getTranslateX(), getTranslateY()));

        if (newPointY != pointY) {
            pointY = newPointY;
            CanvasGrid.selectRectangleAnchorPoints(this, pointX, pointY, widthUnit, heightUnit);
            setTranslateY(pointY * CanvasGrid.OFFSET - startY);
        }
    }
}
