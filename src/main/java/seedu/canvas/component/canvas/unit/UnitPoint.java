package seedu.canvas.component.canvas.unit;

public class UnitPoint {

    private int unitX;
    private int unitY;

    public UnitPoint(int unitX, int unitY) {
        this.unitX = unitX;
        this.unitY = unitY;
    }

    public UnitPoint() {
        this(0, 0);
    }

    public int getUnitX() {
        return unitX;
    }

    public int getUnitY() {
        return unitY;
    }

    public void setPoint(int unitX, int unitY) {
        this.unitX = unitX;
        this.unitY = unitY;
    }

}
