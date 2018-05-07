package Main;


import org.ardulink.core.Link;
import org.ardulink.core.convenience.Links;
import org.ardulink.util.URIs;
import java.util.concurrent.TimeUnit;

public class ArduinoComm {
    public ArduinoComm(String port, int baud){
        Link link = Links.getLink(URIs.newURI("ardulink://serial-jssc?port=COM5&baudrate=9600&pingprobe=false"));
        try {
            link.sendCustomMessage("1,0,1,0,1");
            TimeUnit.SECONDS.sleep(1);
            link.sendCustomMessage("5");
            link.sendCustomMessage("5");
            link.sendCustomMessage("5");
            link.sendCustomMessage("5");
            link.sendCustomMessage("5");
        }catch (Exception e){
            System.out.println(e);
        }

    }

}
