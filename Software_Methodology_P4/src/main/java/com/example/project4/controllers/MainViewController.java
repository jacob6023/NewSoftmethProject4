package com.example.project4.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class MainViewController {

    @FXML
    private void handleChicagoStyle(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project4/chicago-pizza-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Order Chicago-Style Pizza");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Optionally, show an alert to the user
            showAlert("Error", "Unable to open the Chicago-Style Pizza view.");
        }
    }

    private void showAlert(String error, String s) {
    }



    @FXML
    private void handleNYStyle(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project4/ny-pizza-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Order New York-Style Pizza");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleViewOrder(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project4/current-order-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Manage Current Orders");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOrderDetails(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project4/order-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Order Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to open the Order Details view.");
        }
    }
}