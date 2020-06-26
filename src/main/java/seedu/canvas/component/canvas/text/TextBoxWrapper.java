package seedu.canvas.component.canvas.text;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import seedu.canvas.component.canvas.Direction;
import seedu.canvas.component.canvas.unit.CanvasHandle;

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
        group.addAll(Arrays.asList(getHandles()));

        return group;
    }

    public void interact() {
        toFront();
        setVisible(true);
        Arrays.stream(getHandles()).forEach(CanvasHandle::interact);
    }

    public void focus() {
        toFront();
        setVisible(true);
        Arrays.stream(getHandles()).forEach(CanvasHandle::focus);
    }

    public void unfocus() {
        setVisible(false);
        Arrays.stream(getHandles()).forEach(CanvasHandle::unfocus);
    }

    private void initialiseStyle() {
        setStroke(Color.LIGHTGRAY);
        setStrokeWidth(2);
        setFill(null);

        xProperty().bind(textBox.layoutXProperty());
        yProperty().bind(textBox.layoutYProperty());
        widthProperty().bind(textBox.widthProperty());
        heightProperty().bind(textBox.heightProperty());
    }

    private CanvasHandle[] getHandles() {
        return new CanvasHandle[]{moveHandle, resizeHandleNW, resizeHandleNE, resizeHandleSW, resizeHandleSE};
    }
}
