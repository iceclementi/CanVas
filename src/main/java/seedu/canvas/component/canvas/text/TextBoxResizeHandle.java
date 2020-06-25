package seedu.canvas.component.canvas.text;

import seedu.canvas.component.canvas.Direction;
import seedu.canvas.component.canvas.unit.CanvasHandle;

public class TextBoxResizeHandle extends CanvasHandle {

    private TextBoxWrapper wrapper;

    public TextBoxResizeHandle(TextBoxWrapper wrapper, Direction location) {
        super(location);
        this.wrapper = wrapper;

        initialiseStyle();
    }

    private void initialiseStyle() {
        unfocus();

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
            System.out.println("TextBoxResizeHandle: Invalid location!");
            break;
        }
    }
}
