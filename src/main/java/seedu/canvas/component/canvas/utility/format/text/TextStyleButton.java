package seedu.canvas.component.canvas.utility.format.text;

import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.text.TextBox;
import seedu.canvas.component.canvas.text.TextStyle;
import seedu.canvas.storage.FilePath;
import seedu.canvas.util.ComponentUtil;

import java.util.ArrayList;

public class TextStyleButton extends Button {

    private static ArrayList<TextStyleButton> textStyleButtons = new ArrayList<>();
    private boolean isApply = false;

    private String style;
    private String backgroundPath;
    private boolean isEnd;

    public TextStyleButton(String style, String backgroundPath, boolean isEnd) {
        super();

        this.style = style;
        this.backgroundPath = backgroundPath;
        this.isEnd = isEnd;

        textStyleButtons.add(this);

        initialiseStyle();
        initialiseEvents();
    }

    public TextStyleButton(String style, String backgroundPath) {
        this(style, backgroundPath, false);
    }

    public static boolean isApply(String style) {
        for (TextStyleButton styleButton : textStyleButtons) {
            if (styleButton.style.equals(style)) {
                return styleButton.isApply;
            }
        }
        return false;
    }

    public static void apply(boolean isBold, boolean isItalic, boolean isUnderline, boolean isStrikethrough) {
        for (TextStyleButton styleButton : textStyleButtons) {
            switch (styleButton.style) {
            case TextStyle.BOLD:
                styleButton.apply(isBold);
                break;
            case TextStyle.ITALIC:
                styleButton.apply(isItalic);
                break;
            case TextStyle.UNDERLINE:
                styleButton.apply(isUnderline);
                break;
            case TextStyle.STRIKETHROUGH:
                styleButton.apply(isStrikethrough);
                break;
            default:
                break;
            }
        }
    }

    public static void enable() {
        textStyleButtons.forEach(styleButton -> styleButton.setDisable(false));
    }

    public static void disable() {
        textStyleButtons.forEach(TextStyleButton::reset);
        textStyleButtons.forEach(styleButton -> styleButton.setDisable(true));
    }

    private void apply(boolean isApply) {
        this.isApply = isApply;

        if (isApply) {
            setEffect(new ColorAdjust(0, 0, -0.15, 0));
        } else {
            reset();
        }
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
        isApply = !isApply;

        if (isApply) {
            setEffect(new ColorAdjust(0, 0, -0.15, 0));
        } else {
            setEffect(new ColorAdjust(0, 0, -0.05, 0));
        }

        TextBox textBox = TextFormatBox.getTextBox();

        if (textBox != null) {
            textBox.applyTextStyle(style);
            textBox.requestFocus();
        }
    }

    private void reset() {
        setEffect(null);
    }
}
