package View;

import ViewModel.MyViewModel;

import java.awt.*;

public interface IView {
    /**
     * open a new window from the current window controller
     * @param view_name The name of the new window fxml
     * @param window_name The title of the new window
     */
    void openNewWindow(String view_name, String window_name);

    /**
     * Sets the current views a view model
     * @param viewModel The view model to set
     */
    void setViewModel(MyViewModel viewModel);

    /**
     * Sets the label style to the unique style
     * @param label the label that is changed
     */
    void setLableStyle(Label label);
    /**
     * open a new window from the current window controller - and set its ViewModel
     * @param viewModel The viewModel of the current window
     * @param view_name The name of the new window fxml
     * @param window_name The title of the new window
     */
    void openNewWindowModel(MyViewModel viewModel, String view_name, String window_name);
}
