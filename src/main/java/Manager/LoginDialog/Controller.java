package Manager.LoginDialog;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Created by Iron on 2016/12/7.
 */
public class Controller {
    public JFXPasswordField password;
    public JFXTextField id;
    public JFXButton loginButton;
    public AnchorPane rootPane;

    public void initialize() {
        id.textProperty().addListener((observable, oldValue, newValue) -> {loginButton.setDisable(id.getText().isEmpty() || password.getText().isEmpty());});
        password.textProperty().addListener((observable, oldValue, newValue) -> {loginButton.setDisable(id.getText().isEmpty() || password.getText().isEmpty());});
        System.out.println("Init success");
    }

    public void handlechange(InputMethodEvent inputMethodEvent) {
    }

    public void handleExit(ActionEvent event) {
        Stage stage = (Stage)rootPane.getScene().getWindow();
        stage.close();
    }
}
