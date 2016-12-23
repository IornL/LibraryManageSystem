package Manager.MainWindow;

import Manager.Model.Book;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.jfoenix.controls.*;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;

import Manager.shared.Util;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iron on 2016/12/12.
 */
public class BookReturnController {

    public Label messageLabel;
    private List<Book> bookList;

    public JFXTextField bookIdTextField;
    @FXML
    private JFXTreeTableView<Book> bookListView;

    public void initialize() {
        bookList = new ArrayList<>();
        JFXTreeTableColumn<Book, Integer> idColumn = new JFXTreeTableColumn<>("图书编号");
        idColumn.setCellValueFactory(param -> new SimpleObjectProperty<Integer>(param.getValue().getValue().getId()));

        JFXTreeTableColumn<Book, String> titleColumn = new JFXTreeTableColumn<>("书名");
        titleColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getTitle()));

        JFXTreeTableColumn<Book, String> publisherColumn = new JFXTreeTableColumn<>("出版社");
        publisherColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getPublisher()));

        bookListView.getColumns().addAll(idColumn, titleColumn, publisherColumn);
        flashTable();
    }

    public void handleReturnBook(ActionEvent event) {
        Book book = null;
        try{
            book = Book.selectBookById(Integer.parseInt(bookIdTextField.getText()));
        }
        catch (NumberFormatException e) {
            Util.setMessageLabel(messageLabel, Util.MESSAGE_ERROR, "您的输入有误，请重新输入");
            return;
        }
        if (book == null) {
            Util.setMessageLabel(messageLabel, Util.MESSAGE_ERROR, "该图书不存在");
            return;
        }
        if (book.getStatus() != Book.STATUS.OUTSIDE){
            Util.setMessageLabel(messageLabel, Util.MESSAGE_ERROR, "该图书已在馆，无需归还");
            return;
        }
        book.setStatus(Book.STATUS.INSIDE);
        book.setBorrower(0);
        book.setBorrowedDate("null");
        book.save();
        bookList.add(book);
        flashTable();
        Util.setMessageLabel(messageLabel, Util.MESSAGE_SUCCESS, "图书归还成功");
    }

    public void flashTable() {
        ObservableList<Book> books = FXCollections.observableArrayList(bookList);
        final TreeItem<Book> root = new RecursiveTreeItem<Book>(books, RecursiveTreeObject::getChildren);
        bookListView.setRoot(root);
        bookListView.setShowRoot(false);
    }
}
