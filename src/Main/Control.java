package Main;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.*;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Control {
    //comm variables
    private ArduinoComm com1;
    private ArduinoComm com2;
    private boolean com1Connected = false;
    private boolean com2Connected = false;

    //json variables
    private Gson gson = new Gson();
    private JsonReader reader;
    private Order order;
    private boolean jsonResult = false;
    private ArrayList<Product> allProducts;

    //db variables
    private String url = "jdbc:mysql://localhost/kbs?serverTimezone=UTC";
    private String username = "root";
    private String password = "";
    private PreparedStatement ps;

    //general variables
    private ArrayList<Product> products;
    private ArrayList<Box> boxes;
    private BPP bpp;
    private TSP tsp;
    private boolean solved;
    private boolean ready;

    public Control(){
        allProducts = new ArrayList<>();
        products = new ArrayList<>();
        boxes = new ArrayList<>();

        bpp = new BPP();
        tsp = new TSP();
        solved = false;
        ready = false;
    }

    public void setup(){
        //TODO: flip statement
        if(!setupArduino()) {
            System.out.println();
            if(setupJson()) {
                System.out.println();
                if(setupDb()){
                    System.out.println("Application -> Setup successful ready to start simulation");
                    ready = true;
                }
            }else{
                System.out.println("Error -> Please try again");
            }
        }else{
            System.out.println("Error -> Please try again");
        }
    }

    private boolean setupArduino(){
        System.out.println("Application -> Setting up arduino communication");
        if(!com1Connected){
            com1Connected = true;
            System.out.println("RXTX -> Connecting with TSP");
            com1 = new ArduinoComm("TSP", "COM3");
            if(!com1.initialize()){
                com1Connected = false;
            }
            Thread com1T = new Thread(com1);
            com1T.start();
            try{
                TimeUnit.SECONDS.sleep(2);
            }catch (Exception e){
                System.err.println(e.toString());
            }
        }else{
            System.out.println("RXTX -> Already connected to " + com1.getName());
        }
        if(!com2Connected){
            com2Connected = true;
            System.out.println("RXTX -> Connecting with BPP");
            com2 = new ArduinoComm("BPP", "COM6");
            if(!com2.initialize()){
                com2Connected = false;
            }
            Thread com2T = new Thread(com2);
            com2T.start();
            try{
                TimeUnit.SECONDS.sleep(2);
            }catch (Exception e){
                System.err.println(e.toString());
            }
        }else{
            System.out.println("RXTX -> Already connected to " + com2.getName());
        }

        if(com1Connected && com2Connected){
            LeftScreen.setStatus("Connected");
            System.out.println("RXTX -> Connected to robots");
            return true;
        }else if(com1Connected){
            com1.close();
            System.out.println("RXTX -> Closed connections because not both connections could be established");
        }else if(com2Connected){
            com2.close();
            System.out.println("RXTX -> Closed connections because not both connections could be established");
        }else{
            System.out.println("RXTX -> No connection could be established");
        }
        return false;
    }

    private boolean setupJson(){
        System.out.println("Application -> Reading order JSON file");
        try{
            reader = new JsonReader(new FileReader("src\\Files\\order.json"));
            order = gson.fromJson(reader, Order.class);
            jsonResult = true;
        }catch (FileNotFoundException e){
            System.out.println("JsonReader -> no order file found");
        }

        if (jsonResult){
            System.out.println("JsonReader -> Succes ");
            System.out.println("JsonReader -> Data set JSON order file:");
            System.out.println("Customer number: " + order.getCustomerNumber());
            System.out.println("Order number: " + order.getOrdernummer());
            System.out.println("Product numbers: ");
            for (int number : order.getProductNumbers()){
                System.out.println(number);
            }
        }else{
            return false;
        }

        System.out.println();

        System.out.println("Application -> Reading product set JSON file");
        try{
            reader = new JsonReader(new FileReader("src\\Files\\products.json"));
            Type listType = new TypeToken<List<Product>>() {}.getType();
            allProducts = gson.fromJson(reader, listType);
            System.out.println("JsonReader -> Products read successfully");
        }catch (FileNotFoundException e){
            System.out.println("JsonReader -> no products file found");
            return false;
        }
        return true;
    }

    private boolean setupDb(){
        System.out.println("Application -> Setting up database");
        try {
            Connection c = DriverManager.getConnection(url, username, password);
            System.out.println("Db -> Connection OK");

            System.out.println("Db -> Resetting product table");
            ps = c.prepareStatement("Delete from product");
            ps.executeUpdate();
            System.out.println("Db -> Table has been reset");

            System.out.println("Db -> Inserting products into product table");
            for(Product p : allProducts){
                ps = c.prepareStatement("INSERT INTO product (ProductNumber, X_Location, Y_Location, Size) VALUES(?,?,?,?)");
                ps.setInt(1, p.getID());
                ps.setInt(2, p.getX());
                ps.setInt(3, p.getY());
                ps.setInt(4, p.getSize());
                ps.executeUpdate();
            }
            System.out.println("Db -> Product table has been filled");
            System.out.println();
            System.out.println("Db - Retrieving product data");
            products = new ArrayList<>();
            for(int product : order.getProductNumbers()) {
                PreparedStatement ps = c.prepareStatement("SELECT * FROM product WHERE productNumber = ?");
                ps.setInt(1, product);
                ResultSet rs = ps.executeQuery();

                if(!rs.next()){
                    System.out.println("Db -> Product with number: " + product + " not found this product is skipped");
                }else{
                    do{
                        int id = rs.getInt(1);
                        int x = rs.getInt(2);
                        int y = rs.getInt(3);
                        int size = rs.getInt(4);
                        products.add(new Product(id, x, y, size));
                    }while(rs.next());
                }
            }
            System.out.println("Db -> Product data retrieved");

            ps.close();
            c.close();
            return true;
        } catch (SQLException sqle) {
            System.out.println("Error -> SQL Exception: " + sqle);
            return false;
        }
    }

    public void start(){
        if(ready) {
            if (!solved) {
                LeftScreen.setStatus("Started");
                solved = true;
                System.out.println("Application -> Solving BPP");
                boxes = bpp.solve(products);
                System.out.println();
                System.out.println("Application -> Solving TSP");
                for (Box b : boxes) {
                    tsp.solve(b.getProducts());
                }
                LeftScreen.setStatus("Done");
            } else {
                System.out.println("Application -> Algorithms already solved");
            }
        }else{
            System.out.println("Application -> please press setup");
        }
    }

    public void stop(){
        System.out.println();
        System.out.println("Application -> Stopping program");
        System.out.println("Closing -> Closing arduino communication");
        if(com1Connected){
            com1.close();
            com1Connected = false;
        }if(com2Connected){
            com2.close();
            com2Connected = false;
        }
        System.out.println("Closing -> Closing algorithms");
        bpp.stop();
        tsp.stop();
        solved = false;
        boxes = new ArrayList<>();
        ready = false;
    }

    public void testComm(){
        if(com1Connected){
            try{
                if(!com1.write("Available " + com1.getName())){
                    this.com1Connected = false;
                    this.com2Connected = false;
                    com2.close();
                }
            }catch (NullPointerException e){
                System.out.println("Not connected to TSP");
            }
        }else{
            System.out.println("Not connected to TSP");
        }
        if(com2Connected){
            try{
                if(!com2.write("Available " + com2.getName())){
                    this.com2Connected = false;
                    this.com1Connected = false;
                    com1.close();
                }
            }catch (NullPointerException e){
                System.out.println("Not connected to BPP");
            }
        }else{
            System.out.println("Not connected to TSP");
        }
    }

    public ArrayList<Box> getBoxes() {
        return boxes;
    }
}