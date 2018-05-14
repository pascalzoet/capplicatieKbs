package Main;

import java.util.concurrent.TimeUnit;

public class Control {
    private ArduinoComm com1;
    private ArduinoComm com2;
    private boolean com1Connected = false;
    private boolean com2Connected = false;

    public Control(){

    }

    public void setup(){
        if(!com1Connected){
            com1Connected = true;
            System.out.println("Connecting with TSP");
            com1 = new ArduinoComm("TSP", "COM3");
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
            System.out.println("Already connected to " + com1.getName());
        }
        if(!com2Connected){
            com2Connected = true;
            System.out.println("Connecting with BPP");
            com2 = new ArduinoComm("BPP", "COM6");
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
            System.out.println("Already connected to " + com2.getName());
        }

        if(com1Connected && com2Connected){
            LeftScreen.setStatus("Connected");
        }else if(com1Connected){
            com1.close();
            System.out.println("Closed connections because not both connections could be established");
        }else if(com2Connected){
            com2.close();
            System.out.println("Closed connections because not both connections could be established");
        }else{
            System.out.println("No connection could be established");
        }
    }

    public void start(){

    }

    public void stop(){
        if(com1Connected){
            com1.close();
            com1Connected = false;
        }if(com2Connected){
            com2.close();
            com2Connected = false;
        }
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
}