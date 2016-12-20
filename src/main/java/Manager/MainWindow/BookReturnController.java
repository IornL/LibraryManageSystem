package Manager.MainWindow;

import Manager.Model.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.jfoenix.controls.*;

/**
 * Created by Iron on 2016/12/12.
 */
public class BookReturnController {

    public JFXTextField bookIdTextField;
    @FXML
    private JFXTreeTableView<Book> bookListView;


    public void handleReturnBook(ActionEvent event) {
        Book book = null;
        try{
            book = Book.selectBookById(Integer.parseInt(bookIdTextField.getText()));
        }
        catch (NumberFormatException e) {
            // TODO: a dialog for error error book's id
            return;
        }
        if (book == null) {
            //TODO: a dialog for book is not exist
            return;
        }
        if (book.getStatus() != Book.STATUS.OUTSIDE){
            //TODO: a dialog for error book status
            return;
        }
        book.setStatus(Book.STATUS.INSIDE);
        book.setBorrower(0);
        book.setBorrowedDate("null");
        book.save();
    }
}

