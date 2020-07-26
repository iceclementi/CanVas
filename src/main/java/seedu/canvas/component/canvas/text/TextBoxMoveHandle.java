package seedu.canvas.component.canvas.text;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.CanvasHandle;

public class TextBoxMoveHandle extends CanvasHandle {

    private TextBox textBox;

    public TextBoxMoveHandle(TextBox textBox) {
        super(null);
        this.textBox = textBox;

        initialiseStyle();
        initialiseEvents();
    }

    private void initialiseStyle() {
        centerXProperty().bind(textBox.xProperty().add(textBox.widthProperty().divide(2)));
        centerYProperty().bind(textBox.yProperty().add(textBox.heightProperty().divide(2)));

        setCursor(Cursor.MOVE);

        unfocus();
    }

    private void initialiseEvents() {
        addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown()) {
                addMousePressEvent(textBox, mouseEvent);

                mouseEvent.consume();
            }
        });

        setOnMouseReleased(mouseEvent -> {
            addMouseReleaseEvent(textBox);

            mouseEvent.consume();
        });

        setOnMouseDragged(mouseEvent -> {
            addMouseDragEvent(textBox, mouseEvent);

            mouseEvent.consume();
        });
    }
}
