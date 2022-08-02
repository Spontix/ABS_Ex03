package absController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    protected GridPane adminGridPane;

    @FXML
    protected Button increaseYazButton;

    @FXML
    protected Button loadFileButton;

    @FXML
    protected BorderPane LoansBoardPane;

    @FXML
    protected BorderPane CustomerBoardPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }


}
