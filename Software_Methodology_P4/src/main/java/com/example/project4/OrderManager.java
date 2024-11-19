package com.example.project4;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class OrderManager {
    private static OrderManager instance = null;
    private Order currentOrder;
    private ArrayList<Order> allOrders;

    private OrderManager() {
        currentOrder = new Order(); // Start with an empty order
        allOrders = new ArrayList<>();
    }

    // Get the singleton instance
    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    // Get the current order
    public Order getCurrentOrder() {
        return currentOrder;
    }

    // Place the current order and start a new one
    public void placeCurrentOrder() {
        if (!currentOrder.getPizzas().isEmpty()) {
            allOrders.add(currentOrder); // Add the current order to the list of all orders
            currentOrder = new Order(); // Create a new order
        } else {
            System.out.println("Cannot place an empty order."); // For debugging, can be removed or replaced with a user alert
        }
    }

    // Get all orders
    public ArrayList<Order> getAllOrders() {
        return allOrders;
    }

    // Cancel an order
    public void cancelOrder(Order order) {
        if (allOrders.contains(order)) {
            allOrders.remove(order);
        } else {
            System.out.println("Order not found."); // For debugging, replace with user alert if needed
        }
    }

    // Remove an order by reference
    public void removeOrder(Order selectedOrder) {
        cancelOrder(selectedOrder); // This method now reuses the cancelOrder logic
    }

    // Export all orders to a file
    public void exportOrdersToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Order order : allOrders) {
                writer.write(order.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error exporting orders: " + e.getMessage()); // For debugging
        }
    }
}
