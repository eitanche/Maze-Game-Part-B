package View;

import ViewModel.MyViewModel;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import java.io.*;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

/**
 * MyView controller
 */
public class MyViewController extends AViewMenuBarUsers implements Initializable,Observer {


    //private double zoomFactor=1;
    private boolean dragOnPlayer=false;
    private Timeline timeline = new Timeline();

    /**
     * window size listener
     */
    @FXML
    private InvalidationListener listener = new InvalidationListener(){
        @Override
        public void invalidated(javafx.beans.Observable observable) {
            MazeDisplayer.draw();
        }
    };

    /**
     * invoke when save item pressed on menuBar
     * @param actionEvent
     */
    public void MenuBarSavePressed(javafx.event.ActionEvent actionEvent){
        FileChooser fc = new FileChooser();

        fc.setTitle("Save maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fc.setInitialDirectory(new File("./resources"));
        File chosen = fc.showSaveDialog(null);

        try {
            viewModel.saveMaze(chosen);
        }
        catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Couldn't save Maze");
            alert.show();
        }
    }

    /**
     * sets the view model and add MyViewController as observer
     * @param viewModel the View model that MyView wants to observe on.
     */
    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addObserver(this);
    }

    /**
     * keyPressed after games ends.
     * @param keyEvent any key pressed
     */
    public void keyPressed(KeyEvent keyEvent) {
        try {viewModel.movePlayer(keyEvent);}
        catch (IllegalStateException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Game is over! Create a new game to play");
            alert.show();
        }
        keyEvent.consume();
    }


    /**
     * updates observers
     * @param o
     * @param arg observers
     */
    @Override
    public void update(Observable o, Object arg) {
        String s = (String)arg;
        switch (s){
            case "maze generated":
                mazeGenerated();
                break;
            case "player moved":
                playerMoved();
                break;
            case "maze solved":
                mazeSolved();
                break;
            case "goal reached":
                goalReached();
                break;

        }
    }

    /**
     * means that the user land and the goal state
     */
    private void goalReached() {
        playerMoved();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("You Made Itttt!!!!!!!!");

        String path = "./resources/winScene.mp4";
        Media media = new Media(Paths.get(path).toUri().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(12500);
        mediaPlayer.setAutoPlay(true);

        //set media view
        MediaView mediaView = new MediaView(mediaPlayer);

/*        DoubleProperty mvw = mediaView.fitWidthProperty();
        DoubleProperty mvh = mediaView.fitHeightProperty();*/
        double ratio = mediaView.getFitHeight()/mediaView.getFitWidth();
        mediaView.setFitWidth(605);
        mediaView.setFitHeight(340);
/*        mvw.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
        mvh.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));*/
        if(!playSounds)
            mediaPlayer.setVolume(0);
        mediaView.setPreserveRatio(true);

        Group root = new Group();
        root.getChildren().add(mediaView);
        Scene scene = new Scene(root,593,330);
        //stops music when goal achieved
        Main.stopMusic();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //start music when goal window close
                if(playSounds)
                    Main.startMusic();
                mediaPlayer.stop();
            }
        });
        Button newGameButton = new Button();
        newGameButton.setText("Click For New Game");
        newGameButton.setLayoutX(230);
        newGameButton.setLayoutY(300);
        newGameButton.getStylesheets().add("style");
        newGameButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mediaPlayer.stop();
                if(playSounds)
                    Main.startMusic();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("CreateMazeWindow.fxml"));

                Parent root = null;
                try
                {
                    root = fxmlLoader.load();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                CreateMazeWindow viewController = fxmlLoader.getController();
                viewController.setViewModel(viewModel);
                Scene scene = new Scene(root, 700, 700);

                scene.getStylesheets().add("style");
                stage.setScene(scene);
                stage.setTitle("Maze Creator");
                stage.minHeightProperty().bind(stage.widthProperty().divide(1.76574));
                stage.maxHeightProperty().bind(stage.widthProperty().divide(1.76574));
                stage.setResizable(true);
                stage.show();

            }
        });
        root.getChildren().add(newGameButton);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }


    /**
     * asks for solution from the view Model
     */
    private void mazeSolved() {
        MazeDisplayer.setSolution(viewModel.getSolution());
    }

    /**
     * moves the player and draw
     */
    private void playerMoved() {
        MazeDisplayer.setPlayerPosition(viewModel.getPlayerRow(),viewModel.getPlayerCol());
    }

    /**
     * drawing maze.
     * maze generated, now the user can ask for solution and save the maze
     */
    private void mazeGenerated() {
        solveButton.setDisable(false);
        saveButton.setDisable(false);
        MazeDisplayer.drawMaze(viewModel.getMaze());
        MazeDisplayer.requestFocus();
    }

    /**
     * init:
     * bind MazeDisplayer
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //bind mazeDisplayer size to window size
        MazeDisplayer.widthProperty().bind(MazePane.widthProperty());
        MazeDisplayer.heightProperty().bind(MazePane.heightProperty());
        //every time window reshapes, redraw the maze
        MazeDisplayer.widthProperty().addListener(listener);
        MazeDisplayer.heightProperty().addListener(listener);
        //create solve button
        Label solveLabel = new Label("Solve");
        solveLabel.setOnMouseClicked(mouseEvent->{MenuBarSolvePressed();});
        solveButton.setGraphic(solveLabel);
        //initializing AViewMenuBarUsers controls
        initControls();
        //set menu mute or unmute disable or undisable upon user request
        if(!playSounds) {
            MuteButton.setDisable(true);
            UnMuteButton.setDisable(false);
        }
        else{
            MuteButton.setDisable(false);
            UnMuteButton.setDisable(true);
        }

    }

    /**
     * opens create maze window to inserts maze sizes
     * @param actionEvent clicked on new in menu bar
     */
    public void MenuBarNewPressed(javafx.event.ActionEvent actionEvent) {
        openNewWindowModel(viewModel,"CreateMazeWindow.fxml","Maze Creator");
    }

    /**
     *  checks if the user still drags the player
     * @param event mouse released
     */
    public void mouseDragReleased(MouseEvent event) {
        dragOnPlayer=false;
    }

    /**
     * user mouse drag started
     * @param event mouse drag
     */
    public void mouseDragEntered(MouseEvent event) {
        int rows = viewModel.getMaze().getRows();
        int cols = viewModel.getMaze().getCols();

        double cellHeight = MazePane.localToScene(MazePane.getBoundsInLocal()).getHeight()/rows;
        double cellWidth = MazePane.localToScene(MazePane.getBoundsInLocal()).getWidth()/cols;

        double x = event.getSceneX()-MazePane.localToScene(MazePane.getBoundsInLocal()).getMinX();
        double y = event.getSceneY()-MazePane.localToScene(MazePane.getBoundsInLocal()).getMinY();

        int i = (int)(y/cellHeight);
        int j = (int)(x/cellWidth);
        if (i==MazeDisplayer.getPlayerRow() && j==MazeDisplayer.getPlayerCol())
            dragOnPlayer=true;
    }

    /**
     * moves the player according to mouse movments
     * @param event mouse dragged
     */
    public void mouseDragged(MouseEvent event) {
        if (dragOnPlayer) {
            int rows = viewModel.getMaze().getRows();
            int cols = viewModel.getMaze().getCols();

            double cellHeight = MazePane.localToScene(MazePane.getBoundsInLocal()).getHeight()/rows;
            double cellWidth = MazePane.localToScene(MazePane.getBoundsInLocal()).getWidth()/cols;

            double x = event.getSceneX()-MazePane.localToScene(MazePane.getBoundsInLocal()).getMinX();
            double y = event.getSceneY()-MazePane.localToScene(MazePane.getBoundsInLocal()).getMinY();

            int i = (int) (y / cellHeight);
            int j = (int) (x / cellWidth);

            try {viewModel.setPlayerLoc(i, j);}
            catch (IllegalStateException e) {
                if (i==MazeDisplayer.getPlayerRow() && j==MazeDisplayer.getPlayerCol()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Game is over! Create a new game to play");
                    alert.show();
                }
            }
        }
    }

    /**
     * user clicked on the MazeDisplayer
     * @param mouseEvent mouse clicked
     */
    public void mouseCLicked(MouseEvent mouseEvent) {
        MazeDisplayer.requestFocus();
    }

    /**
     * set solution path on the screen after solve button pressed on menuBar.
     */
    @FXML
    public void MenuBarSolvePressed() {
            viewModel.solveMaze();
    }

    /**
     * zoom in function if control is down.
     * @param scrollEvent
     */
    public void mouseScrolled(ScrollEvent scrollEvent) {

        if(scrollEvent.isControlDown()){
            double zoomFactor = 1.1;
            double deltaY = scrollEvent.getDeltaY();

            if (deltaY < 0){
                zoomFactor = 0.9;
            }
            if(MazePane.getScaleX()*zoomFactor < 1){
                screenMinimumSize();
                return;
            }
            //we take the scroll event locations and calculate the zoom relative to the scroll event (the mazePane can be out of screen);
            double x = (scrollEvent.getSceneX() - (MazePane.localToScene(MazePane.getBoundsInLocal()).getWidth() / 2 + MazePane.localToScene(MazePane.getBoundsInLocal()).getMinX()));
            double y = (scrollEvent.getSceneY() - (MazePane.localToScene(MazePane.getBoundsInLocal()).getHeight() / 2 + MazePane.localToScene(MazePane.getBoundsInLocal()).getMinY()));
            //we calculate the size of scale change in percent
            double size = (MazePane.getScaleX()*zoomFactor / MazePane.getScaleX()) - 1;
            System.out.println(size);

            makeTimeline(size,x,y,zoomFactor);
            MazePane.setScaleX(MazePane.getScaleX() * zoomFactor);
            MazePane.setScaleY(MazePane.getScaleY() * zoomFactor);
            scrollEvent.consume();

        }
    }

    /**
     * enable zoom
     * @param size adding percent
     * @param x relative to scroll event and window sizes
     * @param y relative to scroll event and window sizes
     * @param zoomFactor 1.1 if scroll to the screen and 0.9 else
     */
    private void makeTimeline(double size, double x, double y, double zoomFactor) {
        timeline.getKeyFrames().clear();
        //from -> to
        KeyFrame X1 = new KeyFrame(Duration.millis(1), new KeyValue(MazePane.translateXProperty(), MazePane.getTranslateX() - size * x));
        //zoom
        KeyFrame X2 = new KeyFrame(Duration.millis(1), new KeyValue(MazePane.scaleXProperty(), MazePane.getScaleX()*zoomFactor));
        //from -> to
        KeyFrame Y1 = new KeyFrame(Duration.millis(1), new KeyValue(MazePane.translateYProperty(), MazePane.getTranslateY() - size * y));
        KeyFrame Y2 = new KeyFrame(Duration.millis(1), new KeyValue(MazePane.scaleYProperty(), MazePane.getScaleX()*zoomFactor));
        timeline.getKeyFrames().addAll(X1,Y1,X2,Y2);
        timeline.play();
    }

    /**
     * stops the user from zoom out more than 100%
     */
    private void screenMinimumSize() {
        timeline.getKeyFrames().clear();
        //from -> to
        KeyFrame X1 = new KeyFrame(Duration.millis(1), new KeyValue(MazePane.translateXProperty(), 0));
        KeyFrame X2 = new KeyFrame(Duration.millis(1), new KeyValue(MazePane.scaleXProperty(), 1));
        //from -> to
        KeyFrame Y1 = new KeyFrame(Duration.millis(1), new KeyValue(MazePane.translateYProperty(), 0));
        KeyFrame Y2 = new KeyFrame(Duration.millis(1), new KeyValue(MazePane.scaleYProperty(), 1));
        timeline.getKeyFrames().addAll(X1,Y1,X2,Y2);
        timeline.play();
    }



}
