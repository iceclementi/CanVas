package seedu.canvas.component.canvas.utility.format.text;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seedu.canvas.component.canvas.text.TextBox;
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

        initialiseEvents();
    }

    public static TextBox getTextBox() {
        return textBox;
    }

    public static void enable(TextBox textBox) {
        TextFormatBox.textBox = textBox;
    }

    public static void disable() {
        TextFormatBox.textBox = null;
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
                new TextStyleButton("bold", FilePath.CANVAS_BOLD_BUTTON_IMAGE_PATH),
                new TextStyleButton("italic", FilePath.CANVAS_ITALIC_BUTTON_IMAGE_PATH),
                new TextStyleButton("underline", FilePath.CANVAS_UNDERLINE_BUTTON_IMAGE_PATH),
                new TextStyleButton("strikethrough", FilePath.CANVAS_STRIKETHROUGH_BUTTON_IMAGE_PATH, true)
        );

        return textStandardStyleBox;
    }

    private HBox generateTextAlignStyleBox() {
        HBox textAlignStyleBox = new HBox();
        ComponentUtil.setStyleClass(textAlignStyleBox, FilePath.CANVAS_STYLE_PATH, "text-style-button-group");

        textAlignStyleBox.getChildren().addAll(
                new TextStyleButton("align-left", FilePath.CANVAS_LEFT_ALIGN_BUTTON_IMAGE_PATH),
                new TextStyleButton("align-centre", FilePath.CANVAS_CENTRE_ALIGN_BUTTON_IMAGE_PATH),
                new TextStyleButton("align-right", FilePath.CANVAS_RIGHT_ALIGN_BUTTON_IMAGE_PATH),
                new TextStyleButton("align-justify", FilePath.CANVAS_JUSTIFY_BUTTON_IMAGE_PATH, true)
        );

        return textAlignStyleBox;
    }

    private void initialiseEvents() {

    }
}
