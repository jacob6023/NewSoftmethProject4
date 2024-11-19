package com.example.project4;

import java.util.ArrayList;

public class Order {
    private static int nextOrderNumber = 1; // Static for uniqueness on all the orders

    private int number;
    private ArrayList<Pizza> pizzas;

    public Order() {
        this.number = nextOrderNumber++;
        this.pizzas = new ArrayList<>();
    }

    public int getNumber() {
        return number;
    }

    public ArrayList<Pizza> getPizzas() {
        return pizzas;
    }

    public void addPizza(Pizza pizza) {
        pizzas.add(pizza);
    }

    public void removePizza(Pizza pizza) {
        pizzas.remove(pizza);
    }

    public double getTotalAmount() {
        double total = 0;
        for (Pizza pizza : pizzas) {
            total += pizza.price();
        }
        return total;
    }

    public double getSalesTax() {
        return getTotalAmount() * 0.06625;
    }

    public double getOrderTotal() {
        return getTotalAmount() + getSalesTax();
    }

    @Override
    public String toString() {
        return "Order Number: " + number + ", Pizzas: " + pizzas;
    }

    public double calculateTotal() {
        return 0;
    }


}
