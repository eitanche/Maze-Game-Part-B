package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.nio.file.Paths;

public class Main extends Application {

    private static Stage stage;

    /**
     * @return main stage (initalWindow stage)
     */
    public static Stage getPrimaryStage() {
        return stage;
    }

    private MyViewModel viewModel;
    private static MediaPlayer mediaPlayer;
    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("InitalWindow.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("TreasureHunt");

        //set media
        String s = "./resources/Pirates Of The Caribbean Theme Song.mp3";
        Media media = new Media(Paths.get(s).toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        primaryStage.setScene(new Scene(root, 800, 495.6629));
        //1.61426
        //primaryStage.setResizable(false);
        primaryStage.show();

        IModel model = new MyModel();
        viewModel = new MyViewModel(model);
        InitialWindowController viewController = fxmlLoader.getController();
        viewController.setViewModel(viewModel);
    }

    /**
     * stops music
     */
    public static void stopMusic(){
        mediaPlayer.stop();
    }

    /**
     * stops model
     */
    public void stop() {
        viewModel.stop();
    }

    /**
     * starts music
     */
    public static void startMusic(){
        mediaPlayer.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
