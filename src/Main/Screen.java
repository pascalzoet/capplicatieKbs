package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Screen extends JFrame implements ActionListener {

    private JPanel left = new LeftScreen();
    private JPanel right = new RightScreen();


    public Screen() {
        setSize(1400, 900);
        setTitle("Robot applicatie");
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        layout.setVgap(0);
        layout.setHgap(0);

        setLayout(layout);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        add(left);
        add(right);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
