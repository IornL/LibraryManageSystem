package Manager.MainWindow;

import Manager.Model.Book;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.util.Callback;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * Created by Iron on 2016/12/9.
 */
public class BookManageController {

    public JFXTextField titleTextField;
    public JFXTextField publisherTextFiled;
    public JFXDatePicker pubDatePicker;
    @FXML
    private JFXTreeTableView<Book> bookListView;

    @FXML
    private TreeTableColumn<Book, String> name;

    @FXML
    private AnchorPane bookManagePane;

    @FXML
    private AnchorPane bookAddPane;

    public void initialize() {

        JFXTreeTableColumn<Book, Integer> idColumn = new JFXTreeTableColumn<Book, Integer>("图书编号");
        idColumn.setPrefWidth(97.14);
        idColumn.setCellValueFactory(param -> new SimpleObjectProperty<Integer>(param.getValue().getValue().getId()));

        JFXTreeTableColumn<Book, String> titleColumn = new JFXTreeTableColumn<Book, String>("书名");
        titleColumn.setPrefWidth(97.14);
        titleColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getTitle()));


        JFXTreeTableColumn<Book, String> publisherColumn = new JFXTreeTableColumn<Book, String>("出版社");
        publisherColumn.setPrefWidth(97.14);
        publisherColumn.setCellValueFactory((param -> new SimpleStringProperty(param.getValue().getValue().getPublisher())));

        JFXTreeTableColumn<Book, String> statusColumn = new JFXTreeTableColumn<Book, String>("图书状态");
        statusColumn.setPrefWidth(97.14);
        statusColumn.setCellValueFactory(param -> {
            try {
                if (param.getValue().getValue().isOverdue()) {
                    return new SimpleStringProperty("超期");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            switch (param.getValue().getValue().getStatus()) {
                case 1: return new SimpleStringProperty("外借");
                case 2: return new SimpleStringProperty("禁借");
                default: return new SimpleStringProperty("在馆");
            }
        });

        JFXTreeTableColumn<Book, String> borrowerColumn = new JFXTreeTableColumn<Book, String>("借阅者");
        borrowerColumn.setPrefWidth(97.14);
        borrowerColumn.setCellValueFactory(param -> {
            int borrower = param.getValue().getValue().getBorrower();
            if (borrower != 0) {
                return new SimpleStringProperty(Integer.toString(borrower));
            }
            else return new SimpleStringProperty("N/A");
        });

        JFXTreeTableColumn<Book, String> borrowedDateColumn = new JFXTreeTableColumn<Book, String>("借阅日期");
        borrowedDateColumn.setPrefWidth(97.14);
        borrowedDateColumn.setCellValueFactory(param -> {
            String date = !Objects.equals(param.getValue().getValue().getBorrowedDate(), "null") ? param.getValue().getValue().getBorrowedDate() : "N/A";
            return new SimpleStringProperty(date);
        });


        JFXTreeTableColumn<Book, String> returnDateColumn = new JFXTreeTableColumn<Book, String>("应还日期");
        returnDateColumn.setPrefWidth(97.14);
        returnDateColumn.setCellValueFactory(param -> {
            Calendar returnCalendar = null;
            try {
                returnCalendar = param.getValue().getValue().getReturnCalendar();
            } catch (ParseException e) {
                return new SimpleStringProperty("N/A");
            }
            String returnDate = String.format("%d-%d-%d",returnCalendar.get(Calendar.YEAR), returnCalendar.get(Calendar.MONTH), returnCalendar.get(Calendar.DATE));
            return new SimpleStringProperty(returnDate);
        });

        bookListView.getColumns().setAll(idColumn, titleColumn, publisherColumn, statusColumn, borrowerColumn, borrowedDateColumn, returnDateColumn);
        flashTable();
    }

    public void handleAddBook(ActionEvent event) throws IOException {
        String  title = titleTextField.getText();
        String publisher = publisherTextFiled.getText();
        LocalDate pubDate = pubDatePicker.getValue();
        String date = pubDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        Book book = new Book();
        book.setTitle(title);
        book.setPublisher(publisher);
        book.setPubDate(date);
        book.save();
        flashTable();
    }

    public void handleOpenBookAddDialog(ActionEvent event) {
        bookManagePane.setVisible(false);
        bookAddPane.setVisible(true);
    }

    private void flashTable() {
        List<Book> bookList = Book.getAllBooks();
        ObservableList<Book> books = FXCollections.observableArrayList();
        for (Book book:bookList
                ) {
            books.add(book);
        }
        final TreeItem<Book> root = new RecursiveTreeItem<>(books, RecursiveTreeObject::getChildren);
        bookListView.setRoot(root);
        bookListView.setShowRoot(false);
    }

    public void handleCancelAddBook(ActionEvent event) {
        bookAddPane.setVisible(false);
        bookManagePane.setVisible(true);
    }

}
