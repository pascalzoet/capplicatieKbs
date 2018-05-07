package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LeftScreen extends JPanel implements ActionListener{

    private JButton start, stop, reset, testCom;

    public LeftScreen(){

        setPreferredSize(new Dimension(1100,850));
        setLayout(null);

        start = new JButton("Start");
        start.addActionListener(this);
        start.setBounds(802, 10, 88, 51);
        add(start);

        stop = new JButton("Stop");
        stop.addActionListener(this);
        stop.setBounds(906, 10, 88, 51);
        add(stop);

        reset = new JButton("Reset");
        reset.addActionListener(this);
        reset.setBounds(1010, 10, 88, 51);
        add(reset);

        testCom = new JButton("Test communicatie");
        testCom.addActionListener(this);
        testCom.setBounds(802, 70, 296, 51);
        add(testCom);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
