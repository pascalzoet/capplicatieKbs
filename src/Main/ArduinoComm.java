package Main;


import org.ardulink.core.Link;
import org.ardulink.core.convenience.Links;
import org.ardulink.util.URIs;

import java.io.IOException;

public class ArduinoComm {

    private String conn;
    private Link link;

    public ArduinoComm(String port, int baud){
        LeftScreen.setStatus("Connecting");
        conn = "ardulink://serial-jssc?port=" + port + "&baudrate=" + baud + "&pingprobe=false";
        try{
            link = Links.getLink(URIs.newURI(conn));
            LeftScreen.setStatus("Connected");
        }catch (java.lang.IllegalArgumentException e){
            LeftScreen.setStatus("Not connected");
        }
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
