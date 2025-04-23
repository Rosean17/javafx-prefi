package java.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CalculatorController {

    @FXML
    private Label display; // Reference to the display label

    private double firstOperand = 0; // To store the first operand
    private String operator = ""; // To store the current operator (+, -, *, /)
    private boolean isNewInput = true; // Flag for checking if a new number is being entered

    @FXML
    public void initialize() {
        // Initialize display with "0" when the application starts
        display.setText("0");
    }

    @FXML
    public void handleScientificFunction(javafx.event.ActionEvent event) {
        Button button = (Button) event.getSource();
        String func = button.getText();
        double value = Double.parseDouble(display.getText());
        double result = 0;

        switch (func) {
            case "sin" -> result = Math.sin(Math.toRadians(value));
            case "cos" -> result = Math.cos(Math.toRadians(value));
            case "tan" -> result = Math.tan(Math.toRadians(value));
            case "√" -> result = Math.sqrt(value);
            case "log" -> result = Math.log10(value);
            case "ln" -> result = Math.log(value);
            case "π" -> {
                display.setText(String.valueOf(Math.PI));
                isNewInput = true;
                return;
            }
        }

        display.setText(doubleToString(result));
        isNewInput = true;
    }


    @FXML
    public void handleButtonAction(javafx.event.ActionEvent event) {
        // Get the button that triggered the event
        Button button = (Button) event.getSource();
        String buttonText = button.getText();

        switch (buttonText) {
            case "+" -> handleOperator("+");
            case "-" -> handleOperator("-");
            case "*" -> handleOperator("*");
            case "/" -> handleOperator("/");
            case "=" -> calculateResult();
            case "CLEAR" -> clearAll();
            case "<---" -> backspace();
            default -> handleNumber(buttonText); // Handle numbers and '.'
        }
    }

    private void handleNumber(String value) {
        // Append digits or '.' to the display text
        if (isNewInput) {
            display.setText(value.equals(".") ? "0." : value); // Handle initial decimal input
            isNewInput = false;
        } else {
            if (value.equals(".") && display.getText().contains(".")) {
                return; // Prevent multiple decimals in one number
            }
            display.setText(display.getText() + value);
        }
    }

    private void handleOperator(String op) {
        // If there's already an operator, calculate the result first
        if (!operator.isEmpty()) {
            calculateResult();
        }

        firstOperand = Double.parseDouble(display.getText());
        operator = op;
        isNewInput = true; // Flag that a new number will follow
    }

    private void calculateResult() {
        if (operator.isEmpty()) {
            return; // If no operator is set, do nothing
        }

        double secondOperand = Double.parseDouble(display.getText());
        double result = 0;

        switch (operator) {
            case "+" -> result = firstOperand + secondOperand;
            case "-" -> result = firstOperand - secondOperand;
            case "*" -> result = firstOperand * secondOperand;
            case "/" -> result = secondOperand != 0 ? firstOperand / secondOperand : 0; // Avoid division by zero
        }

        // Display the result and clear the operator for future use
        display.setText(doubleToString(result));
        operator = "";
        isNewInput = true; // Ready for the next input
    }

    private void clearAll() {
        // Clear everything and reset to initial state
        display.setText("0");
        firstOperand = 0;
        operator = "";
        isNewInput = true;
    }

    private void backspace() {
        // Remove the last character from the display text
        String text = display.getText();

        if (text.length() > 1) {
            display.setText(text.substring(0, text.length() - 1));
        } else {
            display.setText("0");
            isNewInput = true;
        }
    }

    // Utility method to format double output (removes ".0" if the result is an integer)
    private String doubleToString(double value) {
        if (value % 1 == 0) {
            return String.valueOf((int) value);
        } else {
            return String.valueOf(value);
        }
    }
}