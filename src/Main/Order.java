package Main;

import java.util.ArrayList;

public class Order {

    private int customerNumber;
    private int orderNumber;
    private ArrayList<Integer> productNumbers;

    public Order() {
        this.productNumbers = new ArrayList<>();
    }

    public int getCustomerNumber() {
        return customerNumber;
    }

    public int getOrdernummer() {
        return orderNumber;
    }

    public ArrayList<Integer> getProductNumbers() {
        return productNumbers;
    }

}
