package Manager.MainWindow;

import Manager.Model.Book;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;

import Manager.Model.Reader;

import java.util.List;

/**
 * Created by Iron on 2016/12/12.
 */
public class ReaderManageController {

    public JFXTextField nameTextField;
    public JFXTextField addressTextField;
    public JFXTreeTableView userListView;
    @FXML
    private AnchorPane readerManagePane;


    @FXML
    private AnchorPane addReaderDialog;

    public void initialize() {
        readerManagePane.setVisible(true);
        addReaderDialog.setVisible(false);
        readerManagePane.toFront();

        JFXTreeTableColumn<Reader, Integer> idColumn = new JFXTreeTableColumn<>("卡号");
        idColumn.setCellValueFactory(param -> new SimpleObjectProperty<Integer>(param.getValue().getValue().getId()));

        JFXTreeTableColumn<Reader, String> nameColumn = new JFXTreeTableColumn<>("姓名");
        nameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getName()));

        JFXTreeTableColumn<Reader, String> addressColumn = new JFXTreeTableColumn<>("住址");
        nameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getAddress()));

        JFXTreeTableColumn<Reader, Integer> borrowCountColumn = new JFXTreeTableColumn<>("已借本数");
        borrowCountColumn.setCellValueFactory(param -> new SimpleObjectProperty<Integer>(param.getValue().getValue().getBorrowCount()));


        userListView.getColumns().addAll(idColumn, nameColumn, addressColumn, borrowCountColumn);
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
        //TODO add message for successs info
    }

    @FXML
    void handleReturn(ActionEvent event) {
        readerManagePane.setVisible(true);
        addReaderDialog.setVisible(false);
    }



}
