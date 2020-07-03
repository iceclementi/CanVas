package seedu.canvas.component.canvas.utility.format.text;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seedu.canvas.component.canvas.text.TextBox;
import seedu.canvas.component.canvas.text.TextStyle;
import seedu.canvas.storage.FilePath;
import seedu.canvas.util.ComponentUtil;

public class TextFormatBox {

    public static TextBox textBox = null;

    private VBox textStyleBox = new VBox();

    private HBox textFormatBox;

    public TextFormatBox(HBox textFormatBox) {
        this.textFormatBox = textFormatBox;
    }

    public void initialise() {
        initialiseTextStyleBox();
        textFormatBox.getChildren().add(textStyleBox);
        textFormatBox.getChildren().add(new TextSizeSpinner());
        textFormatBox.getChildren().add(new TextPalette());

        initialiseEvents();
    }

    public static TextBox getTextBox() {
        return textBox;
    }

    public static void link(TextBox textBox) {
        TextFormatBox.textBox = textBox;
        TextAlignmentButton.apply(textBox.getStyleClass().toString());
    }

    public static void unlink() {
        TextFormatBox.textBox = null;
        TextStyleButton.resetAll();
        TextAlignmentButton.resetAll();
    }

    private void initialiseTextStyleBox() {
        textStyleBox.setAlignment(Pos.CENTER);
        textStyleBox.setSpacing(10);

        textStyleBox.getChildren().addAll(
                generateTextStandardStyleBox(),
                generateTextAlignStyleBox()
        );
    }

    private HBox generateTextStandardStyleBox() {
        HBox textStandardStyleBox = new HBox();
        ComponentUtil.setStyleClass(textStandardStyleBox, FilePath.CANVAS_STYLE_PATH, "text-style-button-group");

        textStandardStyleBox.getChildren().addAll(
                new TextStyleButton(TextStyle.BOLD, FilePath.CANVAS_BOLD_BUTTON_IMAGE_PATH),
                new TextStyleButton(TextStyle.ITALIC, FilePath.CANVAS_ITALIC_BUTTON_IMAGE_PATH),
                new TextStyleButton(TextStyle.UNDERLINE, FilePath.CANVAS_UNDERLINE_BUTTON_IMAGE_PATH),
                new TextStyleButton(TextStyle.STRIKETHROUGH, FilePath.CANVAS_STRIKETHROUGH_BUTTON_IMAGE_PATH, true)
        );

        return textStandardStyleBox;
    }

    private HBox generateTextAlignStyleBox() {
        HBox textAlignStyleBox = new HBox();
        ComponentUtil.setStyleClass(textAlignStyleBox, FilePath.CANVAS_STYLE_PATH, "text-style-button-group");

        textAlignStyleBox.getChildren().addAll(
                new TextAlignmentButton(TextStyle.ALIGN_LEFT, FilePath.CANVAS_LEFT_ALIGN_BUTTON_IMAGE_PATH),
                new TextAlignmentButton(TextStyle.ALIGN_CENTRE, FilePath.CANVAS_CENTRE_ALIGN_BUTTON_IMAGE_PATH),
                new TextAlignmentButton(TextStyle.ALIGN_RIGHT, FilePath.CANVAS_RIGHT_ALIGN_BUTTON_IMAGE_PATH),
                new TextAlignmentButton(TextStyle.ALIGN_JUSTIFY, FilePath.CANVAS_JUSTIFY_BUTTON_IMAGE_PATH, true)
        );

        return textAlignStyleBox;
    }

    private void initialiseEvents() {

    }
}
