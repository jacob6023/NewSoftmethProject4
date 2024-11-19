package com.example.project4;

import java.util.ArrayList;

public abstract class Pizza {
    private ArrayList<Topping> toppings;
    private Crust crust;
    private Size size;

    public Pizza() {
        toppings = new ArrayList<>();
        size = Size.SMALL; // Default size
    }

    public ArrayList<Topping> getToppings() {
        return toppings;
    }

    public void setToppings(ArrayList<Topping> toppings) {
        this.toppings = toppings;
    }

    public Crust getCrust() {
        return crust;
    }

    protected void setCrust(Crust crust) {
        this.crust = crust;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public abstract double price();

    @Override
    public String toString() {
        return "Style: " + this.getClass().getSimpleName() +
                ", Crust: " + crust +
                ", Size: " + size +
                ", Toppings: " + toppings;
    }
}

