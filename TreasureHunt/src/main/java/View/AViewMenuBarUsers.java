package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * This class represents an abstract View class, which is a controller of some window in the application
 * which has a menu bar
 */
public abstract class AViewMenuBarUsers extends AView {

    @FXML
    public Menu solveButton;
    public Menu exitButton;
    public Menu helpButton;
    public Menu aboutButton;
    public MenuItem saveButton;
    public GridPane GridPane1;
    public Pane MazePane;
    public MenuBar menuBar;
    public View.MazeDisplayer MazeDisplayer;
    public MenuItem MuteButton;
    public MenuItem UnMuteButton;
    protected static boolean playSounds = true;

    /**
     * The controller will run this function when the new button is pressed
     * @param actionEvent the event that caused the button to activate
     */
    public abstract void MenuBarNewPressed(javafx.event.ActionEvent actionEvent);

    /**
     * Initializes the menu bar buttons
     */
    protected void initControls(){
        //sets OnMouseClicked event for the buttons
        //create help button
        Label helpLabel = new Label("Help");
        helpLabel.setOnMouseClicked(mouseEvent->{MenuBarHelpPressed();});
        helpButton.setGraphic(helpLabel);

        //create about button
        Label aboutLabel = new Label("About");
        aboutLabel.setOnMouseClicked(mouseEvent->{MenuBarAboutPressed();});
        aboutButton.setGraphic(aboutLabel);

        //create exit button
        Label exitLabel = new Label("Exit");
        exitLabel.setOnMouseClicked(mouseEvent->{MenuBarExitPressed(mouseEvent);});
        exitButton.setGraphic(exitLabel);

        //bind the menubar width to window width
        menuBar.prefWidthProperty().bind(GridPane1.widthProperty());
    }

    /**
     * When the load button is pressed, opens the file chooser to load a file
     * @param actionEvent the event that caused the button to activate
     */
    public void MenuBarLoadPressed(javafx.event.ActionEvent actionEvent){
        FileChooser fc = new FileChooser();
        fc.setTitle("Open maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fc.setInitialDirectory(new File("./resources"));
        File chosen = fc.showOpenDialog(null);
        //try to load the maze from the file
        try {
            viewModel.loadMaze(chosen);
        }
        catch (IOException | ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Couldn't open file!");
            alert.show();
        }
        catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("File doesn't contain a legal maze!");
            alert.show();
        }
    }

    /**
     * When the exit button is pressed, close the program
     * @param event the event that caused the button to activate
     */
    public void MenuBarExitPressed(MouseEvent event){
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }

    /**
     * When the properties button is pressed, open the properties window
     * @param actionEvent the event that caused the button to activate
     */
    public void MenuBarPropertiesPressed(javafx.event.ActionEvent actionEvent){
        openNewWindowModel(viewModel,"OptionsWindow.fxml","Options");
    }

    /**
     * When the help button is pressed, open the help window
     */
    public void MenuBarHelpPressed(){
        openNewWindow("HelpWindow.fxml","Help");
    }
    /**
     * When the about button is pressed, open the help window
     */
    public void MenuBarAboutPressed(){
        openNewWindow("AboutWindow.fxml","About");
    }

    /**
     * When the mute button is pressed, stop the music and change the buttons disabled
     * @param actionEvent the event the caused the button to activate
     */
    public void MenuBarMusicOff(ActionEvent actionEvent) {
        Main.stopMusic();
        playSounds = false;
        MuteButton.setDisable(true);
        UnMuteButton.setDisable(false);
    }

    /**
     * When the unmute button is pressed, start the music and change the buttons disabled
     * @param actionEvent the event the caused the button to activate
     */
    public void MenuBarMusicOn(ActionEvent actionEvent) {
        Main.startMusic();
        playSounds = true;
        UnMuteButton.setDisable(true);
        MuteButton.setDisable(false);
    }

}

