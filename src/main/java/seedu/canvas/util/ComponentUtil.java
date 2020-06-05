package seedu.canvas.util;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Region;

public class ComponentUtil {

    public static void setBackground(Region component, String backgroundPath) {
        component.setBackground(generateBackground(component.getWidth(), component.getHeight(), backgroundPath));
    }

    public static void setStyleClass(Parent component, String cssStylePath, String styleClass) {
        component.getStylesheets().add(ComponentUtil.class.getResource(cssStylePath).toExternalForm());
        component.getStyleClass().add(styleClass);
    }

    public static void setStyleClass(Parent component, String styleClass) {
        component.getStyleClass().add(styleClass);
    }

    private static Background generateBackground(double width, double height, String backgroundPath) {
        Image image = new Image(backgroundPath, width, height, false, true, true);
        BackgroundImage background = new BackgroundImage(
                image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(width, height, true, true, true, false));

        return new Background(background);
    }
}
