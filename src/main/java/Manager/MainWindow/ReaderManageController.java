package Manager.MainWindow;

import Manager.Model.Reader;
import Manager.shared.SharedController;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import Manager.shared.Util;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Iron on 2016/12/12.
 */
public class ReaderManageController {

    public JFXTextField nameTextField;
    public JFXTextField addressTextField;
    public JFXTreeTableView<Reader> userListView;
    public Label messageLabel;
    @FXML
    private AnchorPane readerManagePane;


    @FXML
    private AnchorPane addReaderDialog;

    public void initialize() {
        SharedController.readerManageController = this;
        readerManagePane.setVisible(true);
        addReaderDialog.setVisible(false);
        readerManagePane.toFront();

        JFXTreeTableColumn<Reader, Integer> idColumn = new JFXTreeTableColumn<>("卡号");
        idColumn.setCellValueFactory(param -> new SimpleObjectProperty<Integer>(param.getValue().getValue().getId()));

        JFXTreeTableColumn<Reader, String> nameColumn = new JFXTreeTableColumn<>("姓名");
        nameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getName()));
        nameColumn.setOnEditCommit(event -> {
            Reader reader = userListView.getTreeItem(event.getTreeTablePosition().getRow()).getValue();
            reader.setName(event.getNewValue());
            reader.save();
        });

        JFXTreeTableColumn<Reader, String> addressColumn = new JFXTreeTableColumn<>("家庭住址");
        addressColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getAddress()));
        addressColumn.setOnEditCommit(event -> {
            Reader reader = userListView.getTreeItem(event.getTreeTablePosition().getRow()).getValue();
            reader.setAddress(event.getNewValue());
            reader.save();
        });

        JFXTreeTableColumn<Reader, Integer> borrowCountColumn = new JFXTreeTableColumn<>("已借本数");
        borrowCountColumn.setCellValueFactory(param -> new SimpleObjectProperty<Integer>(param.getValue().getValue().getBorrowCount()));

        userListView.getColumns().addAll(idColumn, nameColumn, addressColumn,borrowCountColumn);
        flashTable();
    }


    private void flashTable() {
        ObservableList readers = FXCollections.observableList(Reader.getAllReaders());
        final TreeItem<Reader> root = new RecursiveTreeItem<Reader>(readers, RecursiveTreeObject::getChildren);
        userListView.setRoot(root);
        userListView.setShowRoot(false);
    }

    @FXML
    void handleOpenDialog(ActionEvent event) {
        readerManagePane.setVisible(false);
        addReaderDialog.setVisible(true);
        addReaderDialog.toFront();
    }

    @FXML
    void handleAddReader(ActionEvent event) {
        Reader reader = new Reader(nameTextField.getText(), addressTextField.getText());
        reader.save();
        nameTextField.clear();
        addressTextField.clear();
        Util.setMessageLabel(messageLabel, Util.MESSAGE_SUCCESS, "读者添加成功");
    }

    @FXML
    void handleReturn(ActionEvent event) {
        readerManagePane.setVisible(true);
        addReaderDialog.setVisible(false);
        flashTable();
    }

    public void handleOpenContentDialog(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() > 1) {
            AnchorPane detailPane = FXMLLoader.load(getClass().getResource("/FXML/ReaderInfoDialog.fxml"));
            Stage stage = new Stage();
            stage.setTitle("+1s");
            stage.setScene(new Scene(detailPane));
            stage.show();
        }
    }
}
