package Main;

import java.util.ArrayList;

public class Control {
    private ArrayList<ArduinoComm> arduino;

    public Control(){

    }

    public void setup(){
        arduino = new ArrayList<>();
        arduino.add(new ArduinoComm("Arduino Uno", 9600));
    }

    public void start(){

    }

    public void testComm(){

    }
}
