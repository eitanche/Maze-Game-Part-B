package View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import Server.Configurations;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * options window controller
 */
public class OptionsWindow extends AView implements Initializable {
    public TextField number_of_Threads_TF;
    public ChoiceBox Search_algorithm_ChoseBox;
    public ChoiceBox generate_Algorithm_choiseBox;
    public Label wanted_values;
    public Label lable_generateAlgorithm;
    public Label lable_searchAlgorithm;
    public Label lable_ThreadPoolSize;

    /**
     * set values in choise boxes and changes label style
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //set searching algorithm items
        ArrayList<String> searching_algorithms_names = new ArrayList<>();
        searching_algorithms_names.add("Best First Search");
        searching_algorithms_names.add("Breath First Search");
        searching_algorithms_names.add("Depth First Search");
        ObservableList<String> search_list = FXCollections.observableArrayList(searching_algorithms_names);
        Search_algorithm_ChoseBox.setItems(search_list);

        //set generating algorithm items
        ArrayList<String> generating_algorithms_names = new ArrayList<>();
        generating_algorithms_names.add("My Maze Generator");
        generating_algorithms_names.add("Simple Maze Generator");
        generating_algorithms_names.add("Empty Maze Generator");
        ObservableList<String> generate_list = FXCollections.observableArrayList(generating_algorithms_names);
        generate_Algorithm_choiseBox.setItems(generate_list);
        setLableStyle(wanted_values);
        setLableStyle(lable_generateAlgorithm);
        setLableStyle(lable_searchAlgorithm);
        setLableStyle(lable_ThreadPoolSize);
    }

    /**
     * changes the configuration file at JarFile
     * @param mouseEvent
     */
    public void SubmitValues(MouseEvent mouseEvent) {
        Configurations c = Configurations.getConfigInstance();
        String generateAlgorithm = getBoxAlgorithm(generate_Algorithm_choiseBox);
        String SearchAlgorithm = getBoxAlgorithm(Search_algorithm_ChoseBox);
        String threadPoolSize;
        try {
            threadPoolSize = getThreadPoolSize();
        }
        catch (IllegalArgumentException e){
            return;
        }
        //change configuration file
        c.writeProp(threadPoolSize,generateAlgorithm,SearchAlgorithm);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Configurations changed");
        //close window after alert approved
        alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
            @Override
            public void handle(DialogEvent event) {
                Node node = (Node) mouseEvent.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.close();
            }
        });
        alert.show();


    }

    /**
     * assert numbers only in textField
     * @return string of numbers or null if that's not a number
     */
    private String getThreadPoolSize() {
        String s = number_of_Threads_TF.getText();
        if (s.matches("[0-9]+"))
            return s;
        else if(s.equals(""))
            return null;
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter numbers to the thread pool size");
            alert.show();
            throw new IllegalArgumentException();
        }

    }


    /**
     * returns string without spaces for configuration file
     * @param chosen chosen option from chose box
     * @return string without spaces for configuration file
     */
    private String getBoxAlgorithm(ChoiceBox chosen) {
        Object o = chosen.getSelectionModel().getSelectedItem();
        if (o != null)
            return o.toString().replaceAll("\\s","");
        else
            return null;
    }
}
