package seedu.canvas.component.canvas.draw;

import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import seedu.canvas.component.canvas.CanvasGrid;

import java.util.ArrayList;
import java.util.Arrays;

public class DrawingWrapper extends Rectangle {

    private DrawingMoveHandle moveHandle;

    private Drawing drawing;

    public DrawingWrapper(Drawing drawing) {
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

    public void interactSingle() {
        bringForward();
        highlight(Color.CORNFLOWERBLUE);
        moveHandle.interact();
    }

    public void focusSingle() {
        bringForward();
        highlight(Color.LIGHTGREEN);
        moveHandle.focus();
    }

    public void interactMultiple() {
        drawing.toFront();
        setVisible(false);
        highlight(Color.CADETBLUE);
    }

    public void focusMultiple() {
        drawing.toFront();
        setVisible(false);
        highlight(Color.GREEN);
    }

    public void unfocus() {
        setVisible(false);
        highlight(null);
        moveHandle.unfocus();
    }

    private void initialiseStyle() {
        setStroke(Color.GREY);
        setStrokeWidth(1);

        setFill(Color.LIGHTGREY);
        setOpacity(0.3);

        xProperty().bind(drawing.startXProperty());
        yProperty().bind(drawing.startYProperty());

        widthProperty().bind(drawing.endXProperty().subtract(drawing.startXProperty()));
        heightProperty().bind(drawing.endYProperty().subtract(drawing.startYProperty()));
    }

    private void initialiseEvents() {
    }

    private void highlight(Color colour) {
        if (colour == null) {
            drawing.getDrawingStrokes().forEach(stroke -> stroke.setEffect(null));
        } else {
            drawing.getDrawingStrokes().forEach(stroke ->
                    stroke.setEffect(new DropShadow(BlurType.GAUSSIAN, colour, 7, 0.5, 0, 0)));
        }
    }

    private void bringForward() {
        drawing.toFront();
        toFront();
        setVisible(true);
    }
}
