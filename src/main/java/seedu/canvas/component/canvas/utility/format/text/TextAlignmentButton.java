package seedu.canvas.component.canvas.utility.format.text;

import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.text.TextBox;
import seedu.canvas.storage.FilePath;
import seedu.canvas.util.ComponentUtil;

import java.util.ArrayList;

public class TextAlignmentButton extends Button {

    private static ArrayList<TextAlignmentButton> textAlignmentButtons = new ArrayList<>();
    private static TextAlignmentButton appliedAlignment = null;
    private boolean isApply = false;

    private String style;
    private String backgroundPath;
    private boolean isEnd;

    public TextAlignmentButton(String style, String backgroundPath, boolean isEnd) {
        super();

        this.style = style;
        this.backgroundPath = backgroundPath;
        this.isEnd = isEnd;

        textAlignmentButtons.add(this);

        initialiseStyle();
        initialiseEvents();
    }

    public TextAlignmentButton(String style, String backgroundPath) {
        this(style, backgroundPath, false);
    }

    public static void enable() {
        textAlignmentButtons.forEach(alignmentButton -> alignmentButton.setDisable(false));
        apply(TextFormatBox.getTextBox().getStyleClass().toString());
    }

    public static void disable() {
        textAlignmentButtons.forEach(TextAlignmentButton::reset);
        textAlignmentButtons.forEach(alignmentButton -> alignmentButton.setDisable(true));
    }

    public static void apply(String style) {
        for (TextAlignmentButton alignmentButton : textAlignmentButtons) {
            if (style.contains(alignmentButton.style)) {
                alignmentButton.apply();
                return;
            }
        }
    }

    private void apply() {
        if (appliedAlignment != null) {
            appliedAlignment.isApply = false;
            appliedAlignment.reset();
        }

        appliedAlignment = this;
        isApply = true;

        setEffect(new ColorAdjust(0, 0, -0.15, 0));
    }

    private void initialiseStyle() {
        ComponentUtil.setBackground(this, backgroundPath);
        ComponentUtil.setStyleClass(this, FilePath.CANVAS_STYLE_PATH, "text-style-button");

        if (isEnd) {
            setStyle("-fx-border-width: 0");
        } else {
            setStyle("-fx-border-width: 0 1px 0 0");
        }

        setDisable(true);
    }

    private void initialiseEvents() {
        setOnMouseEntered(this::onHover);
        setOnMouseExited(this::onUnhover);

        setOnMousePressed(this::onPress);
        setOnMouseReleased(this::onRelease);
    }

    private void onHover(MouseEvent mouseEvent) {
        if (!isApply) {
            setEffect(new ColorAdjust(0, 0, -0.05, 0));
        }
    }

    private void onUnhover(MouseEvent mouseEvent) {
        if (!isApply) {
            reset();
        }
    }

    private void onPress(MouseEvent mouseEvent) {
        setEffect(new ColorAdjust(0, 0, -0.2, 0));
    }

    private void onRelease(MouseEvent mouseEvent) {
        apply();

        TextBox textBox = TextFormatBox.getTextBox();

        if (textBox != null) {
            textBox.applyTextAlignment(style);
            textBox.requestFocus();
        }
    }

    private void reset() {
        setEffect(null);
    }
}
