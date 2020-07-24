package seedu.canvas.component.canvas.unit;

import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import seedu.canvas.component.canvas.CanvasGrid;

import java.util.ArrayList;

public class AnchorLineUnit extends LineUnit {

    private static final double OFFSET = CanvasGrid.OFFSET / 2;

    private Boolean isVertical = null;

    private Line startAnchorLine;
    private Line endAnchorLine;

    public AnchorLineUnit(int unitStartX, int unitStartY, int unitEndX, int unitEndY) {
        super(unitStartX, unitStartY, unitEndX, unitEndY);

        initialiseStyle();
        initialiseEvents();
    }

    @Override
    public ArrayList<Node> getGroup() {
        ArrayList<Node> unitGroup = super.getGroup();
        unitGroup.add(startAnchorLine);
        unitGroup.add(endAnchorLine);
        return unitGroup;
    }

    @Override
    public void interactSingle() {
        bringForward();
        highlight(Color.CORNFLOWERBLUE);

        super.interactSingle();
    }

    @Override
    public void focusSingle() {
        bringForward();
        highlight(Color.LIGHTGREEN);

        super.focusSingle();
    }

    @Override
    public void interactMultiple() {
        bringForward();
        highlight(Color.CADETBLUE);

        super.interactMultiple();
    }

    @Override
    public void focusMultiple() {
        bringForward();
        highlight(Color.GREEN);

        super.focusMultiple();
    }

    @Override
    public void unfocus() {
        super.unfocus();

        startAnchorLine.setEffect(null);
        endAnchorLine.setEffect(null);
    }

    @Override
    public void colourLine(Color lineColour) {
        super.colourLine(lineColour);
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

    private void highlight(Color colour) {
        startAnchorLine.setEffect(new DropShadow(BlurType.GAUSSIAN, colour, 7, 0.5, 0, 0));
        endAnchorLine.setEffect(new DropShadow(BlurType.GAUSSIAN, colour, 7, 0.5, 0, 0));
    }

    private void adjustAnchorLines() {
        if (isVertical == checkVertical()) {
            return;
        }

        isVertical = checkVertical();

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

    private Boolean checkVertical() {
        if (getUnitStartX() < getUnitEndX()) {
            return true;
        } else if (getUnitStartX() > getUnitEndX()) {
            return getUnitStartY() == getUnitEndY();
        } else {
            return false;
        }
    }

    private void bringForward() {
        startAnchorLine.toFront();
        endAnchorLine.toFront();
    }
}
