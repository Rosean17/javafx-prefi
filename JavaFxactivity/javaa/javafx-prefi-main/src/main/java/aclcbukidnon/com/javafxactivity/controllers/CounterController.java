
package java.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CounterController {

    @FXML
    private Label labelCount;

    private int count = 0;

    @FXML
    private void onPlusClick() {
        count++;
        updateLabel();
    }

    @FXML
    private void onMinusClick() {
        count--;
        updateLabel();
    }

    private void updateLabel() {
        labelCount.setText(String.valueOf(count));
    }
}
