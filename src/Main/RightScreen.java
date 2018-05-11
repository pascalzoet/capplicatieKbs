package Main;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RightScreen extends JPanel implements ActionListener {
    private Timer timer;
    private static JTextArea comLog;
    private JScrollPane scroll;
    private Control controler;
    private static ByteArrayOutputStream baos;

    public RightScreen(Control control, ByteArrayOutputStream baos){
        this.baos = baos;
        this.controler = control;
        setPreferredSize(new Dimension(280,850));
        setLayout(new BorderLayout());

        comLog = new JTextArea();
        comLog.setBorder(new TitledBorder(new EtchedBorder(), "Communication log"));
        comLog.setFont(new Font("Serif", Font.ITALIC, 16));
        comLog.setLineWrap(true);
        comLog.setWrapStyleWord(true);
        comLog.setEditable(false);
        scroll = new JScrollPane(comLog);
        add(scroll);

        timer = new Timer(1000, this);
        timer.setInitialDelay(0);
        timer.start();
    }

    public static void comLog(){
        comLog.setText(baos.toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        comLog();
    }
}
