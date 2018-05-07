package Main;

import java.util.ArrayList;

public class Control {
    ArduinoComm com1;
    ArduinoComm com2;

    public Control(){

    }

    public void setup(){
        com1 = new ArduinoComm("COM5", 9600);
        //com2 = new ArduinoComm("COM7", 9600);

        testComm();
    }

    public void start(){

    }

    public void testComm(){
        com1.send("Available");
        //com2.send("Available");
    }
}
