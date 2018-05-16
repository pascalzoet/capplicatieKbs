package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Math.toIntExact;

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

        int x = 40;
        int y = 30;
        int w = 100;
        int h = 300;
        for(Box b: controler.getBoxes()){
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(5));
            g2.drawRect(x,y,w,h);
            g.drawString(Integer.toString(b.getSize()), x+40, h+45);

            int py = y + h-10;
            int ph;
            for(Product p: b.getProducts()){
                ph = toIntExact(map(p.getSize(), 1, 20, 10, 300));
                g2.drawRect(x+10,py-ph, w-20, ph);
                g.drawString(Integer.toString(p.getSize()), x+5+((w-20)/2),py-(ph/2)+5);
                py -= ph;
            }

            x+=150;
        }
    }

    private long map(long x, long in_min, long in_max, long out_min, long out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}
