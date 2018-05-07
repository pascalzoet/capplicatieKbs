package Main;


import org.ardulink.core.Link;
import org.ardulink.core.convenience.Links;
import org.ardulink.util.URIs;
import java.util.concurrent.TimeUnit;

public class ArduinoComm {

    private String conn;
    private Link link;

    public ArduinoComm(String port, int baud){
        LeftScreen.setStatus("Connecting");
        conn = "ardulink://serial-jssc?port=" + port + "&baudrate=" + baud + "&pingprobe=false";
        link = Links.getLink(URIs.newURI(conn));
        LeftScreen.setStatus("Connected");
    }

    public String send(String data){
        try {
            link.sendCustomMessage(data);
            RightScreen.comLog("Applicatie", data);
        }catch (Exception e){
            System.out.println(e);
        }
        return "true";
    }

}
