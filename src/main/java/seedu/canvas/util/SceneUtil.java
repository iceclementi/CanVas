package seedu.canvas.util;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneUtil {

    public static void changeScene(Node component, String scenePath) {
        Stage stage = (Stage) component.getScene().getWindow();

        try {
            FXMLLoader sceneLoader = new FXMLLoader(SceneUtil.class.getResource(scenePath));
            Parent root = sceneLoader.load();
            Scene nextScene = new Scene(root);

            double previousWidth = stage.getWidth();
            double previousHeight = stage.getHeight();

            stage.setScene(nextScene);

            stage.show();

            stage.setWidth(Math.max(nextScene.getWidth(), previousWidth));
            stage.setHeight(Math.max(nextScene.getHeight(), previousHeight));
            centreScene(stage);
        } catch (IOException e) {
            System.out.println("Error changing scene!");
            e.printStackTrace();
        }
    }

    public static void centreScene(Stage stage) {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }

    public static Background generateBackground(String backgroundPath) {
        Image image = new Image(backgroundPath);
        BackgroundImage backgroundImage = new BackgroundImage(
                image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        return new Background(backgroundImage);
    }
}
