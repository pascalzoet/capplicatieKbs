package Main;

import org.ardulink.core.Connection;
import org.ardulink.core.ConnectionBasedLink;
import org.ardulink.core.Link;
import org.ardulink.core.Pin;
import org.ardulink.core.convenience.Links;
import org.ardulink.core.events.AnalogPinValueChangedEvent;
import org.ardulink.core.events.DigitalPinValueChangedEvent;
import org.ardulink.core.events.EventListener;
import org.ardulink.util.URIs;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ArduinoComm implements Runnable{

    private String conn;
    private Link link;
    private String port;
    private int baud;

    public ArduinoComm(String port, int baud){
        this.port = port;
        this.baud = baud;
    }

    @Override
    public void run() {
        conn = "ardulink://serial-jssc?port=" + port + "&baudrate=" + baud + "&pingprobe=false";
        try{
            LeftScreen.setStatus("Connecting");
            link = Links.getLink(URIs.newURI(conn));
            System.out.println(link.toString());
            LeftScreen.setStatus("Connected");
        }catch (java.lang.IllegalArgumentException e){
            LeftScreen.setStatus("Not connected");
        }

    }

    public void setupConn(){
        try{
            if(link instanceof ConnectionBasedLink){
                System.out.println("hi");
            }
            link.addListener(eventListener());
            link.startListening(Pin.digitalPin(13));
        }catch (Exception e){}
    }

    private EventListener eventListener() {
        return new EventListener() {
            @Override
            public void stateChanged(DigitalPinValueChangedEvent event) {
                System.out.println("d");
            }

            @Override
            public void stateChanged(AnalogPinValueChangedEvent event) {

            }
        };
    }

    private Connection.Listener rawDataListener() {
        return new Connection.ListenerAdapter() {
            @Override
            public void received(byte[] bytes) {
                System.out.println(bytes);
            }
        };
    }

    public String send(String data){
        try {
            link.sendCustomMessage(data);
            RightScreen.comLog("Applicatie", data);
        }catch (NullPointerException e){
            RightScreen.comLog("Error", "Not connected");
        }catch (IOException e){
            //stage 1
            RightScreen.comLog("Error", "Error at stage 1");
        }
        return "true";
    }

}
