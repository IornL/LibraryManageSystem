package Manager.MainWindow;

import Manager.Model.Book;
import Manager.Model.Reader;
import Manager.shared.Util;
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
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by Iron on 2016/12/12.
 */
public class BookBorrowController {

    public Label messageLabel;
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

    public void handleBorrow(ActionEvent event) throws ParseException {
        int bookId = -1, readerId = -1;
        try {
            bookId = Integer.parseInt(bookIdTextField.getText());
            readerId = Integer.parseInt(readerIdTextField.getText());
        } catch (NumberFormatException e) {
            Util.setMessageLabel(messageLabel, Util.MESSAGE_ERROR, "您的输入有误，请重新输入");
        }
        Book book = Book.selectBookById(bookId);
        Reader reader = Reader.selectReaderById(readerId);
        if (book != null && reader != null) {
            if (reader.isFrozen()) {
                Util.setMessageLabel(messageLabel, Util.MESSAGE_ERROR, "该账号已被续");
                return;
            }
            if (reader.checkOverDue()) {
                Util.setMessageLabel(messageLabel, Util.MESSAGE_ERROR, "该账号有超期图书，无法借阅");
                return;
            }
            if (book.getStatus() != Book.STATUS.INSIDE) {
                Util.setMessageLabel(messageLabel, Util.MESSAGE_ERROR, "该书已被外借");
            }
            book.setBorrowedDate(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
            book.setBorrower(reader);
            book.setStatus(Book.STATUS.OUTSIDE);
            book.save();
            flashTable(reader);
            Util.setMessageLabel(messageLabel, Util.MESSAGE_SUCCESS, "借阅成功");
        } else {
                Util.setMessageLabel(messageLabel, Util.MESSAGE_ERROR, "该图书或读者不存在");
        }
    }
}
