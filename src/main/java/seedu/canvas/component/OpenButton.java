package seedu.canvas.component;

public class OpenButton extends MenuButton {

    /**
     * Constructor for the open button in the menu.
     *
     * @param backgroundPath
     *  The path to the background of the open button
     */
    public OpenButton(String backgroundPath) {
        super(backgroundPath);

        initialiseEvents();
    }

    private void initialiseEvents() {
        setDisable(true);
    }
}
