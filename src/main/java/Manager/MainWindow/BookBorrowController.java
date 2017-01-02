package Manager.MainWindow;

import Manager.Model.Book;
import Manager.Model.Reader;
import Manager.shared.Util;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    }

    private void flashTable(Reader reader) {
        ObservableList<Book> books = FXCollections.observableArrayList(reader.getBorrowedBooks());
        final TreeItem<Book> root = new RecursiveTreeItem<Book>(books, RecursiveTreeObject::getChildren);
        bookListView.setRoot(root);
        bookListView.setShowRoot(false);
    }

    public void handleBorrow(ActionEvent event) throws ParseException {
        int readerId = 0;
        try {
            readerId = Integer.parseInt(readerIdTextField.getText());
        } catch (NumberFormatException e) {
            Util.setMessageLabel(messageLabel, Util.MESSAGE_ERROR, "您的输入的卡号有误，请重新输入");
            return;
        }
        Book book = Book.selectBookById(bookIdTextField.getText());
        Reader reader = Reader.selectReaderById(readerId);

        if (reader == null) {
            Util.setMessageLabel(messageLabel, Util.MESSAGE_ERROR, "该读者不存在");
            return;
        }

        if (book == null) {
            Util.setMessageLabel(messageLabel, Util.MESSAGE_ERROR, "该图书不存在");
            return;
        }

        if (reader.isFrozen()) {
            Util.setMessageLabel(messageLabel, Util.MESSAGE_ERROR, "该账号已被续,请联系管理员");
            return;
        }
        if (reader.hasOverdueBook()) {
            Util.setMessageLabel(messageLabel, Util.MESSAGE_ERROR, "请归还超期图书后再借阅");
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        book.setBorrowedDate(format.format(new Date()));
        book.setReader(reader);
        book.save();
        Util.setMessageLabel(messageLabel, Util.MESSAGE_SUCCESS, "借阅成功");
    }
}
