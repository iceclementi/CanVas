package seedu.canvas.component.menu;

public class HelpButton extends MenuButton {

    /**
     * Constructor for the help button in the menu.
     *
     * @param backgroundPath
     *  The path to the background of the help button
     */
    public HelpButton(String backgroundPath) {
        super(backgroundPath);

        initialiseEvents();
    }

    private void initialiseEvents() {
        setDisable(true);
    }
}
