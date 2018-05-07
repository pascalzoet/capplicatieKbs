package Main;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RightScreen extends JPanel implements ActionListener {

    private static JTextArea comLog;
    private JScrollPane scroll;
    private Control controler;

    public RightScreen(Control control){
        this.controler = control;
        setPreferredSize(new Dimension(280,850));
        setLayout(new BorderLayout());

        comLog = new JTextArea();
        comLog.setBorder(new TitledBorder(new EtchedBorder(), "Communicatie log"));
        comLog.setFont(new Font("Serif", Font.ITALIC, 16));
        comLog.setLineWrap(true);
        comLog.setWrapStyleWord(true);
        comLog.setEditable(false);
        scroll = new JScrollPane(comLog);
        add(scroll);
    }

    public static void comLog(String sender, String log){
        String currentLog = comLog.getText();
        comLog.setText(currentLog + "\n" + sender + " -> " + log);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
