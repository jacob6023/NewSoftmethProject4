package com.example.project4;

public class BuildYourOwn extends Pizza {
    private static final int MAX_TOPPINGS = 7;
    private static final double BASE_PRICE_SMALL = 8.99;
    private static final double BASE_PRICE_MEDIUM = 10.99;
    private static final double BASE_PRICE_LARGE = 12.99;
    private static final double TOPPING_PRICE = 1.69;

    @Override
    public double price() {
        double basePrice;
        switch (getSize()) {
            case SMALL:
                basePrice = BASE_PRICE_SMALL;
                break;
            case MEDIUM:
                basePrice = BASE_PRICE_MEDIUM;
                break;
            case LARGE:
                basePrice = BASE_PRICE_LARGE;
                break;
            default:
                basePrice = 0;
        }
        int numToppings = getToppings().size();
        return basePrice + (numToppings * TOPPING_PRICE);
    }

    public boolean addTopping(Topping topping) {
        if (getToppings().size() >= MAX_TOPPINGS) {
            return false; // Cant add more
        }
        return getToppings().add(topping);
    }

    public boolean removeTopping(Topping topping) {
        return getToppings().remove(topping);
    }
}
