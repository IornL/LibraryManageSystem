package Manager.MainWindow;

import Manager.Model.Book;
import Manager.Model.Reader;
import Manager.shared.SharedController;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTablePosition;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Iron on 2017/1/2.
 */
public class ReaderInfoDialogController {
    public JFXTreeTableView<Book> readersBookView;

    public void initialize() {
        JFXTreeTableColumn<Book, String> bookIdColumn = new JFXTreeTableColumn<>("图书编号");
        bookIdColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getId()));

        JFXTreeTableColumn<Book, String> bookStatusColumn = new JFXTreeTableColumn<>("图书状态");
        bookStatusColumn.setCellValueFactory(param -> {
                    String s = null;
                    if (param.getValue().getValue().isInLibrary()) {
                        s = "在馆";
                    } else {
                        try {
                            s = param.getValue().getValue().isOverdue() ? "超期" : "外借";
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    return new SimpleStringProperty(s);
                }
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
                if (param.getValue().getValue().isInLibrary())
                    return new SimpleStringProperty("N/A");
                Calendar returnCalendar = param.getValue().getValue().getReturnCalendar();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                return new SimpleStringProperty(dateFormat.format(returnCalendar.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        });
        readersBookView.getColumns().addAll(bookIdColumn, bookStatusColumn, bookReaderColumn, borrowedDateColumn, returnDateColumn);
        JFXTreeTableView readerListView = SharedController.readerManageController.userListView;
        ObservableList<TreeTablePosition<Reader, ?>> selectedBookInfos = readerListView.getSelectionModel().getSelectedCells();
        Reader reader = selectedBookInfos.get(0).getTreeItem().getValue();
        List<Book> bookList = Book.selectBookByReader(reader);
        ObservableList<Book> bookObservableList = FXCollections.observableList(bookList);
        final TreeItem<Book> treeItem = new RecursiveTreeItem<Book>(bookObservableList, RecursiveTreeObject::getChildren);
        readersBookView.setRoot(treeItem);
    }

}
