package Manager.MainWindow;

import Manager.Model.Book;
import Manager.Model.Reader;
import Manager.shared.SharedController;
import Manager.shared.Util;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Created by Iron on 2016/12/12.
 */
public class ReaderManageController {

    public JFXTextField nameTextField;
    public JFXTextField addressTextField;
    public JFXTreeTableView<Reader> userListView;
    public Label messageLabel;
    public JFXButton submitButton;
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

        JFXTreeTableColumn<Reader, String> addressColumn = new JFXTreeTableColumn<>("家庭住址");
        addressColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getAddress()));
        JFXTreeTableColumn<Reader, Integer> maxBorrowCountColumn = new JFXTreeTableColumn<>("最大借阅数");
        maxBorrowCountColumn.setCellValueFactory(param -> new SimpleObjectProperty<Integer>(param.getValue().getValue().getMaxBorrowCount()));
        userListView.getColumns().addAll(idColumn, nameColumn, addressColumn, maxBorrowCountColumn);
        flashTable();
    }


    public void flashTable() {
        ObservableList readers = FXCollections.observableList(Reader.getAllReaders());
        final TreeItem<Reader> root = new RecursiveTreeItem<Reader>(readers, RecursiveTreeObject::getChildren);
        userListView.setRoot(root);
        userListView.setShowRoot(false);
    }

    @FXML
    void handleOpenInsertDialog(ActionEvent event) {
        submitButton.setText("添加");
        readerManagePane.setVisible(false);
        addReaderDialog.setVisible(true);
        addReaderDialog.toFront();
    }

    @FXML
    void handleInsertOrModifyReader(ActionEvent event) {
        if (Objects.equals(submitButton.getText(), "添加")) {
            Reader reader = new Reader(nameTextField.getText(), addressTextField.getText());
            reader.save();
            nameTextField.clear();
            addressTextField.clear();
            Util.setMessageLabel(messageLabel, Util.MESSAGE_SUCCESS, "读者添加成功");
        } else {
            ObservableList<TreeTablePosition<Reader, ?>> selectedBooks = userListView.getSelectionModel().getSelectedCells();
            if (selectedBooks.size() == 0) {
                return;
            }
            Reader reader = selectedBooks.get(0).getTreeItem().getValue();
            reader.setName(nameTextField.getText());
            reader.setAddress(addressTextField.getText());
            reader.save();
            Util.setMessageLabel(messageLabel, Util.MESSAGE_SUCCESS, "读者信息修改成功");
        }
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

    public void handleOpenModifyDialog(ActionEvent event) {
        ObservableList<TreeTablePosition<Reader, ?>> selectedReaders = userListView.getSelectionModel().getSelectedCells();
        if (selectedReaders.size() == 0) {
            return;
        }
        Reader reader = selectedReaders.get(0).getTreeItem().getValue();
        nameTextField.setText(reader.getName());
        addressTextField.setText(reader.getAddress());
        submitButton.setText("修改");
        readerManagePane.setVisible(false);
        addReaderDialog.setVisible(true);
        addReaderDialog.toFront();
    }

    public void handleDelete(ActionEvent event) {
        ObservableList<TreeTablePosition<Reader, ?>> selectedReaders = userListView.getSelectionModel().getSelectedCells();
        for (TreeTablePosition position : selectedReaders
                ) {
            Reader reader = userListView.getTreeItem(position.getRow()).getValue();
            List<Book> readerList = Book.selectBookByReader(reader);
            if (Book.selectBookByReader(reader).size() != 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("该读者还有书籍外借，无法删除");
                alert.show();
                return;
            }
            reader.delete();
            TreeItem<Reader> readerTreeItem = userListView.getTreeItem(position.getRow());
            readerTreeItem.getParent().getChildren().remove(readerTreeItem);
            flashTable();
        }
    }
}
