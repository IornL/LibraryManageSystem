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
        idColumn.setCellValueFactory(param -> new SimpleObjectProperty<Integer>(param.getValue().getValue().getId()));

        JFXTreeTableColumn<Book, String> titleColumn = new JFXTreeTableColumn<Book, String>("书名");
        titleColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        titleColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getTitle()));
        titleColumn.setOnEditCommit(event -> {
            Book Book = bookListView.getTreeItem(event.getTreeTablePosition().getRow()).getValue();
            Book.setTitle(event.getNewValue());
            Book.save();
        });

        JFXTreeTableColumn<Book, String> publisherColumn = new JFXTreeTableColumn<Book, String>("出版社");
        publisherColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        publisherColumn.setCellValueFactory((param -> new SimpleStringProperty(param.getValue().getValue().getPublisher())));
        publisherColumn.setOnEditCommit(event -> {
            Book Book = bookListView.getTreeItem(event.getTreeTablePosition().getRow()).getValue();
            Book.setPublisher(event.getNewValue());
            Book.save();
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

        JFXTreeTableColumn<Book, String> isbnColumn = new JFXTreeTableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getISBN()));
        isbnColumn.setOnEditCommit(event -> {
            Book book = bookListView.getTreeItem(event.getTreeTablePosition().getRow()).getValue();
            book.setISBN(event.getNewValue());
            book.save();
        });

        JFXTreeTableColumn<Book, Integer> amountColumn = new JFXTreeTableColumn<>("图书总量");
        amountColumn.setCellValueFactory(param -> new SimpleObjectProperty<Integer>(param.getValue().getValue().getAmount()));
        amountColumn.setOnEditCommit(event -> {
            Book book = bookListView.getTreeItem(event.getTreeTablePosition().getRow()).getValue();
            book.setAmount(event.getNewValue());
            book.save();
        });

        JFXTreeTableColumn<Book, String> categoryColumn = new JFXTreeTableColumn<>("分类");
        categoryColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getCategory().getName()));
        // TODO: a cell factory for selectBox

        bookListView.getColumns().setAll(idColumn, titleColumn, publisherColumn, pubDateColumn, isbnColumn, amountColumn, categoryColumn);
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
        Book Book = new Book();
        Book.setTitle(title);
        Book.setPublisher(publisher);
        Book.setPubDate(date);
        Book.save();
        flashTable();
        Util.setMessageLabel(messageLabel, Util.MESSAGE_SUCCESS, "读者添加成功");
    }

    public void handleOpenBookAddDialog(ActionEvent event) {
        bookManagePane.setVisible(false);
        bookAddPane.setVisible(true);
    }

    private void flashTable() {
        List<Book> BookList = Book.selectAllBooks();
        ObservableList<Book> Books = FXCollections.observableArrayList();
        for (Book Book : BookList
                ) {
            Books.add(Book);
        }
        final TreeItem<Book> root = new RecursiveTreeItem<>(Books, RecursiveTreeObject::getChildren);
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
