package seedu.canvas.component.canvas.unit;

import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import seedu.canvas.component.canvas.CanvasGrid;
import seedu.canvas.component.canvas.Direction;

import java.util.ArrayList;
import java.util.Arrays;

public class GroupLineUnit extends LineUnit {

    private static final double OFFSET = CanvasGrid.OFFSET / 2;

    private Direction direction = null;

    private Line startGroupLine;
    private Line endGroupLine;
    private Line middleGroupLine;

    public GroupLineUnit(int unitStartX, int unitStartY, int unitEndX, int unitEndY) {
        super(unitStartX, unitStartY, unitEndX, unitEndY);

        initialiseStyle();
        initialiseEvents();
    }

    @Override
    public ArrayList<Node> getGroup() {
        ArrayList<Node> unitGroup = super.getGroup();
        unitGroup.addAll(Arrays.asList(startGroupLine, endGroupLine, middleGroupLine));
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

        startGroupLine.setEffect(null);
        endGroupLine.setEffect(null);
        middleGroupLine.setEffect(null);
    }

    @Override
    public void colourLine(Color lineColour) {
        super.colourLine(lineColour);
        startGroupLine.setStroke(lineColour);
        endGroupLine.setStroke(lineColour);
        middleGroupLine.setStroke(lineColour);
    }

    @Override
    protected void colour() {
        super.colour();
        startGroupLine.setStroke(canvas.getLineColour());
        endGroupLine.setStroke(canvas.getLineColour());
        middleGroupLine.setStroke(canvas.getLineColour());
    }

    @Override
    protected void initialiseOther() {
        startGroupLine = new Line();
        endGroupLine = new Line();
        middleGroupLine = new Line();
    }

    private void initialiseStyle() {
        startGroupLine.setStrokeWidth(3);
        endGroupLine.setStrokeWidth(3);
        middleGroupLine.setStrokeWidth(3);

        startGroupLine.setStrokeLineCap(StrokeLineCap.ROUND);
        endGroupLine.setStrokeLineCap(StrokeLineCap.ROUND);
        middleGroupLine.setStrokeLineCap(StrokeLineCap.ROUND);

        startGroupLine.startXProperty().bind(startXProperty());
        startGroupLine.startYProperty().bind(startYProperty());

        endGroupLine.startXProperty().bind(endXProperty());
        endGroupLine.startYProperty().bind(endYProperty());

        middleGroupLine.startXProperty().bind(startXProperty().add(endXProperty()).divide(2));
        middleGroupLine.startYProperty().bind(startYProperty().add(endYProperty()).divide(2));

        faceGroupLines();
    }

    private void initialiseEvents() {
        startXProperty().addListener(observable -> faceGroupLines());
        startYProperty().addListener(observable -> faceGroupLines());
        endXProperty().addListener(observable -> faceGroupLines());
        endYProperty().addListener(observable -> faceGroupLines());
    }

    private void highlight(Color colour) {
        startGroupLine.setEffect(new DropShadow(BlurType.GAUSSIAN, colour, 7, 0.5, 0, 0));
        endGroupLine.setEffect(new DropShadow(BlurType.GAUSSIAN, colour, 7, 0.5, 0, 0));
        middleGroupLine.setEffect(new DropShadow(BlurType.GAUSSIAN, colour, 7, 0.5, 0, 0));
    }

    private void faceGroupLines() {
        Direction newDirection = computeDirection();

        if (direction == newDirection) {
            return;
        }

        direction = newDirection;

        switch (newDirection) {
        case WEST:
            faceWest();
            break;
        case EAST:
            faceEast();
            break;
        case NORTH:
            faceNorth();
            break;
        case SOUTH:
            faceSouth();
            break;
        default:
            System.out.println("GroupLineUnit: Invalid direction!!");
            break;
        }
    }

    private void faceWest() {
        resetBind();

        startGroupLine.endXProperty().bind(startXProperty().add(OFFSET));
        startGroupLine.endYProperty().bind(startYProperty());

        endGroupLine.endXProperty().bind(endXProperty().add(OFFSET));
        endGroupLine.endYProperty().bind(endYProperty());

        middleGroupLine.endXProperty().bind(startXProperty().add(endXProperty()).divide(2).subtract(OFFSET));
        middleGroupLine.endYProperty().bind(startYProperty().add(endYProperty()).divide(2));
    }

    private void faceEast() {
        resetBind();

        startGroupLine.endXProperty().bind(startXProperty().subtract(OFFSET));
        startGroupLine.endYProperty().bind(startYProperty());

        endGroupLine.endXProperty().bind(endXProperty().subtract(OFFSET));
        endGroupLine.endYProperty().bind(endYProperty());

        middleGroupLine.endXProperty().bind(startXProperty().add(endXProperty()).divide(2).add(OFFSET));
        middleGroupLine.endYProperty().bind(startYProperty().add(endYProperty()).divide(2));
    }

    private void faceNorth() {
        resetBind();

        startGroupLine.endXProperty().bind(startXProperty());
        startGroupLine.endYProperty().bind(startYProperty().add(OFFSET));

        endGroupLine.endXProperty().bind(endXProperty());
        endGroupLine.endYProperty().bind(endYProperty().add(OFFSET));

        middleGroupLine.endXProperty().bind(startXProperty().add(endXProperty()).divide(2));
        middleGroupLine.endYProperty().bind(startYProperty().add(endYProperty()).divide(2).subtract(OFFSET));
    }

    private void faceSouth() {
        resetBind();

        startGroupLine.endXProperty().bind(startXProperty());
        startGroupLine.endYProperty().bind(startYProperty().subtract(OFFSET));

        endGroupLine.endXProperty().bind(endXProperty());
        endGroupLine.endYProperty().bind(endYProperty().subtract(OFFSET));

        middleGroupLine.endXProperty().bind(startXProperty().add(endXProperty()).divide(2));
        middleGroupLine.endYProperty().bind(startYProperty().add(endYProperty()).divide(2).add(OFFSET));
    }

    private void resetBind() {
        startGroupLine.endXProperty().unbind();
        startGroupLine.endYProperty().unbind();

        endGroupLine.endXProperty().unbind();
        endGroupLine.endYProperty().unbind();

        middleGroupLine.endXProperty().unbind();
        middleGroupLine.endYProperty().unbind();
    }

    private Direction computeDirection() {
        int deltaX = getUnitEndX() - getUnitStartX();
        int deltaY = getUnitEndY() - getUnitStartY();

        if (deltaX > 0 && isWithinBounds(deltaY, -deltaX, deltaX, true)) {
            return Direction.SOUTH;
        } else if (deltaX < 0 && isWithinBounds(deltaY, deltaX, -deltaX, false)) {
            return Direction.NORTH;
        } else if (deltaY > 0 && isWithinBounds(deltaX, -deltaY, deltaY, false)) {
            return Direction.WEST;
        } else {
            return Direction.EAST;
        }
    }

    private boolean isWithinBounds(int value, int lowerBound, int upperBound, boolean isExcludeLower) {
        return isExcludeLower ? (value > lowerBound && value <= upperBound)
                : (value >= lowerBound && value < upperBound);
    }

    private void bringForward() {
        startGroupLine.toFront();
        endGroupLine.toFront();
        middleGroupLine.toFront();
    }
}
