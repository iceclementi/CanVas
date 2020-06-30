package seedu.canvas.component.canvas.utility.format.text;

import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.text.TextBox;
import seedu.canvas.storage.FilePath;
import seedu.canvas.util.ComponentUtil;

public class TextStyleButton extends Button {

    private String style;
    private String backgroundPath;
    private boolean isEnd;

    public TextStyleButton(String style, String backgroundPath, boolean isEnd) {
        super();

        this.style = style;
        this.backgroundPath = backgroundPath;
        this.isEnd = isEnd;

        initialiseStyle();
        initialiseEvents();
    }

    public TextStyleButton(String style, String backgroundPath) {
        this(style, backgroundPath, false);
    }

    private void initialiseStyle() {
        ComponentUtil.setBackground(this, backgroundPath);
        ComponentUtil.setStyleClass(this, FilePath.CANVAS_STYLE_PATH, "text-style-button");

        if (isEnd) {
            setStyle("-fx-border-width: 0");
        } else {
            setStyle("-fx-border-width: 0 1px 0 0");
        }
    }

    private void initialiseEvents() {
        setOnMouseEntered(this::onHover);
        setOnMouseExited(this::onUnhover);

        setOnMousePressed(this::onPress);
        setOnMouseReleased(this::onRelease);
    }

    private void onHover(MouseEvent mouseEvent) {
        setEffect(new ColorAdjust(0, 0, -0.05, 0));
    }

    private void onUnhover(MouseEvent mouseEvent) {
        setEffect(null);
    }

    private void onPress(MouseEvent mouseEvent) {
        setEffect(new ColorAdjust(0, 0, -0.2, 0));
    }

    private void onRelease(MouseEvent mouseEvent) {
        setEffect(new ColorAdjust(0, 0, -0.05, 0));

        TextBox textBox = TextFormatBox.getTextBox();

        if (textBox != null) {
            if (style.contains("align")) {
                textBox.applyTextAlignment(style);
            } else {
                textBox.applyTextStyle(style);
            }

            textBox.requestFocus();
        }
    }
}
