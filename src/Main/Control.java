package Main;

import java.util.concurrent.TimeUnit;

public class Control {
    ArduinoComm com1;

    public Control(){

    }

    public void setup(){
        LeftScreen.setStatus("Connecting");
        com1 = new ArduinoComm();
        com1.initialize();
        Thread t = new Thread(com1);
        t.start();
        try{
            TimeUnit.SECONDS.sleep(2);
        }catch (Exception e){
            System.err.println(e.toString());
        }
        LeftScreen.setStatus("Connected");
    }

    public void start(){

    }

    public void testComm(){
        com1.write("Available");
        System.out.println("Application -> Available");
    }
}