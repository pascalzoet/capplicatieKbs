package Main;

public class Product {
    private int ID;
    private int X;
    private int Y;
    private int Size;

    public Product(int id, int x, int y, int size){
        this.ID = id;
        this.X = x;
        this.Y = y;
        this.Size = size;
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

    public int getSize() {
        return Size;
    }
}
