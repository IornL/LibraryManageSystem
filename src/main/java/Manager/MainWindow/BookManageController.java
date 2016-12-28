package Manager.MainWindow;

import Manager.Model.BookInfo;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    private JFXTreeTableView<BookInfo> bookListView;

    @FXML
    private AnchorPane bookManagePane;

    @FXML
    private AnchorPane bookAddPane;

    public void initialize() {
        popup.setSource(bookListView);
        popup.setContent(popupContentVBox);

        JFXTreeTableColumn<BookInfo, Integer> idColumn = new JFXTreeTableColumn<BookInfo, Integer>("图书编号");
        idColumn.setCellValueFactory(param -> new SimpleObjectProperty<Integer>(param.getValue().getValue().getId()));

        JFXTreeTableColumn<BookInfo, String> titleColumn = new JFXTreeTableColumn<BookInfo, String>("书名");
        titleColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        titleColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getTitle()));
        titleColumn.setOnEditCommit(event -> {
            BookInfo Book = bookListView.getTreeItem(event.getTreeTablePosition().getRow()).getValue();
            Book.setTitle(event.getNewValue());
            Book.save();
        });

        JFXTreeTableColumn<BookInfo, String> publisherColumn = new JFXTreeTableColumn<BookInfo, String>("出版社");
        publisherColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        publisherColumn.setCellValueFactory((param -> new SimpleStringProperty(param.getValue().getValue().getPublisher())));
        publisherColumn.setOnEditCommit(event -> {
            BookInfo Book = bookListView.getTreeItem(event.getTreeTablePosition().getRow()).getValue();
            Book.setPublisher(event.getNewValue());
            Book.save();
        });

        JFXTreeTableColumn<BookInfo, String> pubDateColumn = new JFXTreeTableColumn<BookInfo, String>("出版日期");
        //TODO use datePicker as cellFactory
        pubDateColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        pubDateColumn.setCellValueFactory((param -> new SimpleStringProperty(param.getValue().getValue().getPubDate())));
        pubDateColumn.setOnEditCommit(event -> {
            BookInfo book = bookListView.getTreeItem(event.getTreeTablePosition().getRow()).getValue();
            book.setPubDate(event.getNewValue());
            book.save();
        });

        JFXTreeTableColumn<BookInfo, String> isbnColumn = new JFXTreeTableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getISBN()));
        isbnColumn.setOnEditCommit(event -> {
            BookInfo book = bookListView.getTreeItem(event.getTreeTablePosition().getRow()).getValue();
            book.setISBN(event.getNewValue());
            book.save();
        });

        JFXTreeTableColumn<BookInfo, Integer> amountColumn = new JFXTreeTableColumn<>("图书总量");
        amountColumn.setCellValueFactory(param -> new SimpleObjectProperty<Integer>(param.getValue().getValue().getAmount()));
        amountColumn.setOnEditCommit(event -> {
            BookInfo book = bookListView.getTreeItem(event.getTreeTablePosition().getRow()).getValue();
            book.setAmount(event.getNewValue());
            book.save();
        });

        JFXTreeTableColumn<BookInfo, Integer> restColumn = new JFXTreeTableColumn<>("图书存量");
        restColumn.setCellValueFactory(param -> new SimpleObjectProperty<Integer>(param.getValue().getValue().getRest()));
        JFXTreeTableColumn<BookInfo, String> categoryColumn = new JFXTreeTableColumn<>("分类");
        categoryColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getCategory().getName()));
        // TODO: a cell factory for selectBox

        bookListView.getColumns().setAll(idColumn, titleColumn, publisherColumn, pubDateColumn, isbnColumn, categoryColumn, amountColumn,  restColumn);
        for (TreeTableColumn<BookInfo, ?> column : bookListView.getColumns()
                ) {
        }
        flashTable();
    }

    public void handleAddBook(ActionEvent event) throws IOException {
        String title = titleTextField.getText();
        String publisher = publisherTextFiled.getText();
        LocalDate pubDate = pubDatePicker.getValue();
        String date = pubDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        BookInfo Book = new BookInfo();
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
        List<BookInfo> BookList = BookInfo.selectAllBooks();
        ObservableList<BookInfo> Books = FXCollections.observableArrayList();
        for (BookInfo Book : BookList
                ) {
            Books.add(Book);
        }
        final TreeItem<BookInfo> root = new RecursiveTreeItem<>(Books, RecursiveTreeObject::getChildren);
        bookListView.setRoot(root);
        bookListView.setShowRoot(false);
    }

    public void handleCancelAddBook(ActionEvent event) {
        bookAddPane.setVisible(false);
        bookManagePane.setVisible(true);
        flashTable();
    }

    public void handleDeleteRow(ActionEvent event) {
        ObservableList<TreeTablePosition<BookInfo, ?>> selectedBooks = bookListView.getSelectionModel().getSelectedCells();
        for (TreeTablePosition position :
                selectedBooks) {
            TreeItem<BookInfo> book = bookListView.getTreeItem(position.getRow());
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
