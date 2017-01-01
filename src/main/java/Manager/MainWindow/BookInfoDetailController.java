package Manager.MainWindow;

import Manager.Model.Book;
import Manager.Model.BookInfo;
import Manager.shared.SharedController;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.SimpleStyleableStringProperty;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.layout.AnchorPane;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Iron on 2016/12/31.
 */
public class BookInfoDetailController {

    public JFXTreeTableView<Book> bookContentView;

    public void initialize() {
        JFXTreeTableColumn<Book, String> bookIdColumn = new JFXTreeTableColumn<>("图书编号");
        bookIdColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getId()));

        JFXTreeTableColumn<Book, String> bookStatusColumn = new JFXTreeTableColumn<>("图书状态");
        bookStatusColumn.setCellValueFactory(param ->
            new SimpleStringProperty(
                    param.getValue().getValue().isInLibrary() ? "在馆" : "外借"
            )
        );

        JFXTreeTableColumn<Book, String> bookReaderColumn = new JFXTreeTableColumn<>("借阅者");
        bookReaderColumn.setCellValueFactory(param ->
            new SimpleStringProperty(
                    param.getValue().getValue().isInLibrary() ? "N/A" : param.getValue().getValue().getReader().getName()
            )
        );

        JFXTreeTableColumn<Book, String> borrowedDateColumn = new JFXTreeTableColumn<>("借阅日期");
        borrowedDateColumn.setCellValueFactory(param ->
                new SimpleStringProperty(
                        param.getValue().getValue().isInLibrary() ? "N/A" : param.getValue().getValue().getBorrowedDate()
        ));

        JFXTreeTableColumn<Book, String> returnDateColumn = new JFXTreeTableColumn<>("应还日期");
        returnDateColumn.setCellValueFactory(param -> {
            try {
                if(param.getValue().getValue().isInLibrary())
                    return new SimpleStringProperty("N/A");
                Calendar returnCalendar = param.getValue().getValue().getReturnCalendar();
                SimpleDateFormat dateFormat = new SimpleDateFormat("YY-MM-DD");
                return new SimpleStringProperty(dateFormat.format(returnCalendar.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        });
        bookContentView.getColumns().addAll(bookIdColumn, bookStatusColumn, bookReaderColumn, borrowedDateColumn, returnDateColumn);
        JFXTreeTableView bookListView = SharedController.bookManageController.bookListView;
        ObservableList<TreeTablePosition<BookInfo, ?>> selectedBookInfos = bookListView.getSelectionModel().getSelectedCells();
        BookInfo bookInfo = selectedBookInfos.get(0).getTreeItem().getValue();
        List<Book> bookList = Book.selectBookByBookInfo(bookInfo);
        ObservableList<Book> bookObservableList = FXCollections.observableList(bookList);

        final TreeItem<Book> bookTreeItem = new RecursiveTreeItem<Book>(bookObservableList, RecursiveTreeObject::getChildren);
        bookContentView.setShowRoot(false);
        bookContentView.setRoot(bookTreeItem);
    }
}
