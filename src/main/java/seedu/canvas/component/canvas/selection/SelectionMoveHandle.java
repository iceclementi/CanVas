package seedu.canvas.component.canvas.selection;

import javafx.scene.Cursor;
import seedu.canvas.component.canvas.unit.CanvasHandle;

public class SelectionMoveHandle extends CanvasHandle {

    private SelectionWrapper wrapper;

    public SelectionMoveHandle(SelectionWrapper wrapper) {
        super(null);
        this.wrapper = wrapper;

        initialiseStyle();
    }

    private void initialiseStyle() {
        centerXProperty().bind(wrapper.xProperty().add(wrapper.widthProperty().divide(2)));
        centerYProperty().bind(wrapper.yProperty().add(wrapper.heightProperty().divide(2)));

        setCursor(Cursor.MOVE);

        unfocus();
    }
}
