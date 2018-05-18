package Main;

import java.util.ArrayList;

public class Order {

    private String firstName;
    private String lastName;
    private String city;
    private String street;
    private int houseNumber;
    private String postalCode;
    private String date;
    private ArrayList<Integer> productNumbers;

    public Order() {
        this.productNumbers = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<Integer> getProductNumbers() {
        return productNumbers;
    }

}
