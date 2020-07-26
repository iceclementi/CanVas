package seedu.canvas.component.canvas.utility.format.text;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import seedu.canvas.component.canvas.text.TextBoxContent;
import seedu.canvas.storage.FilePath;
import seedu.canvas.util.CanvasMath;
import seedu.canvas.util.ComponentUtil;

public class TextSizeSpinner extends VBox {

    private static int currentSize = 12;
    private static final int[] sizes =
        {8, 9, 10, 11, 12, 14, 16, 18, 20, 24, 28, 32, 36, 40, 44, 48, 54, 60, 66, 72, 80, 88, 96};

    private static TextField spinnerText;
    private static Button incrementButton;
    private static Button decrementButton;

    public TextSizeSpinner() {
        super();

        initialiseStyle();
        initialise();
    }

    public static int getSize() {
        return currentSize;
    }

    public static void increment() {
        if (currentSize < 96) {
            for (int size : sizes) {
                if (size > currentSize) {
                    spinnerText.setText(String.valueOf(size));
                    currentSize = size;
                    break;
                }
            }
            updateTextSize();
        }

        updateTextSize();
    }

    public static void decrement() {
        if (currentSize > 8) {
            int previousSize = 8;
            for (int size : sizes) {
                if (size < currentSize) {
                    previousSize = size;
                } else {
                    break;
                }
            }
            spinnerText.setText(String.valueOf(previousSize));
            currentSize = previousSize;
            updateTextSize();
        }

        updateTextSize();
    }

    public static void applySize(String sizeText, int size) {
        spinnerText.setText(sizeText);
        currentSize = size;
    }

    public static void resetToDefault() {
        spinnerText.setText("12");
        currentSize = 12;
    }

    public static void enable() {
        spinnerText.setDisable(false);
        incrementButton.setDisable(false);
        decrementButton.setDisable(false);
    }

    public static void disable() {
        spinnerText.setText("");
        spinnerText.setDisable(true);
        incrementButton.setDisable(true);
        decrementButton.setDisable(true);
    }

    private void initialise() {
        initialiseSpinnerText();
        initialiseButtons();

        getChildren().addAll(incrementButton, spinnerText, decrementButton);

        spinnerText.setDisable(true);
        incrementButton.setDisable(true);
        decrementButton.setDisable(true);
    }

    private void initialiseSpinnerText() {
        spinnerText = new TextField();
        ComponentUtil.setStyleClass(spinnerText, FilePath.CANVAS_STYLE_PATH, "text-size-spinner-text");

        spinnerText.setText("");
        spinnerText.textProperty().addListener(observable -> {
            if (spinnerText.getText().length() > 4) {
                spinnerText.setText(spinnerText.getText(0, 4));
            }
        });

        spinnerText.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String input = spinnerText.getText();
                setInputSize(input);
                requestFocus();
            }
        });

        spinnerText.focusedProperty().addListener(observable -> {
            if (!spinnerText.isFocused()) {
                String input = spinnerText.getText();
                setInputSize(input);
            }
        });
    }

    private void initialiseButtons() {
        incrementButton = new Button();
        decrementButton = new Button();

        initialiseButtonStyle(incrementButton, true);
        initialiseButtonStyle(decrementButton, false);

        incrementButton.setOnMouseReleased(mouseEvent -> increment());
        decrementButton.setOnMouseReleased(mouseEvent -> decrement());
    }

    private void initialiseButtonStyle(Button button, boolean isIncrement) {
        button.setPrefSize(40, 10);
        button.setMaxSize(40, 10);
        button.setPadding(new Insets(2));
        if (isIncrement) {
            button.setGraphic(new Polygon(4, 0, 0, 6, 8, 6));
        } else {
            button.setGraphic(new Polygon(0, 0, 8, 0, 4, 6));
        }
    }

    private void initialiseStyle() {
        setAlignment(Pos.CENTER);
    }

    private void setInputSize(String input) {
        try {
            int inputSize = Integer.parseInt(input);
            int restrictedSize = CanvasMath.clamp(inputSize, 8, 96);
            spinnerText.setText(String.valueOf(restrictedSize));
            currentSize = restrictedSize;
            updateTextSize();
        } catch (NumberFormatException e) {
            spinnerText.setText(String.valueOf(currentSize));
        }
    }

    private static void updateTextSize() {
        TextBoxContent content = TextFormatBox.getTextBox().getContent();
        if (content != null) {
            content.applyTextSize(currentSize);
            content.requestFocus();
        }
    }
}
