package Manager.shared;

import javafx.scene.control.Label;

/**
 * Created by Iron on 2016/12/23.
 */
public class Util {
    public static final int MESSAGE_ERROR = 0;
    public static final int MESSAGE_SUCCESS = 1;
    public static void setMessageLabel(Label label, int type, String content){
        if (type== MESSAGE_ERROR) {
            label.setStyle("-fx-text-fill: red");
        }
        else {
            label.setStyle("-fx-text-fill: lawngreen");
        }
        label.setText(content);
    }
}
