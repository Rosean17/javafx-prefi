
package java.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TodoController {

    @FXML
    private ListView<String> todoList;

    private final ObservableList<String> items = FXCollections.observableArrayList();
    private final Path filePath = Paths.get("todo.txt");

    @FXML
    public void initialize() {
        loadFromFile();
        todoList.setItems(items);
        todoList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        todoList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                editSelectedItem();
            }
        });
    }

    @FXML
    protected void onCreateClick() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Add Todo");
        dialog.setHeaderText("Enter a new task:");

        dialog.showAndWait().ifPresent(value -> {
            if (!value.trim().isEmpty()) {
                items.add(value.trim());
                saveToFile();
            }
        });
    }

    @FXML
    protected void onDeleteClick() {
        int selectedIndex = todoList.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            items.remove(selectedIndex);
            saveToFile();
        } else {
            showInfo("No task selected", "Please select a task to delete.");
        }
    }

    @FXML
    protected void onClearAllClick() {
        items.clear();
        saveToFile();
    }

    private void editSelectedItem() {
        int index = todoList.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            String current = items.get(index);
            TextInputDialog dialog = new TextInputDialog(current);
            dialog.setTitle("Edit Task");
            dialog.setHeaderText("Edit selected task:");

            dialog.showAndWait().ifPresent(newValue -> {
                if (!newValue.trim().isEmpty()) {
                    items.set(index, newValue.trim());
                    saveToFile();
                }
            });
        }
    }

    private void saveToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (String item : items) {
                writer.write(item);
                writer.newLine();
            }
        } catch (IOException e) {
            showError("Could not save tasks.");
        }
    }

    private void loadFromFile() {
        if (Files.exists(filePath)) {
            try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        items.add(line.trim());
                    }
                }
            } catch (IOException e) {
                showError("Could not load tasks.");
            }
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(msg);
        alert.showAndWait();
    }

    private void showInfo(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(msg);
        alert.showAndWait();
    }
}
