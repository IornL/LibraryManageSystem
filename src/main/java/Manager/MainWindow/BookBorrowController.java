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
//        JFXTreeTableColumn<Book, Integer> idColumn = new JFXTreeTableColumn<Book, Integer>("图书编号");
//        idColumn.setCellValueFactory(param -> new SimpleObjectProperty<Integer>(param.getValue().getValue().getId()));
//
//        JFXTreeTableColumn<Book, String> titleColumn = new JFXTreeTableColumn<Book, String>("书名");
//        titleColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getTitle()));
//
//        JFXTreeTableColumn<Book, String> publisherColumn = new JFXTreeTableColumn<Book, String>("出版社");
//        publisherColumn.setCellValueFactory((param -> new SimpleStringProperty(param.getValue().getValue().getPublisher())));
//
//        JFXTreeTableColumn<Book, String> returnDateColumn = new JFXTreeTableColumn<>("应还日期");
//
//        bookListView.getColumns().addAll(idColumn, titleColumn, publisherColumn);

    }

    private void flashTable(Reader reader) {
        ObservableList<Book> books = FXCollections.observableArrayList(reader.getBorrowedBooks());
        final TreeItem<Book> root = new RecursiveTreeItem<Book>(books, RecursiveTreeObject::getChildren);
        bookListView.setRoot(root);
        bookListView.setShowRoot(false);
    }

    public void handleBorrow(ActionEvent event) {
        int readerId = 0;
        try{
            readerId = Integer.parseInt(readerIdTextField.getText());
        }
        catch (NumberFormatException e){
            Util.setMessageLabel(messageLabel, Util.MESSAGE_ERROR, "您的输入的卡号有误，请重新输入");
            return;
        }
        Book book = Book.selectBookById(bookIdTextField.getText());
        Reader reader = Reader.selectReaderById(readerId);

        if(reader == null) {
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
        Util.setMessageLabel(messageLabel, Util.MESSAGE_SUCCESS, "借阅成功");
        book.setReader(reader);
        book.save();
//        int bookId = -1, readerId = -1;
//        try {
//            bookId = Integer.parseInt(bookIdTextField.getText());
//            readerId = Integer.parseInt(readerIdTextField.getText());
//        } catch (NumberFormatException e) {
//            Util.setMessageLabel(messageLabel, Util.MESSAGE_ERROR, "您的输入有误，请重新输入");
//        }
//        Book bookLog = Book.selectBookById(bookId);
//        Reader reader = Reader.selectReaderById(readerId);
//        if (bookLog != null && reader != null) {
//            if (reader.isFrozen()) {
//                Util.setMessageLabel(messageLabel, Util.MESSAGE_ERROR, "该账号已被续");
//                return;
//            }
//            if (reader.checkOverDue()) {
//                Util.setMessageLabel(messageLabel, Util.MESSAGE_ERROR, "该账号有超期图书，无法借阅");
//                return;
//            }
//            if (bookLog.getStatus() != Book.STATUS.INSIDE) {
//                Util.setMessageLabel(messageLabel, Util.MESSAGE_ERROR, "该书已被外借");
//            }
//            bookLog.setBorrowedDate(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
//            bookLog.setBorrower(reader);
//            bookLog.setStatus(Book.STATUS.OUTSIDE);
//            bookLog.save();
//            flashTable(reader);
//            Util.setMessageLabel(messageLabel, Util.MESSAGE_SUCCESS, "借阅成功");
//        } else {
//                Util.setMessageLabel(messageLabel, Util.MESSAGE_ERROR, "该图书或读者不存在");
//        }
    }
}
