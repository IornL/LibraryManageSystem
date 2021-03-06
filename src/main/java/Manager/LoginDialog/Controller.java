package Manager.LoginDialog;

import Manager.Model.Admin;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Created by Iron on 2016/12/7.
 */
public class Controller {
    public JFXPasswordField passwordTextField;
    public JFXTextField idTextField;
    public JFXButton loginButton;
    public StackPane rootPane;
    public JFXDialog errorDialog;

    public void initialize() {
        errorDialog.setDialogContainer(rootPane);
        idTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(idTextField.getText().isEmpty() || passwordTextField.getText().isEmpty());
        });
        passwordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(idTextField.getText().isEmpty() || passwordTextField.getText().isEmpty());
        });
        System.out.println("Init success");
    }

    public void handlechange(InputMethodEvent inputMethodEvent) {
    }

    public void handleExit(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    public void handleLogin(ActionEvent event) throws IOException {
        String id = idTextField.getText();
        String password = passwordTextField.getText();
        Admin result = Admin.selectAdmin(id, password);
        if (result == null) {
            errorDialog.show();
            passwordTextField.setText("");
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/MainWindow.fxml"));
            Stage stage = new Stage();
            stage.setTitle("岂因祸福避趋之");
            stage.setScene(new Scene(root,800, 600));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);
            stage.show();
            stage = (Stage) rootPane.getScene().getWindow();
            stage.hide();
        }
    }

    public void handleCloseDialog(ActionEvent event) {
        errorDialog.close();
    }
}
