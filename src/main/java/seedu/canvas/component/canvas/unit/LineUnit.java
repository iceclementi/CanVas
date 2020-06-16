package seedu.canvas.component.canvas.unit;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.Direction;
import seedu.canvas.component.canvas.TheCanvas;

import java.util.ArrayList;
import java.util.Arrays;

public class LineUnit extends Line {

    private TheCanvas canvas = TheCanvas.getInstance();

    private IntegerProperty unitStartX = new SimpleIntegerProperty();
    private IntegerProperty unitStartY = new SimpleIntegerProperty();
    private IntegerProperty unitEndX = new SimpleIntegerProperty();
    private IntegerProperty unitEndY = new SimpleIntegerProperty();

    private LineResizeHandle resizeHandleWest = new LineResizeHandle(this, Direction.WEST);
    private LineResizeHandle resizeHandleEast = new LineResizeHandle(this, Direction.EAST);

    public LineUnit(int unitStartX, int unitStartY, int unitEndX, int unitEndY) {
        super();

        initialiseStyle();

        this.unitStartX.set(unitStartX);
        this.unitStartY.set(unitStartY);
        this.unitEndX.set(unitEndX);
        this.unitEndY.set(unitEndY);

        // canvas.addLine(this);

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
        return new ArrayList<>(Arrays.asList(this, resizeHandleWest, resizeHandleEast));
    }

    public void interact() {
        toFront();

        resizeHandleWest.interact();
        resizeHandleEast.interact();
    }

    public void focus() {
        toFront();

        resizeHandleWest.focus();
        resizeHandleEast.focus();
    }

    public void unfocus() {
        resizeHandleWest.unfocus();
        resizeHandleEast.unfocus();
    }

    // public boolean isIntersect(int pointX, int pointY) {
    //     return (pointX >= unitStartX.get() && pointX <= unitStartX.get() + unitEndX.get())
    //             && (pointY >= unitStartY.get() && pointY <= unitStartY.get() + unitEndY.get());
    // }

    public void scale(int newUnitEndX, int newUnitEndY) {
        unitEndX.set(clamp(newUnitEndX, CanvasGrid.MIN_X, CanvasGrid.MAX_X));
        unitEndY.set(clamp(newUnitEndY, CanvasGrid.MIN_Y, CanvasGrid.MAX_Y));
    }

    public void move(int newUnitStartX, int newUnitStartY) {

        int width = Math.abs(unitEndX.get() - unitStartX.get());

        if ((unitEndX.get() >= unitStartX.get())) {
            unitStartX.set(clamp(newUnitStartX, 0, CanvasGrid.MAX_X - width));
        } else {
            unitStartX.set(clamp(newUnitStartX, width, CanvasGrid.MAX_X));
        }

        unitEndX.set(unitStartX.get() + width);

        int height = Math.abs(unitEndY.get() - unitStartY.get());

        if ((unitEndY.get() >= unitStartY.get())) {
            unitStartY.set(clamp(newUnitStartY, 0, CanvasGrid.MAX_Y - height));
        } else {
            unitStartY.set(clamp(newUnitStartY, height, CanvasGrid.MAX_Y));
        }

        unitEndY.set(unitStartY.get() + height);
    }

    private void initialiseStyle() {
        colour();
        setStrokeWidth(3);

        startXProperty().bind(unitStartX.multiply(CanvasGrid.OFFSET));
        startYProperty().bind(unitStartY.multiply(CanvasGrid.OFFSET));
        endXProperty().bind(unitEndX.multiply((CanvasGrid.OFFSET)));
        endYProperty().bind(unitEndY.multiply(CanvasGrid.OFFSET));

        unfocus();
    }

    private void colour() {
        setStroke(canvas.getLineColour());
    }

    private void colour(Paint lineColour, Paint fillColor) {
        setStroke(lineColour);
    }

    private void initialiseEvents() {
        LineUnitEventManager lineUnitEventManager = new LineUnitEventManager();

        addEventFilter(MouseEvent.MOUSE_PRESSED, lineUnitEventManager.getOnMousePressed());
        addEventFilter(MouseEvent.MOUSE_DRAGGED, lineUnitEventManager.getOnMouseDragged());
        addEventFilter(MouseEvent.MOUSE_RELEASED, lineUnitEventManager.getOnMouseReleased());
        addEventFilter(MouseEvent.MOUSE_CLICKED, lineUnitEventManager.getOnMouseClicked());
    }

    private int clamp(int value, int minValue, int maxValue) {
        return Math.min(Math.max(value, minValue), maxValue);
    }

    private void swapPropertyValues(IntegerProperty firstProperty, IntegerProperty secondProperty) {
        int temporaryValue = firstProperty.get();
        firstProperty.set(secondProperty.get());
        secondProperty.set(temporaryValue);
    }
}
