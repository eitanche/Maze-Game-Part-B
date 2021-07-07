package View;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class represents the controller of the about window
 */
public class AboutWindow implements Initializable {
   public Label aboutLabel;
   public ScrollPane scrollPane;

    /**
     * on the about window initialization
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //bind the text to the scrollpane, and change its style
        aboutLabel.translateXProperty().bind(scrollPane.widthProperty().subtract(aboutLabel.widthProperty()).divide(2));
        aboutLabel.getStyleClass().remove("label");
        aboutLabel.getStyleClass().add("unique");
    }

}
