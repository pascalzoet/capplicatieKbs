package Main;

import java.awt.geom.Line2D;
import java.util.ArrayList;

import static java.lang.Math.toIntExact;

public class TSP {

    private Route route;
    private ArrayList<Product> products;
    Control c;

    public TSP(Control c){
        this.c = c;
        route = new Route();
    }

    public Route solve(ArrayList<Product> p){
        //NN
        this.products = p;
        route.empty();
        route.addPoint(10,840);
        while(products.size() > 0){
            int nearest = getNearest(products, route.getLast());
            route.addPoint(products.get(nearest).getX(),products.get(nearest).getY());
            products.remove(nearest);
        }
        route.addPoint(10,840);

        System.out.println("TSP -> Nearest neighbour result:");
        System.out.println(route);
        System.out.println();

        //2-opt
        while(!twoOpt()){}
        System.out.println("TSP -> 2-Opt result:");
        System.out.println(route);
        return route;
    }

    public int getNearest(ArrayList<Product> products, Point p2){
        double distance = 999999999;
        int smallest = -1;
        for(int i=0; i<products.size(); i++){
            Product p1 = products.get(i);

            double d = Math.hypot(p1.getTrueX()-p2.getTrueX(), p1.getTrueY()-p2.getTrueY());
            if(d < distance){
                distance = d;
                smallest = i;
            }
        }
        return smallest;
    }

    public void stop(){
        route = new Route();
    }

    public boolean twoOpt(){
        for(int i=0; i< route.getPoints().size()-3; i++){
            Point r1 = route.getPoints().get(i);
            Point r2 = route.getPoints().get(i+1);
            Point r3 = route.getPoints().get(i+2);
            Point r4 = route.getPoints().get(i+3);
            if(Line2D.linesIntersect(r1.getTrueX(),r1.getTrueY(),r2.getTrueX(),r2.getTrueY(),r3.getTrueX(),r3.getTrueY(),r4.getTrueX(),r4.getTrueY())){
                int r2x = r2.getX();
                int r2y = r2.getY();
                route.getPoints().get(i+1).setX(r3.getX());
                route.getPoints().get(i+1).setY(r3.getY());
                route.getPoints().get(i+2).setX(r2x);
                route.getPoints().get(i+2).setY(r2y);
                return false;
            }
        }
        return true;
    }
}
