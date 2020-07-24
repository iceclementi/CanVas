package seedu.canvas.component.canvas.text;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import seedu.canvas.component.canvas.Direction;
import seedu.canvas.component.canvas.CanvasHandle;

import java.util.ArrayList;
import java.util.Arrays;

public class TextBoxWrapper extends Rectangle {

    private TextBoxMoveHandle moveHandle;
    private TextBoxResizeHandle resizeHandleNW;
    private TextBoxResizeHandle resizeHandleNE;
    private TextBoxResizeHandle resizeHandleSW;
    private TextBoxResizeHandle resizeHandleSE;

    private TextBox textBox;

    public TextBoxWrapper(TextBox textBox) {
        super();
        this.textBox = textBox;

        moveHandle = new TextBoxMoveHandle(this);
        resizeHandleNW = new TextBoxResizeHandle(this, Direction.NORTHWEST);
        resizeHandleNE = new TextBoxResizeHandle(this, Direction.NORTHEAST);
        resizeHandleSW = new TextBoxResizeHandle(this, Direction.SOUTHWEST);
        resizeHandleSE = new TextBoxResizeHandle(this, Direction.SOUTHEAST);

        initialiseStyle();
    }

    public TextBox getTextBox() {
        return textBox;
    }

    public ArrayList<Node> getGroup() {
        ArrayList<Node> group = new ArrayList<>();
        group.add(this);
        group.addAll(getHandles());

        return group;
    }

    public void interactSingle() {
        bringForward();
        setStroke(Color.CORNFLOWERBLUE);
        getHandles().forEach(CanvasHandle::interact);
    }

    public void focusSingle() {
        bringForward();
        setStroke(Color.LIGHTGREEN);
        getHandles().forEach(CanvasHandle::focus);
    }

    public void interactMultiple() {
        bringForward();
        setStroke(Color.CADETBLUE);
    }

    public void focusMultiple() {
        bringForward();
        setStroke(Color.GREEN);
    }

    public void unfocus() {
        setVisible(false);
        getHandles().forEach(CanvasHandle::unfocus);
    }

    private void initialiseStyle() {
        setStrokeWidth(3);
        setOpacity(0.4);
        setFill(null);

        xProperty().bind(textBox.layoutXProperty());
        yProperty().bind(textBox.layoutYProperty());
        widthProperty().bind(textBox.widthProperty());
        heightProperty().bind(textBox.heightProperty());
    }

    private ArrayList<CanvasHandle> getHandles() {
        return new ArrayList<>(
                Arrays.asList(moveHandle, resizeHandleNW, resizeHandleNE, resizeHandleSW, resizeHandleSE));
    }

    private void bringForward() {
        toFront();
        setVisible(true);
        textBox.toFront();
    }
}
