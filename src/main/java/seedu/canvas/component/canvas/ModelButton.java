package seedu.canvas.component.canvas;

import seedu.canvas.component.canvas.ToolButton;

public class ModelButton extends ToolButton {

    /**
     * Constructor for the help button in the menu.
     *
     * @param backgroundPath
     *  The path to the background of the help button
     */
    public ModelButton(String backgroundPath) {
        super(backgroundPath);

        initialiseEvents();
    }

    private void initialiseEvents() {

    }
}
