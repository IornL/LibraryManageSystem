package Manager.MainWindow;

import Manager.Model.Book;
import Manager.shared.Util;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;

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
        JFXTreeTableColumn<Book, String> idColumn = new JFXTreeTableColumn<>("图书编号");
        idColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getId()));

        JFXTreeTableColumn<Book, String> titleColumn = new JFXTreeTableColumn<>("书名");
        titleColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getBookInfo().getTitle()));

        JFXTreeTableColumn<Book, String> publisherColumn = new JFXTreeTableColumn<>("出版社");
        publisherColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getBookInfo().getPublisher()));

        bookListView.getColumns().addAll(idColumn, titleColumn, publisherColumn);
        flashTable();
    }

    public void handleReturnBook(ActionEvent event) {
        Book book = Book.selectBookById(bookIdTextField.getText());

        if (book == null) {
            Util.setMessageLabel(messageLabel, Util.MESSAGE_ERROR, "该图书不存在");
            return;
        }

        if (book.getReaderId() == 0) {
            Util.setMessageLabel(messageLabel, Util.MESSAGE_ERROR, "该图书已被归还，无需归还");
            return;
        }
        book.setReader(null);
        Util.setMessageLabel(messageLabel, Util.MESSAGE_SUCCESS, "图书归还成功");
        book.save();
        bookList.add(book);
        flashTable();
    }

    public void flashTable() {
        ObservableList<Book> books = FXCollections.observableArrayList(bookList);
        final TreeItem<Book> root = new RecursiveTreeItem<Book>(books, RecursiveTreeObject::getChildren);
        bookListView.setRoot(root);
        bookListView.setShowRoot(false);
    }
}
