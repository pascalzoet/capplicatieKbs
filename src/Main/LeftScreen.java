package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LeftScreen extends JPanel implements ActionListener{

    private JButton comm, start, stop, testCom;
    private JLabel statuslbl;
    private static JTextField status;
    private Control controler;

    public LeftScreen(Control control){
        this.controler = control;

        setPreferredSize(new Dimension(1100,850));
        setLayout(null);

        comm = new JButton("Setup");
        comm.addActionListener(this);
        comm.setBounds(802, 10, 88, 51);
        add(comm);

        start = new JButton("Start");
        start.addActionListener(this);
        start.setBounds(906, 10, 88, 51);
        add(start);

        stop = new JButton("Stop");
        stop.addActionListener(this);
        stop.setBounds(1010, 10, 88, 51);
        add(stop);

        testCom = new JButton("Test communication");
        testCom.addActionListener(this);
        testCom.setBounds(802, 70, 296, 51);
        add(testCom);

        statuslbl = new JLabel("Status:");
        statuslbl.setBounds(906, 110, 40, 51);
        add(statuslbl);

        status = new JTextField("Not started");
        status.setBounds(950, 127, 148, 20);
        status.setEditable(false);
        status.setBackground(Color.gray);
        status.setForeground(Color.white);
        add(status);
    }

    public static void setStatus(String s) {
        status.setText(s);
        status.repaint();
        System.out.println("Status -> " + s);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == start){
            controler.start();
        }else if(e.getSource() == testCom){
            controler.testComm();
        }else if(e.getSource() == stop){
            controler.stop();
        }else if(e.getSource() == comm){
            controler.setup();
        }
        RightScreen.comLog();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawLine(0,425,1100,425);

        //TSP
        g.drawRect(10, 435, 1080, 405);
        for(int i=0; i<5; i++){
            for(int j=0;j<5;j++){
                g.drawRect(i*216+10, j*81+435, 216, 80);
            }
        }
    }
}
