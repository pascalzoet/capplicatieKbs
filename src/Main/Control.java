package Main;

import org.ardulink.core.Connection;
import org.ardulink.core.ConnectionBasedLink;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Control {
    ArduinoComm com1;
    ArduinoComm com2;

    public Control(){

    }

    public void setup(){
        com1 = new ArduinoComm("COM3", 9600);
        Thread t = new Thread(com1);
        t.start();

        //com2 = new ArduinoComm("COM7", 9600);

        //read value
    }

    public void start(){

    }

    public void testComm(){
        com1.setupConn();
        try{
            com1.send("Available");
        }catch (NullPointerException e){
            RightScreen.comLog("Error", "No communication made");
        }
        //com2.send("Available");
    }
}
