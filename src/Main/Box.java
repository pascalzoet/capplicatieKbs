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

    @Override
    public int compareTo(Box b) {
        return this.getSizeLeft() - b.getSizeLeft();
    }

    @Override
    public String toString() {
        String out = "Box:";
        for (Product p: products) {
            out += p.getSize();
        }
        out += "Total: ";
        out += (size-getSizeLeft());
        return out;
    }
}
