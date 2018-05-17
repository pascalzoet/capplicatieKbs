package Main;

import static java.lang.Math.toIntExact;

public class Point {

    private int x;
    private int y;

    public Point(int x, int y){
        this.x = x;
        this.y =y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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

    private long map(long x, long in_min, long in_max, long out_min, long out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}
