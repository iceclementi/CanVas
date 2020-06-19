package seedu.canvas.component.canvas.draw;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import seedu.canvas.component.canvas.CanvasGrid;

import java.util.ArrayList;
import java.util.Arrays;

public class DrawingSelectionBox extends Rectangle {

    private DrawingMoveHandle moveHandle;

    private Drawing drawing;

    public DrawingSelectionBox(Drawing drawing) {
        super();
        this.drawing = drawing;

        moveHandle = new DrawingMoveHandle(this);

        initialiseStyle();
        initialiseEvents();
    }

    public ArrayList<Node> getGroup() {
        return new ArrayList<>(Arrays.asList(this, moveHandle));
    }

    public Drawing getDrawing() {
        return drawing;
    }

    public void interact() {
        toFront();
        setVisible(true);
        moveHandle.interact();
    }

    public void focus() {
        toFront();
        setVisible(true);
        moveHandle.focus();
    }

    public void unfocus() {
        setVisible(false);
        moveHandle.unfocus();
    }

    public void move(double newX, double newY) {
        double widthOffset = getWidth() / 2;
        double heightOffset = getHeight() / 2;

        double finalNewX = clamp(newX, widthOffset, CanvasGrid.WIDTH - widthOffset);
        double finalNewY = clamp(newY, heightOffset, CanvasGrid.HEIGHT - heightOffset);

        double deltaX = finalNewX - (getX() + widthOffset);
        double deltaY = finalNewY - (getY() + heightOffset);

        drawing.move(deltaX, deltaY);
    }

    private void initialiseStyle() {
        setStroke(Color.MIDNIGHTBLUE);
        setStrokeWidth(1);
        getStrokeDashArray().addAll(6d, 4d);

        setFill(Color.TRANSPARENT);

        xProperty().bind(drawing.startXProperty());
        yProperty().bind(drawing.startYProperty());

        widthProperty().bind(drawing.endXProperty().subtract(drawing.startXProperty()));
        heightProperty().bind(drawing.endYProperty().subtract(drawing.startYProperty()));
    }

    private void initialiseEvents() {

    }

    private double clamp(double value, double minimum, double maximum) {
        return Math.min(Math.max(value, minimum), maximum);
    }
}
