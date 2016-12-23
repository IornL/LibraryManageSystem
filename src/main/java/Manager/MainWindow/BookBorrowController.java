package Manager.MainWindow;

import Manager.Model.Book;
import Manager.Model.Reader;
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
import javafx.scene.control.TreeItem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by Iron on 2016/12/12.
 */
public class BookBorrowController {

    @FXML
    private JFXTextField bookIdTextField;

    @FXML
    private JFXTextField readerIdTextField;

    @FXML
    private JFXTreeTableView<Book> bookListView;

    public void initialize() {
        JFXTreeTableColumn<Book, Integer> idColumn = new JFXTreeTableColumn<Book, Integer>("图书编号");
        idColumn.setCellValueFactory(param -> new SimpleObjectProperty<Integer>(param.getValue().getValue().getId()));

        JFXTreeTableColumn<Book, String> titleColumn = new JFXTreeTableColumn<Book, String>("书名");
        titleColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getTitle()));

        JFXTreeTableColumn<Book, String> publisherColumn = new JFXTreeTableColumn<Book, String>("出版社");
        publisherColumn.setCellValueFactory((param -> new SimpleStringProperty(param.getValue().getValue().getPublisher())));

        JFXTreeTableColumn<Book, String> returnDateColumn = new JFXTreeTableColumn<>("应还日期");

        bookListView.getColumns().addAll(idColumn, titleColumn, publisherColumn);

    }

    private void flashTable(Reader reader) {
        ObservableList<Book> books = FXCollections.observableArrayList(reader.getBorrowedBooks());
        final TreeItem<Book> root = new RecursiveTreeItem<Book>(books, RecursiveTreeObject::getChildren);
        bookListView.setRoot(root);
        bookListView.setShowRoot(false);
    }

    public void handleBorrow(ActionEvent event) {
        int bookId = -1, readerId = -1;
        try {
            bookId = Integer.parseInt(bookIdTextField.getText());
            readerId = Integer.parseInt(readerIdTextField.getText());
        } catch (NumberFormatException e) {
            //TODO: show a dialog for invalid input .
        }
        Book book = Book.selectBookById(bookId);
        Reader reader = Reader.selectReaderById(readerId);
        if (book != null && reader != null) {
            if (reader.isFrozen()) {
                // TODO: show dialog for reader is frozen
                return;
            }
            // TODO: judge whether reader have overdue book

            if (book.getStatus() != Book.STATUS.INSIDE) {
                //TODO: show a dialog for book has been borrowed
            }
            book.setBorrowedDate(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
            book.setBorrower(reader.getId());
            book.setStatus(Book.STATUS.OUTSIDE);
            book.save();
            // TODO: A message for success
        } else {
            //TODO: Add a dialog for book or reader is not exist
        }
        flashTable(reader);
    }
}
