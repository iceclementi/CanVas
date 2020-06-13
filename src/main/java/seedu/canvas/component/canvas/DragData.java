package seedu.canvas.component.canvas;

import seedu.canvas.component.canvas.unit.ModelUnit;

import java.util.ArrayList;

public class DragData {

    private double mouseAnchorX = 0;
    private double mouseAnchorY = 0;

    private double translateAnchorX = 0;
    private double translateAnchorY = 0;

    private ArrayList<ModelUnit> copiedUnits = new ArrayList<>();
    private Direction copyDirection = null;

    public DragData() {
    }

    public double getMouseAnchorX() {
        return mouseAnchorX;
    }

    public void setMouseAnchorX(double mouseAnchorX) {
        this.mouseAnchorX = mouseAnchorX;
    }

    public double getMouseAnchorY() {
        return mouseAnchorY;
    }

    public void setMouseAnchorY(double mouseAnchorY) {
        this.mouseAnchorY = mouseAnchorY;
    }

    public double getTranslateAnchorX() {
        return translateAnchorX;
    }

    public void setTranslateAnchorX(double translateAnchorX) {
        this.translateAnchorX = translateAnchorX;
    }

    public double getTranslateAnchorY() {
        return translateAnchorY;
    }

    public void setTranslateAnchorY(double translateAnchorY) {
        this.translateAnchorY = translateAnchorY;
    }

    public ArrayList<ModelUnit> getCopiedUnits() {
        return copiedUnits;
    }

    public Direction getCopyDirection() {
        return copyDirection;
    }

    public void setCopyDirection(Direction copyDirection) {
        this.copyDirection = copyDirection;
    }

    /**
     * Resets the drag data to its default values.
     */
    public void reset() {
        mouseAnchorX = 0;
        mouseAnchorY = 0;
        translateAnchorX = 0;
        translateAnchorY = 0;

        copiedUnits = new ArrayList<>();
        copyDirection = null;
    }
}
