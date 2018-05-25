package Main;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.omg.CORBA.INTERNAL;

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
    private ArrayList<Route> routes;
    private ArrayList<Integer> skipped;
    private BPP bpp;
    private TSP tsp;
    private boolean solved;
    private boolean ready;
    private int index;
    PrintWriter writer = null;

    public Control(){
        allProducts = new ArrayList<>();
        products = new ArrayList<>();
        boxes = new ArrayList<>();
        routes = new ArrayList<>();
        skipped = new ArrayList<>();
        index = 1;

        bpp = new BPP();
        tsp = new TSP(this);
        solved = false;
        ready = false;
    }

    //Setup application
    public void setup(){
        if(!ready) {
            //setup Arduino communication
            if (setupArduino()) {
                System.out.println();
                //setup Product and order files
                if (setupJson()) {
                    System.out.println();
                    //setup database
                    if (setupDb()) {
                        System.out.println("Application -> Setup successful ready to start simulation");
                        System.out.println();
                        ready = true;
                    }else{
                        System.out.println("Error -> Database error please try again");
                    }
                } else {
                    System.out.println("Error -> JSON error please try again");
                }
            } else {
                System.out.println("Error -> Arduino error please try again");
            }
        }else{
            System.out.println("Application -> Setup has already been completed");
        }
    }

    private boolean setupArduino(){
        System.out.println("Application -> Setting up arduino communication");
        if(!com1Connected){
            com1Connected = true;
            System.out.println("RXTX -> Connecting with TSP");
            com1 = new ArduinoComm("TSP", "COM3", this);
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
            com2 = new ArduinoComm("BPP", "COM5", this);
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
            System.out.println("Customer: " + order.getFirstName() + " " + order.getLastName());
            System.out.println("Product numbers: ");
            for (int i=0; i<order.getProductNumbers().size(); i++){
                System.out.print(order.getProductNumbers().get(i));
                if (i<order.getProductNumbers().size()-1) {
                    System.out.print(",");
                }
            }
            System.out.println();
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
                ps = c.prepareStatement("INSERT INTO product (ProductNumber, Name, X_Location, Y_Location, Size) VALUES(?,?,?,?,?)");
                ps.setInt(1, p.getID());
                ps.setString(2, p.getName());
                ps.setInt(3, p.getX());
                ps.setInt(4, p.getY());
                ps.setInt(5, p.getSize());
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
                    skipped.add(product);
                }else{
                    do{
                        int id = rs.getInt(1);
                        String name = rs.getString(2);
                        int x = rs.getInt(3);
                        int y = rs.getInt(4);
                        int size = rs.getInt(5);
                        products.add(new Product(id, name, x, y, size));
                    }while(rs.next());
                }
            }
            System.out.println("Db -> Product data retrieved");

            for(Product p: products){
                PreparedStatement ps = c.prepareStatement("DELETE FROM product WHERE productNumber = ?");
                ps.setInt(1, p.getID());
                ps.executeUpdate();
            }

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
                System.out.println("Application -> Solving BPP");
                boxes = bpp.solve(products);

                for(Box b : boxes){
                    if(b.getProducts().size() > 4){
                        System.out.println("Applicatie -> There is a limit of four products in a box");
                        LeftScreen.setStatus("Done");
                        return;
                    }
                }
                skipped.addAll(bpp.getSkipped());
                System.out.println("Application -> Solving TSP");
                ArrayList<Product> clone;
                for (Box b : boxes) {
                    System.out.println(b);
                    clone = (ArrayList<Product>) b.getProducts().clone();
                    try{
                        Route r = (Route)((Route)tsp.solve(clone)).clone();
                        routes.add(r);
                    }catch(Exception e){
                        System.out.println("Error -> Clone failed");
                    }
                }
                solved = true;
                System.out.println("Skipped products: " + skipped);
                System.out.println();
                LeftScreen.setStatus("Communicating");
                nextNumber();
            } else {
                System.out.println("Application -> Algorithms already solved");
            }
        }else{
            System.out.println("Application -> please press setup");
        }
    }

    public boolean generateReceipt(){
        boolean result = true;
        try {
            writer = new PrintWriter("src\\Files\\Receipt.txt");
            writer.println("Thanks for your order!");
            writer.println();
            writer.println("Customer:");
            writer.println("Name: " + order.getFirstName() + " " + order.getLastName());
            writer.println("Address: " + order.getStreet() + " " + order.getHouseNumber());
            writer.println(order.getPostalCode() + " " + order.getCity());
            writer.println("Date: " + order.getDate());
            writer.println();
            writer.println();
            writer.println("Delivered products:");
            for(Box b : boxes){
                for(Product p : b.getProducts()){
                    writer.println(p.getName());
                }
                writer.println();
            }
            if(skipped.size() > 0){
                writer.println("Products to be delivered:");
                for(int i : skipped){
                    boolean found = false;
                    for(Product p : allProducts){
                        if(p.getID() == i){
                            writer.println(p.getName());
                            found = true;
                            break;
                        }
                    }
                    if(!found){
                        writer.println("ID: " + i);
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error -> File could not be created " + ex.getMessage());
            result = false;
        } finally {
            if (writer != null) {
                writer.close();
            }
            if(!result){
                return false;
            }
        }
        System.out.println("Application -> Receipt generated");
        return true;
    }

    public void stop(){
        System.out.println();
        System.out.println("Application -> Stopping program");
        System.out.println("Closing -> Closing arduino communication");
        if(com1Connected){
            com1.close();
            com1Connected = false;
        }if(com2Connected){
            try {
                com2.close();
                com2Connected = false;
            }catch(NullPointerException e){
                System.out.println("Error -> Couldn't close BPP");
            }
        }
        System.out.println("Closing -> Closing algorithms");
        bpp.stop();
        tsp.stop();
        solved = false;
        boxes = new ArrayList<>();
        ready = false;
        routes = new ArrayList<>();
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

    public void message(String sender, String message){
        System.out.println();
        System.out.println("Message received:");
        System.out.println(sender + " -> " + message);

        switch (message){
            case "DoneY":
                nextNumber();
                break;
            case "Dropoff-ready":
                if(index > routes.get(0).getPoints().size()-1){
                    com2.write("Right");
                }else{
                    com2.write("Left");
                }
                break;
            case "Dropoff-done":
                System.out.println("moo");
                com2.write("Stop");
                nextNumber();
                break;
        }
    }

    private void nextNumber(){
        int r = 0;
        int realIndex = index;
        if(index >= routes.get(0).getPoints().size()){
            r=1;
            realIndex -= routes.get(0).getPoints().size();
        }

        if(r == 1){
            realIndex++;
        }

        com1.write("x" + Integer.toString(routes.get(r).getPoints().get(realIndex).getX()) + "y" + Integer.toString(routes.get(r).getPoints().get(realIndex).getY()));

        if(r == 1 && index >= routes.get(1).getPoints().size() + routes.get(1).getPoints().size()-2){
            LeftScreen.setStatus("Done");
            System.out.println("Generating receipt");
            generateReceipt();
        }

        index++;
    }

    public ArrayList<Box> getBoxes() {
        return boxes;
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public boolean isSolved() {
        return solved;
    }
}