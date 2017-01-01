package Manager.MainWindow;

import Manager.Model.Book;
import Manager.Model.BookCategory;
import Manager.Model.BookInfo;
import Manager.shared.SharedController;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Manager.shared.Util;
import javafx.stage.Stage;

/**
 * Created by Iron on 2016/12/9.
 */
public class BookManageController {

    public JFXTextField titleTextField;

    public JFXTextField publisherTextFiled;

    public JFXDatePicker pubDatePicker;


    public Label messageLabel;
    public JFXComboBox<BookCategory> categoryComboBox;
    public JFXTextField ISBNTextFiled;
    public JFXTextField amountTextFiled;
    public JFXButton commitButton;

    public JFXTreeTableView<BookInfo> bookListView;

    @FXML
    private AnchorPane bookManagePane;

    @FXML
    private AnchorPane bookUpdatePane;

    public void initialize() {
        SharedController.bookManageController = this;
        List<BookCategory> categories = BookCategory.selectCategories();
        categoryComboBox.getItems().addAll(categories);

        JFXTreeTableColumn<BookInfo, Integer> idColumn = new JFXTreeTableColumn<BookInfo, Integer>("图书编号");
        idColumn.setCellValueFactory(param -> new SimpleObjectProperty<Integer>(param.getValue().getValue().getId()));

        JFXTreeTableColumn<BookInfo, String> titleColumn = new JFXTreeTableColumn<BookInfo, String>("书名");
        titleColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getTitle()));


        JFXTreeTableColumn<BookInfo, String> publisherColumn = new JFXTreeTableColumn<BookInfo, String>("出版社");
        publisherColumn.setCellValueFactory((param -> new SimpleStringProperty(param.getValue().getValue().getPublisher())));

        JFXTreeTableColumn<BookInfo, String> pubDateColumn = new JFXTreeTableColumn<BookInfo, String>("出版日期");
        //TODO use datePicker as cellFactory
        pubDateColumn.setCellValueFactory((param -> new SimpleStringProperty(param.getValue().getValue().getPubDate())));


        JFXTreeTableColumn<BookInfo, String> isbnColumn = new JFXTreeTableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getISBN()));


        JFXTreeTableColumn<BookInfo, Integer> amountColumn = new JFXTreeTableColumn<>("图书总量");
        amountColumn.setCellValueFactory(param -> new SimpleObjectProperty<Integer>(param.getValue().getValue().getAmount()));


        JFXTreeTableColumn<BookInfo, Integer> restColumn = new JFXTreeTableColumn<>("图书存量");
        restColumn.setCellValueFactory(param -> new SimpleObjectProperty<Integer>(param.getValue().getValue().getRest()));
        JFXTreeTableColumn<BookInfo, String> categoryColumn = new JFXTreeTableColumn<>("分类");
        categoryColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getCategory().getName()));

        bookListView.getColumns().setAll(idColumn, titleColumn, publisherColumn, pubDateColumn, isbnColumn, categoryColumn, amountColumn,  restColumn);

        flashTable();
    }
    public void handleInsertOrModifyBook(ActionEvent event) throws IOException {
        if (Objects.equals(commitButton.getText(), "添加")){
            BookInfo bookInfo = new BookInfo();
            String title = titleTextField.getText();
            String publisher = publisherTextFiled.getText();
            LocalDate pubDate = pubDatePicker.getValue();
            BookCategory category = categoryComboBox.getValue();
            int amount = Integer.parseInt(amountTextFiled.getText());
            String date = pubDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
            bookInfo.setTitle(title);
            bookInfo.setPublisher(publisher);
            bookInfo.setPubDate(date);
            bookInfo.setAmount(amount);
            bookInfo.setCategory(category);
            bookInfo.save();
            int index = BookInfo.selectSumOfBookByCategory(category);
            for(int i = 0; i < amount; ++i) {
                Book book = new Book();
                book.setId(bookInfo.getCategory().getPrefix() + (++index));
                book.setBookInfo(bookInfo);
                book.save();
            }
            Util.setMessageLabel(messageLabel, Util.MESSAGE_SUCCESS, "添加成功");
        } else {
            ObservableList<TreeTablePosition<BookInfo, ?>> selectedBooks = bookListView.getSelectionModel().getSelectedCells();
            BookInfo bookInfo = selectedBooks.get(0).getTreeItem().getValue();
            int oldAmount = bookInfo.getAmount();
            String title = titleTextField.getText();
            String publisher = publisherTextFiled.getText();
            LocalDate pubDate = pubDatePicker.getValue();
            BookCategory category = categoryComboBox.getValue();
            int newAmount = Integer.parseInt(amountTextFiled.getText());
            String date = pubDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
            bookInfo.setTitle(title);
            bookInfo.setPublisher(publisher);
            bookInfo.setPubDate(date);
            bookInfo.setCategory(category);
            bookInfo.setAmount(newAmount);
            int index = BookInfo.selectSumOfBookByCategory(category);
            if (newAmount >= oldAmount) {
                for(int z = 0 ; z < newAmount - oldAmount ; ++z) {
                    Book book = new Book();
                    book.setId(bookInfo.getCategory().getPrefix() + (++index));
                    book.setBookInfo(bookInfo);
                    book.save();
                }
            } else {
                List<Book> books = Book.selectBookByBookInfo(bookInfo);
                Stream<Book> insideBookStream = books.stream().filter(b -> b.getReaderId() == 0);
                List<Book> insideBooks = insideBookStream.collect(Collectors.toList());
                insideBookStream.close();
                if (insideBooks.size() < oldAmount - newAmount) {
                    Util.setMessageLabel(messageLabel, Util.MESSAGE_ERROR, "您减少的书籍数量小于在馆的书籍数量");
                    bookInfo.setAmount(oldAmount);
                    amountTextFiled.setText(String.valueOf(oldAmount));
                    return;
                }
                for(int z = 0 ; z < oldAmount - newAmount ; ++z) {
                   Book book = insideBooks.get(z);
                   book.delete();
                }

            }
            bookInfo.save();
            Util.setMessageLabel(messageLabel, Util.MESSAGE_SUCCESS, "修改成功");
        }
        flashTable();
    }

    private void flashTable() {
        List<BookInfo> bookInfoList = BookInfo.selectAllBookInfos();
        ObservableList<BookInfo> Books = FXCollections.observableArrayList();
        for (BookInfo bookInfo : bookInfoList
                ) {
            Books.add(bookInfo);
        }

        final TreeItem<BookInfo> root = new RecursiveTreeItem<>(Books, RecursiveTreeObject::getChildren);
        bookListView.setRoot(root);
        bookListView.setShowRoot(false);
    }

    public void handleCancelInsertOrModifyBook(ActionEvent event) {
        bookUpdatePane.setVisible(false);
        bookManagePane.setVisible(true);
        flashTable();
    }

    public void handleDeleteSelectedBookInfo(ActionEvent event) {
        ObservableList<TreeTablePosition<BookInfo, ?>> selectedBooks = bookListView.getSelectionModel().getSelectedCells();
        for (TreeTablePosition position :
                selectedBooks) {
            TreeItem<BookInfo> bookInfoItem = bookListView.getTreeItem(position.getRow());
            if (bookInfoItem.getValue().getAmount() != bookInfoItem.getValue().getRest()) {
                Util.setMessageLabel(messageLabel, Util.MESSAGE_ERROR, "该类书籍并未全部在馆，无法删除");
                return;
            }
            bookInfoItem.getParent().getChildren().remove(bookInfoItem);
            bookInfoItem.getValue().delete();
        }
    }

    public void handleOpenBookModifyDialog(ActionEvent event) throws ParseException {
        ObservableList<TreeTablePosition<BookInfo, ?>> selectedBooks = bookListView.getSelectionModel().getSelectedCells();
        if(selectedBooks.size() == 0) {
            return;
        }
        BookInfo bookInfo = selectedBooks.get(0).getTreeItem().getValue();
        titleTextField.setText(bookInfo.getTitle());
        publisherTextFiled.setText(bookInfo.getPublisher());
        ISBNTextFiled.setText(bookInfo.getPublisher());
        amountTextFiled.setText(String.valueOf(bookInfo.getAmount()));
        categoryComboBox.setValue(bookInfo.getCategory());
        categoryComboBox.setDisable(true);
        commitButton.setText("修改");
        SimpleDateFormat dateFormat = new SimpleDateFormat("YY-MM-DD");
        pubDatePicker.setValue(dateFormat.parse(bookInfo.getPubDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        bookManagePane.setVisible(false);
        bookUpdatePane.toFront();
        bookUpdatePane.setVisible(true);
    }

    public void handleOpenBookInfoDetail(MouseEvent mouseEvent) throws IOException {
        if(mouseEvent.getClickCount() == 2) {
            AnchorPane detailPane = FXMLLoader.load(getClass().getResource("/FXML/BookInfoDialog.fxml"));
            Stage stage = new Stage();
            stage.setTitle("+1s");
            stage.setScene(new Scene(detailPane));
            stage.show();
        }
    }
}
