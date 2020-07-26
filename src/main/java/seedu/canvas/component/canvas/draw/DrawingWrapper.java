package seedu.canvas.component.canvas.draw;

import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
        moveHandle.interactSingle();
    }

    public void focusSingle() {
        bringForward();
        highlight(Color.LIGHTGREEN);
        moveHandle.focusSingle();
    }

    public void interactMultiple() {
        bringForward();
        highlight(Color.CADETBLUE);
        moveHandle.interactMultiple();
    }

    public void focusMultiple() {
        bringForward();
        highlight(Color.GREEN);
        moveHandle.focusMultiple();
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
