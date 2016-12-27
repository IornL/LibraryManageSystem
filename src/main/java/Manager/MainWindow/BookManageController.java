package Manager.MainWindow;

import Manager.Model.Book;
import Manager.Model.Reader;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import Manager.shared.Util;

/**
 * Created by Iron on 2016/12/9.
 */
public class BookManageController {

    public JFXTextField titleTextField;

    public JFXTextField publisherTextFiled;

    public JFXDatePicker pubDatePicker;

    public JFXPopup popup;

    public VBox popupContentVBox;
    public Label messageLabel;

    @FXML
    private JFXTreeTableView<Book> bookListView;

    @FXML
    private AnchorPane bookManagePane;

    @FXML
    private AnchorPane bookAddPane;

    public void initialize() {
        popup.setSource(bookListView);
        popup.setContent(popupContentVBox);

        JFXTreeTableColumn<Book, Integer> idColumn = new JFXTreeTableColumn<Book, Integer>("图书编号");
//        idColumn.setPrefWidth(97.14);
        idColumn.setCellValueFactory(param -> new SimpleObjectProperty<Integer>(param.getValue().getValue().getId()));

        JFXTreeTableColumn<Book, String> titleColumn = new JFXTreeTableColumn<Book, String>("书名");
//        titleColumn.setPrefWidth(97.14);
        titleColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        titleColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getTitle()));
        titleColumn.setOnEditCommit(event -> {
            Book book = bookListView.getTreeItem(event.getTreeTablePosition().getRow()).getValue();
            book.setTitle(event.getNewValue());
            book.save();
        });

        JFXTreeTableColumn<Book, String> publisherColumn = new JFXTreeTableColumn<Book, String>("出版社");
//        publisherColumn.setPrefWidth(97.14);
        publisherColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        publisherColumn.setCellValueFactory((param -> new SimpleStringProperty(param.getValue().getValue().getPublisher())));
        publisherColumn.setOnEditCommit(event -> {
            Book book = bookListView.getTreeItem(event.getTreeTablePosition().getRow()).getValue();
            book.setPublisher(event.getNewValue());
            book.save();
        });
        JFXTreeTableColumn<Book, String> pubDateColumn = new JFXTreeTableColumn<Book, String>("出版日期");
        //TODO use datePicker as cellFactory
        pubDateColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        pubDateColumn.setCellValueFactory((param -> new SimpleStringProperty(param.getValue().getValue().getPubDate())));
        pubDateColumn.setOnEditCommit(event -> {
            Book book = bookListView.getTreeItem(event.getTreeTablePosition().getRow()).getValue();
            book.setPubDate(event.getNewValue());
            book.save();
        });
        JFXTreeTableColumn<Book, String> statusColumn = new JFXTreeTableColumn<Book, String>("图书状态");
//        statusColumn.setPrefWidth(97.14);
        statusColumn.setCellValueFactory(param -> {
            try {
                if (param.getValue().getValue().isOverdue()) {
                    return new SimpleStringProperty("超期");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            switch (param.getValue().getValue().getStatus()) {
                case Book.STATUS.OUTSIDE:
                    return new SimpleStringProperty("外借");
                case Book.STATUS.FORBIDDEN:
                    return new SimpleStringProperty("禁借");
                default:
                    return new SimpleStringProperty("在馆");
            }
        });

        JFXTreeTableColumn<Book, String> borrowerColumn = new JFXTreeTableColumn<Book, String>("借阅者");
        borrowerColumn.setPrefWidth(97.14);
        borrowerColumn.setCellValueFactory(param -> {
            Reader borrower = param.getValue().getValue().getBorrower();
            if (borrower != null) {
                return new SimpleStringProperty(borrower.getName());
            } else {
                return new SimpleStringProperty("N/A");
            }
        });

        JFXTreeTableColumn<Book, String> borrowedDateColumn = new JFXTreeTableColumn<Book, String>("借阅日期");
//        borrowedDateColumn.setPrefWidth(97.14);
        borrowedDateColumn.setCellValueFactory(param -> {
            String date = !Objects.equals(param.getValue().getValue().getBorrowedDate(), "null") ? param.getValue().getValue().getBorrowedDate() : "N/A";
            return new SimpleStringProperty(date);
        });


        JFXTreeTableColumn<Book, String> returnDateColumn = new JFXTreeTableColumn<Book, String>("应还日期");
//        returnDateColumn.setPrefWidth(95);
        returnDateColumn.setCellValueFactory(param -> {
            String dateString = null;
            if (!Objects.equals(param.getValue().getValue().getBorrowedDate(), "null")) {
                try {
                    Calendar calendar = param.getValue().getValue().getReturnCalendar();
                    dateString = new SimpleDateFormat("YYYY-MM-DD").format(calendar.getTime());
                } catch (ParseException e) {
                    dateString = "N/A";
                }
            }
            return new SimpleStringProperty(dateString);
        });

        bookListView.getColumns().setAll(idColumn, titleColumn, publisherColumn, pubDateColumn, statusColumn, borrowerColumn, borrowedDateColumn, returnDateColumn);
        for (TreeTableColumn<Book, ?> column : bookListView.getColumns()
                ) {

        }
        ;
        flashTable();
    }

    public void handleAddBook(ActionEvent event) throws IOException {
        String title = titleTextField.getText();
        String publisher = publisherTextFiled.getText();
        LocalDate pubDate = pubDatePicker.getValue();
        String date = pubDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        Book book = new Book();
        book.setTitle(title);
        book.setPublisher(publisher);
        book.setPubDate(date);
        book.save();
        flashTable();
        Util.setMessageLabel(messageLabel, Util.MESSAGE_SUCCESS, "读者添加成功");
    }

    public void handleOpenBookAddDialog(ActionEvent event) {
        bookManagePane.setVisible(false);
        bookAddPane.setVisible(true);
    }

    private void flashTable() {
        List<Book> bookList = Book.getAllBooks();
        ObservableList<Book> books = FXCollections.observableArrayList();
        for (Book book : bookList
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
        flashTable();
    }

    public void handleDeleteRow(ActionEvent event) {
        ObservableList<TreeTablePosition<Book, ?>> selectedBooks = bookListView.getSelectionModel().getSelectedCells();
        for (TreeTablePosition position :
                selectedBooks) {
            TreeItem<Book> book = bookListView.getTreeItem(position.getRow());
            book.getParent().getChildren().remove(book);
            book.getValue().delete();
            popup.close();
        }
    }

    public void handleShowPopup(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            popup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, mouseEvent.getX(), mouseEvent.getY());
        }
    }
}
