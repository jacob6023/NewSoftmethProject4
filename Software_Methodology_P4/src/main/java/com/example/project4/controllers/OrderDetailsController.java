package com.example.project4.controllers;

import com.example.project4.Order;
import com.example.project4.OrderManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class OrderDetailsController {

    @FXML
    private ListView<Order> ordersListView; // Displays the list of orders

    @FXML
    private Label totalAmountLabel; // Displays the total amount of all orders

    @FXML
    private Button cancelOrderButton; // Button to cancel a selected order

    @FXML
    private Button exportOrdersButton; // Button to export all orders to a text file

    private ObservableList<Order> orders; // Observable list of orders for the ListView
    private DecimalFormat df = new DecimalFormat("#.00");

    @FXML
    public void initialize() {
        // Load orders from OrderManager
        orders = FXCollections.observableArrayList(OrderManager.getInstance().getAllOrders());
        ordersListView.setItems(orders);

        // Format the order items in the ListView
        ordersListView.setCellFactory(param -> new ListCell<Order>() {
            @Override
            protected void updateItem(Order order, boolean empty) {
                super.updateItem(order, empty);
                if (empty || order == null) {
                    setText(null);
                } else {
                    setText(formatOrder(order));
                }
            }
        });

        // Update the total amount
        updateTotalAmount();

        // Disable buttons if there are no orders
        cancelOrderButton.setDisable(orders.isEmpty());
        exportOrdersButton.setDisable(orders.isEmpty());

        // Enable/disable the cancel button when an order is selected/deselected
        ordersListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            cancelOrderButton.setDisable(newSelection == null);
        });
    }

    @FXML
    private void handleCancelOrder() {
        // Get the selected order
        Order selectedOrder = ordersListView.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            // Remove the selected order from the list
            OrderManager.getInstance().cancelOrder(selectedOrder);
            orders.remove(selectedOrder);

            // Update the total amount
            updateTotalAmount();

            // Disable buttons if there are no more orders
            if (orders.isEmpty()) {
                cancelOrderButton.setDisable(true);
                exportOrdersButton.setDisable(true);
            }
        } else {
            showAlert("No Order Selected", "Please select an order to cancel.");
        }
    }

    @FXML
    private void handleExportOrders() {
        if (orders.isEmpty()) {
            showAlert("No Orders to Export", "There are no orders to export.");
            return;
        }

        // Open a file chooser dialog to save the orders
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Orders");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        java.io.File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Order order : orders) {
                    writer.write(formatOrder(order));
                    writer.newLine();
                }
                showAlert("Export Successful", "Orders have been successfully exported to " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert("Export Failed", "An error occurred while exporting orders: " + e.getMessage());
            }
        }
    }

    private void updateTotalAmount() {
        double totalAmount = orders.stream().mapToDouble(Order::getOrderTotal).sum();
        totalAmountLabel.setText(String.format("Total Amount: $%.2f", totalAmount));
    }

    private String formatOrder(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append("Order #").append(order.getNumber()).append(": ");
        order.getPizzas().forEach(pizza -> sb.append(pizza.toString()).append("; "));
        sb.append("Total: $").append(String.format("%.2f", order.getOrderTotal()));
        return sb.toString();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
