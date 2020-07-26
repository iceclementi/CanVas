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

        moveHandle = new TextBoxMoveHandle(textBox);
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
        getHandles().forEach(CanvasHandle::interactSingle);
    }

    public void focusSingle() {
        bringForward();
        setStroke(Color.LIGHTGREEN);
        getHandles().forEach(CanvasHandle::focusSingle);
    }

    public void interactMultiple() {
        bringForward();
        setStroke(Color.CADETBLUE);
        moveHandle.interactMultiple();
    }

    public void focusMultiple() {
        bringForward();
        setStroke(Color.GREEN);
        moveHandle.focusMultiple();
    }

    public void unfocus() {
        setVisible(false);
        getHandles().forEach(CanvasHandle::unfocus);
    }

    private void initialiseStyle() {
        setStrokeWidth(6);
        setOpacity(0.4);
        setFill(null);

        xProperty().bind(textBox.xProperty());
        yProperty().bind(textBox.yProperty());
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
        textBox.getContent().toFront();
    }
}
