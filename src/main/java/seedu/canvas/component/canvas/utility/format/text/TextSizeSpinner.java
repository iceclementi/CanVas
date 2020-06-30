package seedu.canvas.component.canvas.utility.format.text;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import seedu.canvas.component.canvas.text.TextBox;
import seedu.canvas.util.CanvasMath;

public class TextSizeSpinner extends VBox {

    private int currentSize = 10;
    private final int[] sizes =
        {8, 9, 10, 11, 12, 14, 16, 18, 20, 24, 28, 32, 36, 40, 44, 48, 54, 60, 66, 72, 80, 88, 96};

    private TextField spinnerText;
    private Button incrementButton;
    private Button decrementButton;

    public TextSizeSpinner() {
        super();

        initialiseStyle();
        initialise();
    }

    private void initialise() {
        initialiseSpinnerText();
        initialiseButtons();

        getChildren().addAll(incrementButton, spinnerText, decrementButton);
    }

    private void initialiseSpinnerText() {
        spinnerText = new TextField();
        spinnerText.setPrefSize(40, 30);

        spinnerText.setText(String.valueOf(currentSize));

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

        initialiseButtonStyle(incrementButton);
        initialiseButtonStyle(decrementButton);

        incrementButton.setOnMouseReleased(mouseEvent -> {
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
        });

        decrementButton.setOnMouseReleased(mouseEvent -> {
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
        });

    }

    private void initialiseEvents() {
    }

    private void initialiseButtonStyle(Button button) {
        button.setPrefSize(40, 10);
        button.setMaxSize(40, 10);
        button.setGraphic(new Polygon(0, 0, 5, 0, 0, 5));
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

    private void updateTextSize() {
        TextBox textBox = TextFormatBox.getTextBox();
        if (textBox != null) {
            textBox.applyTextSize(currentSize);
        }
    }
}
