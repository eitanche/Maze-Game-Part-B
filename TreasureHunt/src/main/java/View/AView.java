package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.awt.*;
import java.io.IOException;

/**
 * This class represents an abstract View class, which is a controller of some window in the application
 */
public abstract class AView implements IView {

    protected MyViewModel viewModel;

    /**
     * open a new window from the current window controller
     * @param view_name The name of the new window fxml
     * @param window_name The title of the new window
     */
    public void openNewWindow(String view_name, String window_name){
        Parent root;
        try {
            //load the new window
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(view_name));
            root = fxmlLoader.load();
            Stage stage = new Stage();
            //can't go to the main window until the new one closes
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(window_name);
            Scene scene = new Scene(root, 700, 700);
            scene.getStylesheets().add("style");
            stage.setScene(scene);
            //bind the height of the new window to its width - to fit the background picture
            stage.minHeightProperty().bind(stage.widthProperty().divide(1.76574));
            stage.maxHeightProperty().bind(stage.widthProperty().divide(1.76574));
            stage.show();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the label style to the unique style
     * @param label the label that is changed
     */
    public void setLableStyle(Label label){
        label.getStyleClass().remove("label");
        label.getStyleClass().add("unique");
    }

    /**
     * open a new window from the current window controller - and set its ViewModel
     * @param viewModel The viewModel of the current window
     * @param view_name The name of the new window fxml
     * @param window_name The title of the new window
     */
    public void openNewWindowModel(MyViewModel viewModel, String view_name, String window_name) {
        Parent root;
        try {
            //load the new window
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(view_name));
            root = fxmlLoader.load();
            Stage stage = new Stage();
            //can't go to the main window until the new one closes
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(window_name);
            Scene scene = new Scene(root, 700, 700);
            scene.getStylesheets().add("style");
            stage.setScene(scene);
            //bind the height of the new window to its width - to fit the background picture
            stage.minHeightProperty().bind(stage.widthProperty().divide(1.76574));
            stage.maxHeightProperty().bind(stage.widthProperty().divide(1.76574));
            //set the new window view model to the current window view model
            IView window = fxmlLoader.getController();
            window.setViewModel(viewModel);
            stage.show();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the current window view model
     * @param viewModel
     */
    public void setViewModel(MyViewModel viewModel){
        this.viewModel = viewModel;
    }
}
