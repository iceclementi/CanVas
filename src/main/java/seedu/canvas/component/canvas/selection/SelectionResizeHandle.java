package seedu.canvas.component.canvas.selection;

import seedu.canvas.component.canvas.Direction;
import seedu.canvas.component.canvas.unit.CanvasHandle;

public class SelectionResizeHandle extends CanvasHandle {

    private SelectionWrapper wrapper;

    public SelectionResizeHandle(SelectionWrapper wrapper, Direction location) {
        super(location);
        this.wrapper = wrapper;
    }

    private void initialiseStyle() {
        switch (location) {
        case NORTHWEST:
            centerXProperty().bind(wrapper.xProperty());
            centerYProperty().bind(wrapper.yProperty());
            break;
        case NORTHEAST:
            centerXProperty().bind(wrapper.xProperty().add(wrapper.widthProperty()));
            centerYProperty().bind(wrapper.yProperty());
            break;
        case SOUTHWEST:
            centerXProperty().bind(wrapper.xProperty());
            centerYProperty().bind(wrapper.yProperty().add(wrapper.heightProperty()));
            break;
        case SOUTHEAST:
            centerXProperty().bind(wrapper.xProperty().add(wrapper.widthProperty()));
            centerYProperty().bind(wrapper.yProperty().add(wrapper.heightProperty()));
            break;
        default:
            System.out.println("SelectionResizeHandle: Invalid location!");
            break;
        }

        unfocus();
    }

}
