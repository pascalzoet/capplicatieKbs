package Main;

import java.util.ArrayList;

import static java.lang.Math.toIntExact;

public class Route implements Cloneable{

    private ArrayList<Point> points;

    public Route(){
        points = new ArrayList<>();
    }

    public void addPoint(int x, int y){
        points.add(new Point(x,y));
    }
    public void addPoint(int index, int x, int y){
        points.add(index, new Point(x,y));
    }

    public Point getLast(){
        return points.get(points.size()-1);
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void empty(){
        points = new ArrayList<>();
    }

    @Override
    public String toString() {
        String out;
        out = "Points: ";
        for(Point p: points){
            out += "[" + p.getX() + "," + p.getY() + "]";
        }
        return out;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
