package com.example.project4.controllers;
import com.example.project4.BuildYourOwn;
import com.example.project4.Pizza;
import com.example.project4.Size;
import com.example.project4.Topping;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.text.DecimalFormat;

public class BuildYourOwnControllerPizza {

    @FXML
    private ListView<Topping> availableToppingsListView;

    @FXML
    private ListView<Topping> selectedToppingsListView;

    @FXML
    private ComboBox<Size> sizeComboBox;

    @FXML
    private Label crustLabel;

    @FXML
    private Label runningTotalLabel;

    private Pizza pizza;
    private DecimalFormat df = new DecimalFormat("#.00");

    @FXML
    private void initialize() {
        // Initialize available toppings
        ObservableList<Topping> toppings = FXCollections.observableArrayList(Topping.values());
        availableToppingsListView.setItems(toppings);

        // Initialize size options
        sizeComboBox.setItems(FXCollections.observableArrayList(Size.values()));
        sizeComboBox.setValue(Size.SMALL); // Default size

        // Create Build Your Own Pizza
        pizza = new BuildYourOwn();
        pizza.setSize(Size.SMALL);

        // Update crust label
        crustLabel.setText(pizza.getCrust().toString());

        // Update running total
        updateRunningTotal();
    }

    @FXML
    private void handleAddTopping() {
        Topping selectedTopping = availableToppingsListView.getSelectionModel().getSelectedItem();
        if (selectedTopping != null) {
            if (((BuildYourOwn) pizza).addTopping(selectedTopping)) {
                selectedToppingsListView.getItems().add(selectedTopping);
                updateRunningTotal();
            } else {
                // Show error message: Maximum toppings reached
            }
        }
    }

    @FXML
    private void handleRemoveTopping() {
        Topping selectedTopping = selectedToppingsListView.getSelectionModel().getSelectedItem();
        if (selectedTopping != null) {
            ((BuildYourOwn) pizza).removeTopping(selectedTopping);
            selectedToppingsListView.getItems().remove(selectedTopping);
            updateRunningTotal();
        }
    }

    @FXML
    private void handleSizeChange() {
        Size selectedSize = sizeComboBox.getValue();
        pizza.setSize(selectedSize);
        updateRunningTotal();
    }

    private void updateRunningTotal() {
        double price = pizza.price();
        runningTotalLabel.setText("$" + df.format(price));
    }

    @FXML
    private void handleAddToOrder() {
        // Add pizza to current order
    }
}

