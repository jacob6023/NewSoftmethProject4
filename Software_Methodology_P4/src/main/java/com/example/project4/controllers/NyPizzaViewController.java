package com.example.project4.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.text.DecimalFormat;
import com.example.project4.*;

public class NyPizzaViewController {

    @FXML
    private ComboBox<String> pizzaTypeComboBox;

    @FXML
    private RadioButton smallSizeRadioButton;

    @FXML
    private RadioButton mediumSizeRadioButton;

    @FXML
    private RadioButton largeSizeRadioButton;

    @FXML
    private ToggleGroup sizeToggleGroup;

    @FXML
    private TextField crustTextField;

    @FXML
    private ListView<String> availableToppingsListView;

    @FXML
    private ListView<String> selectedToppingsListView;

    @FXML
    private Button addToppingButton;

    @FXML
    private Button removeToppingButton;

    @FXML
    private TextField pizzaPriceTextField;

    @FXML
    private Button addToOrderButton;

    // Other instance variables
    private PizzaFactory pizzaFactory;
    private Pizza pizza;
    private DecimalFormat df = new DecimalFormat("#0.00");
    private ObservableList<String> availableToppings;
    private ObservableList<String> selectedToppings;

    private Order currentOrder;

    @FXML
    public void initialize() {
        // Initialize pizza factory for New York style
        pizzaFactory = new NYPizza(); // Ensure NYPizza class exists

        // Set up the pizzaTypeComboBox items
        ObservableList<String> pizzaTypes = FXCollections.observableArrayList("Deluxe", "BBQ Chicken", "Meatzza", "Build Your Own");
        pizzaTypeComboBox.setItems(pizzaTypes);
        pizzaTypeComboBox.setValue("Build Your Own"); // default value

        // Initialize toppings lists
        availableToppings = FXCollections.observableArrayList(
                "Sausage", "Pepperoni", "Green Pepper", "Onion", "Mushroom",
                "BBQ Chicken", "Provolone", "Cheddar", "Beef", "Ham",
                "Pineapple", "Spinach", "Olives"
        );
        selectedToppings = FXCollections.observableArrayList();

        availableToppingsListView.setItems(availableToppings);
        selectedToppingsListView.setItems(selectedToppings);

        // Set default size
        sizeToggleGroup.selectToggle(smallSizeRadioButton);

        // Create initial pizza
        pizza = pizzaFactory.createBuildYourOwn();
        pizza.setSize(Size.SMALL);
        crustTextField.setText(pizza.getCrust().toString());

        updatePizzaPrice();

        // Initialize current order
        currentOrder = OrderManager.getInstance().getCurrentOrder();
    }

    @FXML
    private void handlePizzaTypeSelection(ActionEvent event) {
        String selectedType = pizzaTypeComboBox.getValue();

        // Clear selected toppings
        selectedToppings.clear();
        availableToppingsListView.setItems(availableToppings);
        selectedToppingsListView.setItems(selectedToppings);

        // Update pizza object
        if (selectedType.equals("Build Your Own")) {
            pizza = pizzaFactory.createBuildYourOwn();
            crustTextField.setText(pizza.getCrust().toString());
            // Enable topping selection
            availableToppingsListView.setDisable(false);
            selectedToppingsListView.setDisable(false);
            addToppingButton.setDisable(false);
            removeToppingButton.setDisable(false);
        } else {
            // Create specialty pizza
            switch (selectedType) {
                case "Deluxe":
                    pizza = pizzaFactory.createDeluxe();
                    break;
                case "BBQ Chicken":
                    pizza = pizzaFactory.createBBQChicken();
                    break;
                case "Meatzza":
                    pizza = pizzaFactory.createMeatzza();
                    break;
            }
            crustTextField.setText(pizza.getCrust().toString());
            // Disable topping selection
            availableToppingsListView.setDisable(true);
            selectedToppingsListView.setDisable(true);
            addToppingButton.setDisable(true);
            removeToppingButton.setDisable(true);
            // Display toppings
            selectedToppings.clear();
            for (Topping topping : pizza.getToppings()) {
                selectedToppings.add(formatToppingName(topping));
            }
        }

        // Update pizza size based on selected size radio button
        RadioButton selectedSizeButton = (RadioButton) sizeToggleGroup.getSelectedToggle();
        if (selectedSizeButton != null) {
            String size = selectedSizeButton.getText();
            pizza.setSize(Size.valueOf(size.toUpperCase()));
        }

        updatePizzaPrice();
    }

    @FXML
    private void handleSizeSelection(ActionEvent event) {
        RadioButton selectedSizeButton = (RadioButton) sizeToggleGroup.getSelectedToggle();
        if (selectedSizeButton != null && pizza != null) {
            String size = selectedSizeButton.getText();
            pizza.setSize(Size.valueOf(size.toUpperCase()));
            updatePizzaPrice();
        }
    }

    @FXML
    private void handleAddTopping(ActionEvent event) {
        String selectedTopping = availableToppingsListView.getSelectionModel().getSelectedItem();
        if (selectedTopping != null) {
            if (selectedToppings.size() < 7) {
                availableToppings.remove(selectedTopping);
                selectedToppings.add(selectedTopping);

                // Add topping to pizza
                Topping toppingEnum = Topping.valueOf(formatEnumName(selectedTopping));
                ((BuildYourOwn) pizza).addTopping(toppingEnum);

                updatePizzaPrice();
            } else {
                showAlert("Maximum Toppings Reached", "You can only add up to 7 toppings.");
            }
        } else {
            showAlert("No Topping Selected", "Please select a topping to add.");
        }
    }

    @FXML
    private void handleRemoveTopping(ActionEvent event) {
        String selectedTopping = selectedToppingsListView.getSelectionModel().getSelectedItem();
        if (selectedTopping != null) {
            selectedToppings.remove(selectedTopping);
            availableToppings.add(selectedTopping);

            // Remove topping from pizza
            Topping toppingEnum = Topping.valueOf(formatEnumName(selectedTopping));
            ((BuildYourOwn) pizza).removeTopping(toppingEnum);

            updatePizzaPrice();
        } else {
            showAlert("No Topping Selected", "Please select a topping to remove.");
        }
    }

    @FXML
    private void handleAddToOrder(ActionEvent event) {
        if (pizza != null) {
            currentOrder.addPizza(pizza);
            showAlert("Pizza Added", "The pizza has been added to the current order.");
            // Optionally reset the form or close the window
        } else {
            showAlert("No Pizza Selected", "Please select a pizza to add to the order.");
        }
    }

    private void updatePizzaPrice() {
        if (pizza != null) {
            double price = pizza.price();
            pizzaPriceTextField.setText("$" + df.format(price));
        }
    }

    private String formatEnumName(String name) {
        return name.toUpperCase().replace(" ", "_");
    }

    private String formatToppingName(Topping topping) {
        String name = topping.toString().replace("_", " ").toLowerCase();
        return capitalizeWords(name);
    }

    private String capitalizeWords(String input) {
        String[] words = input.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if(word.length() > 0) {
                sb.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }
        return sb.toString().trim();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
