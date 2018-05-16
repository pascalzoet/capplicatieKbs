package Main;

import java.util.ArrayList;
import java.util.Collections;

public class BPP {

    private ArrayList<Box> boxes;

    public BPP(){
        boxes = new ArrayList<>();
    }

    protected ArrayList<Product> sortProducts(ArrayList<Product> list) {
        System.out.println("BPP -> Sorting products");
        Collections.sort(list);
        System.out.println("BPP -> Products sorted result:");
        for(int i=0; i<list.size(); i++){
            System.out.print(list.get(i).getSize());
            if(i < list.size()-1){
                System.out.print(",");
            }else{
                System.out.println();
            }
        }
        return list;
    }

    public ArrayList<Box> solve(ArrayList<Product> products){
        products = sortProducts(products);
        boxes.add(new Box(20));
        boxes.add(new Box(20));

        for (Product product : products) {
            int min = boxes.indexOf(Collections.min(boxes));
            int max = boxes.indexOf(Collections.max(boxes));
            if(!boxes.get(min).addProduct(product)){
                if(!boxes.get(max).addProduct(product)){
                    //TODO: save skipped products
                    System.out.println("BPP -> the products are too big to fit in two boxes product " + product.getID() + " has been skipped");
                }
            }
        }
        for(Box b: boxes){
            System.out.println(b);
        }

        return boxes;
    }
}
