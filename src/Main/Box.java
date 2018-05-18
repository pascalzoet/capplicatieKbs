package Main;

import java.util.ArrayList;

public class Box implements Comparable<Box>{
    private int size;
    private ArrayList<Product> products;

    public Box(int size){
        this.size = size;
        products = new ArrayList<>();
    }

    public boolean addProduct(Product p){
        if(this.getSizeLeft() >= p.getSize()){
            products.add(p);
            return true;
        }else{
            return false;
        }
    }

    public int getSizeLeft(){
        int total = 0;
        for(Product p: products){
            total+=p.getSize();
        }
        return this.size - total;
    }

    public int getSize() {
        return size;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    @Override
    public int compareTo(Box b) {
        return this.getSizeLeft() - b.getSizeLeft();
    }

    @Override
    public String toString() {
        String out = "Box:\n";
        out += "Sizes: ";
        for (int i=0; i<products.size(); i++) {
            Product p = products.get(i);
            out += p.getSize();
            if(i < products.size()-1){
                out += ",";
            }
        }
        out+="\n";
        out+="ID's: ";
        for (int i=0; i<products.size(); i++) {
            Product p = products.get(i);
            out += p.getID();
            if(i < products.size()-1){
                out += ",";
            }
        }
        out += "\n";
        out += "Total: ";
        out += (size-getSizeLeft());
        out += "\n";
        return out;
    }
}
