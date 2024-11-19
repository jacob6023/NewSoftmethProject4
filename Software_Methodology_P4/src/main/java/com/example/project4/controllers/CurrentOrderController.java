package com.example.project4.controllers;

import com.example.project4.Order;
import com.example.project4.OrderManager;
import com.example.project4.Pizza;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.text.DecimalFormat;

public class CurrentOrderController {

    @FXML
    private TextField orderNumberField;

    @FXML
    private ListView<Pizza> orderListView;

    @FXML
    private TextField subtotalField;

    @FXML
    private TextField salesTaxField;

    @FXML
    private TextField orderTotalField;

    @FXML
    private Button removePizzaButton;

    @FXML
    private Button clearOrderButton;

    @FXML
    private Button placeOrderButton;

    private Order currentOrder;
    private DecimalFormat df = new DecimalFormat("#.00");

    @FXML
    public void initialize() {
        // Get the current order from the OrderManager
        currentOrder = OrderManager.getInstance().getCurrentOrder();

        // Bind the ListView to the pizzas in the current order
        orderListView.setItems(FXCollections.observableArrayList(currentOrder.getPizzas()));

        // Format the ListView display for pizzas
        orderListView.setCellFactory(param -> new ListCell<Pizza>() {
            @Override
            protected void updateItem(Pizza pizza, boolean empty) {
                super.updateItem(pizza, empty);
                if (empty || pizza == null) {
                    setText(null);
                } else {
                    setText(pizza.toString());
                }
            }
        });

        // Initialize the UI elements
        updateOrderDetails();

        // Disable buttons if no pizzas are in the order
        toggleButtons();

        // Enable/disable buttons based on pizza selection
        orderListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            removePizzaButton.setDisable(newSelection == null);
        });
    }

    @FXML
    private void handleRemovePizza() {
        // Get the selected pizza
        Pizza selectedPizza = orderListView.getSelectionModel().getSelectedItem();
        if (selectedPizza != null) {
            currentOrder.removePizza(selectedPizza);

            // Update the ListView
            orderListView.setItems(FXCollections.observableArrayList(currentOrder.getPizzas()));

            // Update order details
            updateOrderDetails();

            // Toggle buttons based on the updated order state
            toggleButtons();
        } else {
            showAlert("No Pizza Selected", "Please select a pizza to remove.");
        }
    }

    @FXML
    private void handleClearOrder() {
        currentOrder.getPizzas().clear();

        // Update the ListView
        orderListView.setItems(FXCollections.observableArrayList(currentOrder.getPizzas()));

        // Update order details
        updateOrderDetails();

        // Toggle buttons
        toggleButtons();
    }

    @FXML
    private void handlePlaceOrder() {
        if (!currentOrder.getPizzas().isEmpty()) {
            // Place the order and start a new one
            OrderManager.getInstance().placeCurrentOrder();

            // Update the UI for the new order
            currentOrder = OrderManager.getInstance().getCurrentOrder();
            orderListView.setItems(FXCollections.observableArrayList(currentOrder.getPizzas()));
            updateOrderDetails();
            toggleButtons();

            showAlert("Order Placed", "Your order has been placed successfully.");
        } else {
            showAlert("Order Empty", "Cannot place an empty order.");
        }
    }

    private void updateOrderDetails() {
        orderNumberField.setText(String.valueOf(currentOrder.getNumber()));
        subtotalField.setText(df.format(currentOrder.getTotalAmount()));
        salesTaxField.setText(df.format(currentOrder.getSalesTax()));
        orderTotalField.setText(df.format(currentOrder.getOrderTotal()));
    }

    private void toggleButtons() {
        boolean hasPizzas = !currentOrder.getPizzas().isEmpty();
        removePizzaButton.setDisable(!hasPizzas);
        clearOrderButton.setDisable(!hasPizzas);
        placeOrderButton.setDisable(!hasPizzas);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
