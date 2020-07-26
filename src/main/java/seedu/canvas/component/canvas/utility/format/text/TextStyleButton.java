package seedu.canvas.component.canvas.utility.format.text;

import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import seedu.canvas.component.canvas.text.TextBoxContent;
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

    public static void resetToDefault() {
        applyTextStyle(false, false, false, false);
    }

    public static void enable() {
        textStyleButtons.forEach(styleButton -> styleButton.setDisable(false));
    }

    public static void disable() {
        textStyleButtons.forEach(TextStyleButton::reset);
        textStyleButtons.forEach(styleButton -> styleButton.setDisable(true));
    }

    public static void applyTextStyle(String textStyle) {
        for (TextStyleButton styleButton : textStyleButtons) {
            if (styleButton.style.equals(textStyle)) {
                styleButton.toggleStyle();
                return;
            }
        }
    }

    public static void applyTextStyle(boolean isBold, boolean isItalic, boolean isUnderline, boolean isStrikethrough) {
        for (TextStyleButton styleButton : textStyleButtons) {
            switch (styleButton.style) {
            case TextStyle.BOLD:
                styleButton.applyTextStyle(isBold);
                break;
            case TextStyle.ITALIC:
                styleButton.applyTextStyle(isItalic);
                break;
            case TextStyle.UNDERLINE:
                styleButton.applyTextStyle(isUnderline);
                break;
            case TextStyle.STRIKETHROUGH:
                styleButton.applyTextStyle(isStrikethrough);
                break;
            default:
                break;
            }
        }
    }

    private void applyTextStyle(boolean isApply) {
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
        toggleStyle();
        if (!isApply) {
            setEffect(new ColorAdjust(0, 0, -0.05, 0));
        }
    }

    private void reset() {
        setEffect(null);
    }

    private void toggleStyle() {
        isApply = !isApply;

        if (isApply) {
            setEffect(new ColorAdjust(0, 0, -0.15, 0));
        } else {
            reset();
        }

        updateTextStyle();
    }

    private void updateTextStyle() {
        TextBoxContent content = TextFormatBox.getTextBox().getContent();

        if (content != null) {
            content.applyTextStyle(style);
            content.requestFocus();
        }
    }
}
