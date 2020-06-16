package seedu.canvas.component.canvas.unit;

import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import seedu.canvas.component.canvas.CanvasGrid;

import java.util.ArrayList;

public class AnchorLineUnit extends LineUnit {

    private static final double OFFSET = CanvasGrid.OFFSET / 2;

    private boolean isVertical = true;

    private Line startAnchorLine;
    private Line endAnchorLine;

    public AnchorLineUnit(int unitStartX, int unitStartY, int unitEndX, int unitEndY) {
        super(unitStartX, unitStartY, unitEndX, unitEndY);

        initialiseStyle();
        initialiseEvents();
    }

    @Override
    public ArrayList<Node> getUnitGroup() {
        ArrayList<Node> unitGroup = super.getUnitGroup();
        unitGroup.add(startAnchorLine);
        unitGroup.add(endAnchorLine);
        return unitGroup;
    }

    @Override
    public void interact() {
        startAnchorLine.toFront();
        endAnchorLine.toFront();
        super.interact();
    }

    @Override
    public void focus() {
        startAnchorLine.toFront();
        endAnchorLine.toFront();
        super.focus();
    }

    @Override
    public void colour(Paint lineColour) {
        super.colour(lineColour);
        startAnchorLine.setStroke(lineColour);
        endAnchorLine.setStroke(lineColour);
    }

    @Override
    protected void colour() {
        super.colour();
        startAnchorLine.setStroke(canvas.getLineColour());
        endAnchorLine.setStroke(canvas.getLineColour());
    }

    @Override
    protected void initialiseOther() {
        startAnchorLine = new Line();
        endAnchorLine = new Line();
    }

    private void initialiseStyle() {
        startAnchorLine.setStrokeWidth(3);
        endAnchorLine.setStrokeWidth(3);
        startAnchorLine.setStrokeLineCap(StrokeLineCap.ROUND);
        endAnchorLine.setStrokeLineCap(StrokeLineCap.ROUND);

        adjustAnchorLines();
    }

    private void initialiseEvents() {
        startXProperty().addListener(observable -> adjustAnchorLines());
        startYProperty().addListener(observable -> adjustAnchorLines());
        endXProperty().addListener(observable -> adjustAnchorLines());
        endYProperty().addListener(observable -> adjustAnchorLines());
    }

    private void adjustAnchorLines() {
        if (isVertical == checkVertical()) {
            return;
        }

        isVertical = !isVertical;

        if (isVertical) {
            faceVertical();
        } else {
            faceHorizontal();
        }
    }

    private void faceVertical() {
        resetBind();

        startAnchorLine.startXProperty().bind(startXProperty());
        startAnchorLine.startYProperty().bind(startYProperty().subtract(OFFSET));
        startAnchorLine.endXProperty().bind(startXProperty());
        startAnchorLine.endYProperty().bind(startYProperty().add(OFFSET));

        endAnchorLine.startXProperty().bind(endXProperty());
        endAnchorLine.startYProperty().bind(endYProperty().subtract(OFFSET));
        endAnchorLine.endXProperty().bind(endXProperty());
        endAnchorLine.endYProperty().bind(endYProperty().add(OFFSET));
    }

    private void faceHorizontal() {
        resetBind();

        startAnchorLine.startXProperty().bind(startXProperty().subtract(OFFSET));
        startAnchorLine.startYProperty().bind(startYProperty());
        startAnchorLine.endXProperty().bind(startXProperty().add(OFFSET));
        startAnchorLine.endYProperty().bind(startYProperty());

        endAnchorLine.startXProperty().bind(endXProperty().subtract(OFFSET));
        endAnchorLine.startYProperty().bind(endYProperty());
        endAnchorLine.endXProperty().bind(endXProperty().add(OFFSET));
        endAnchorLine.endYProperty().bind(endYProperty());
    }

    private void resetBind() {
        startAnchorLine.startXProperty().unbind();
        startAnchorLine.startYProperty().unbind();
        startAnchorLine.endXProperty().unbind();
        startAnchorLine.endYProperty().unbind();

        endAnchorLine.startXProperty().unbind();
        endAnchorLine.startYProperty().unbind();
        endAnchorLine.endXProperty().unbind();
        endAnchorLine.endYProperty().unbind();
    }

    private boolean checkVertical() {
        if (getUnitStartX() < getUnitEndX()) {
            return true;
        } else if (getUnitStartX() > getUnitEndX()) {
            return getUnitStartY() == getUnitEndY();
        } else {
            return false;
        }
    }
}
