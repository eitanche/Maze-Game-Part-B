package View;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class represents the controller of the help window
 */
public class HelpWindowController implements Initializable {

    public Label helpLabel;
    public ScrollPane helPane;

    /**
     * on the about window initialization
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //bind the text to the scrollpane, and change its style
        helpLabel.translateXProperty().bind(helPane.widthProperty().subtract(helpLabel.widthProperty()).divide(2));
        helpLabel.getStyleClass().remove("label");
        helpLabel.getStyleClass().add("unique");
    }

}
