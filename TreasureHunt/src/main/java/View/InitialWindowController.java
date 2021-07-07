package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

/**
 * Represents the controller of the Initial window
 */
public class InitialWindowController extends AViewMenuBarUsers implements Initializable,Observer {
    public Button newGameButton;
    public Button menuBarAbout;
    public Pane ImagePane;



    @Override
    /**
     * On the window initialization, activates this function
     */
    public void initialize(URL location, ResourceBundle resources) {
        //init the menu bar buttons
        initControls();
        //bind the height to the width to fit the picture
        Stage stage = Main.getPrimaryStage();
        stage.minHeightProperty().bind(stage.widthProperty().divide(1.614));
        stage.maxHeightProperty().bind(stage.widthProperty().divide(1.614));
        //bind the buttons layout to be in the middle of the window
        newGameButton.layoutYProperty().bind(stage.heightProperty().divide(2).subtract(60));
        menuBarAbout.layoutYProperty().bind(stage.heightProperty().divide(2).subtract(20));

        newGameButton.layoutXProperty().bind(stage.widthProperty().divide(2).subtract(40));
        menuBarAbout.layoutXProperty().bind(stage.widthProperty().divide(2).subtract(45)) ;
    }

    /**
     * Opens the create maze window when new is pressed
     * @param actionEvent the event that caused the button to activate
     */
    public void MenuBarNewPressed(javafx.event.ActionEvent actionEvent) {
        openNewWindowModel(viewModel,"CreateMazeWindow.fxml","Maze Creator");
    }

    /**
     * Loads the game window (myView)
     */
    private void loadMyViewControllerViaMenueBar() {
        Stage stage = Main.getPrimaryStage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("MyView.fxml"));
        Parent root = getRoot(fxmlLoader);
        Scene scene = new Scene(root, 700, 733);
        scene.getStylesheets().add("initialwindowStyle");
        stage.setScene(scene);
        MyViewController viewController = fxmlLoader.getController();
        viewController.setViewModel(viewModel);
        viewController.solveButton.setDisable(false);
        viewController.saveButton.setDisable(false);
        //draws the maze in the new window
        viewController.MazeDisplayer.drawMaze(viewModel.getMaze());
        viewController.MazeDisplayer.requestFocus();
        stage.show();
        //The initial window shouldnt observe the viewModel anymore
        viewModel.deleteObserver(this);
    }

    /**
     * Gets the root of the new fxml that is loaded
     * @param fxmlLoader The fxml Loader
     * @return the root of the new fxml
     */
    private Parent getRoot(FXMLLoader fxmlLoader) {
        Parent root = null;
        try
        {
            root = fxmlLoader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return root;
    }

    /**
     * Sets the current views view model, and adds is as an observer
     * @param viewModel
     */
    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addObserver(this);
    }

    /**
     * Opens the create maze window when new is pressed
     * @param actionEvent the event that caused the button to activate
     */
    public void newGameButtonPressed(javafx.event.ActionEvent actionEvent) {
        openNewWindowModel(viewModel,"CreateMazeWindow.fxml","Maze Creator");
    }

    /**
     * When this contreller gets an update from its observerable, if a maze was generated - load the myView
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg.equals("maze generated")) {
            loadMyViewControllerViaMenueBar();
        }
    }
}
