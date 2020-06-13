package seedu.canvas.component.canvas.unit;

public class UnitPoint {

    private int unitX;
    private int unitY;

    public UnitPoint(int unitX, int unitY) {
        this.unitX = unitX;
        this.unitY = unitY;
    }

    public int getUnitX() {
        return unitX;
    }

    public void setUnitX(int unitX) {
        this.unitX = unitX;
    }

    public int getUnitY() {
        return unitY;
    }

    public void setUnitY(int unitY) {
        this.unitY = unitY;
    }
}
