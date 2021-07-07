package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

/**
 * This class represents the controller of the create maze window
 */
public class CreateMazeWindow extends AView implements Initializable {

    public TextField new_maze_rows;
    public TextField new_maze_columns;
    public Label label_mazeSize;
    public Label label_rowNum;
    public Label label_colNum;
    private MyViewModel myViewModel;

    /**
     * When the generate button is pressed, asks the model to generate a new maze
     * @param actionEvent The event the caused the button to activate
     */
    public void generateMaze(Event actionEvent) {
        try {
            int rows = Integer.valueOf(new_maze_rows.getText());
            int cols = Integer.valueOf(new_maze_columns.getText());

            myViewModel.generateMaze(rows,cols);
            //close the create maze window
            Node node = (Node) actionEvent.getSource();
            Stage thisStage = (Stage) node.getScene().getWindow();
            thisStage.close();

        }
        catch (NumberFormatException | UnknownHostException nfe){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please Enter Numbers Only");
            alert.show();
        }
        catch (IllegalArgumentException invalidargs){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please Enter Numbers bigger than 2");
            alert.show();
        }
    }

    /**
     * Generates a new maze if ENTER is pressed
     * @param keyEvent the key that was pressed
     */
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode()== KeyCode.ENTER)
            generateMaze(keyEvent);
    }

    /**
     * When this window is initialized, set the labels style
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setLableStyle(label_colNum);
        setLableStyle(label_mazeSize);
        setLableStyle(label_rowNum);
    }
}
