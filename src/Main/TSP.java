package Main;

import java.util.ArrayList;

public class TSP {

    private Route route;
    private ArrayList<Product> products;
    private Control c;

    public TSP(Control c){
        this.c = c;
        route = new Route();
    }

    public Route solve(ArrayList<Product> p){
        //nearest neighbour
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

        //heuristic 2-opt
        if(!(route.getPoints().size() == 2)){
            while(twoOpt()){}
        }
        System.out.println("TSP -> 2-Opt result:");
        System.out.println(route);
        return route;
    }

    private int getNearest(ArrayList<Product> products, Point p2){
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

    private boolean twoOpt(){
        boolean improved = false;
        for(int i=1; i<route.getPoints().size()-1; i++){
            for(int j=1; j<route.getPoints().size()-1; j++){
                if(i == j){
                    continue;
                }
                double d = meassureRoute();
                swap(i,j);
                double d2 = meassureRoute();
                if(d<=d2){
                    swap(i,j);
                }else{
                    improved = true;
                }
            }
        }
        if(improved){
            return true;
        }else{
            return false;
        }
    }

    //swap 2 points
    private void swap(int i,int j){
        int ix = route.getPoints().get(i).getX();
        int iy = route.getPoints().get(i).getY();
        route.getPoints().get(i).setX(route.getPoints().get(j).getX());
        route.getPoints().get(i).setY(route.getPoints().get(j).getY());
        route.getPoints().get(j).setX(ix);
        route.getPoints().get(j).setY(iy);
    }

    //measure the length of the route
    private double meassureRoute(){
        double total = 0;
        for(int i=0; i<route.getPoints().size(); i++){
            if(i < route.getPoints().size()-1){
                total += calculateDistance(route.getPoints().get(i), route.getPoints().get(i+1));
            }else{
                total += calculateDistance(route.getPoints().get(i), route.getPoints().get(0));
            }
        }
        return total;
    }

    private double calculateDistance(Point locA, Point locB) {
        double distA;
        double distB;
        if (locA.getTrueX() > locB.getTrueX()) {
            distA = locA.getTrueX() - locB.getTrueX();
        } else {
            distA = locB.getTrueX() - locA.getTrueX();
        }
        if (locA.getTrueY() > locB.getTrueY()) {
            distB = locA.getTrueY() - locB.getTrueY();
        } else {
            distB = locB.getTrueY() - locA.getTrueY();
        }
        return Math.sqrt(Math.pow(distA, 2) + Math.pow(distB, 2));
    }
}
