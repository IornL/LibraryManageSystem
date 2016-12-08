package Manager.MainWindow;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXDrawersStack;
import com.jfoenix.controls.JFXNodesList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * Created by Iron on 2016/12/7.
 */
public class Controller {


    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private JFXDrawersStack drawerStack;

    @FXML
    public void initialize() throws IOException {
        VBox vBox = FXMLLoader.load(getClass().getResource("/FXML/Sidebar.fxml"));
        drawer.setSidePane(vBox);
    }

    @FXML
    void handleMouseEnterd(MouseEvent event) {
        drawer.open();
    }

    @FXML
    void handleMouseExitd(MouseEvent event) {
        drawer.close();
    }

    public void handleCloseWindow(javafx.event.ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    public void handleMinimize(javafx.event.ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setIconified(true);
    }
}

