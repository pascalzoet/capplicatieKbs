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

        Graphics2D g2 = (Graphics2D) g;

        g.drawLine(0,425,1100,425);

        //TSP
        g.drawRect(10, 435, 1080, 405);
        for(int i=0; i<5; i++){
            for(int j=0;j<5;j++){
                g.drawRect(i*216+10, j*81+435, 216, 80);
            }
        }
        if(controler.isSolved()){
            g2.setColor(Color.red);
            for(Route r : controler.getRoutes()){
                for(int i=0; i < r.getPoints().size()-1; i++){
                    Point p1 = r.getPoints().get(i);
                    Point p2 = r.getPoints().get(i+1);

                    g2.setStroke(new BasicStroke(3));
                    g2.drawLine(p1.getTrueX(),p1.getTrueY(),p2.getTrueX(),p2.getTrueY());
                    g.drawOval(p1.getTrueX()-5,p1.getTrueY()-5,10,10);
                    g.drawOval(p2.getTrueX()-5,p2.getTrueY()-5,10,10);
                }
                g2.setColor(Color.BLUE);
            }
        }
        g2.setColor(Color.black);


        //BPP
        int x = 40;
        int y = 30;
        int w = 100;
        int h = 310;

        for(Box b: controler.getBoxes()){
            g2.setStroke(new BasicStroke(5));
            g2.drawRect(x,y,w,h);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 18));
            g.drawString((b.getSize() - b.getSizeLeft()) + "/" + b.getSize(), x+30, h+60);

            int py = y + h-10;
            int ph;
            for(Product p: b.getProducts()){
                ph = toIntExact(map(p.getSize(), 0, b.getSize(), 0, 290));
                g2.drawRect(x+10,py-ph, w-20, ph);
                if(p.getSize() >= 10){
                    g.drawString(Integer.toString(p.getSize()), x+((w-20)/2),py-(ph/2)+5);
                }else{
                    if(p.getSize() == 1){
                        g.setColor(Color.red);
                        g2.setStroke(new BasicStroke(2));
                        g.drawLine(x+20+((w-20)/2), py-(ph/2), x+10+w, py-(ph/2));
                        g.setColor(Color.black);
                        g2.setStroke(new BasicStroke(5));
                        g.drawString(Integer.toString(p.getSize()), x+10+w,py-(ph/2)+5);
                    }else{
                        g.drawString(Integer.toString(p.getSize()), x+5+((w-20)/2),py-(ph/2)+5);
                    }
                }
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
