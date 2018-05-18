package Main;

import static java.lang.Math.toIntExact;

public class Product  implements Comparable<Product>{
    private int ID;
    private String Name;
    private int X;
    private int Y;
    private int Size;

    public Product(int id, String Name, int x, int y, int size){
        this.ID = id;
        this.Name = Name;
        this.X = x;
        this.Y = y;
        this.Size = size;
    }

    @Override
    public int compareTo(Product p) {
        int comparesize = ((Product) p).getSize();
        return comparesize - this.Size;
    }

    public int getID(){
        return ID;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public int getTrueX(){
        int p1x;

        if(getX() < 5){
            p1x = (getX() * 216 + 216/2) + 10;
        }else{
            p1x = getX();
        }

        return p1x;
    }

    public int getTrueY(){
        int p1y;
        int calc1 = toIntExact(map(getY(), 0, 4, 4, 0));
        if(getY() < 5){
            p1y = (calc1 * 80 + 80/2) + 435;
        }else{
            p1y = getY();
        }

        return p1y;
    }

    private long map(long x, long in_min, long in_max, long out_min, long out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public String getName() {
        return Name;
    }

    public int getSize() {
        return Size;
    }
}
