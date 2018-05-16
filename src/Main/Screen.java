package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;

public class Screen extends JFrame implements ActionListener {

    private JPanel left;
    private JPanel right;
    private Control controler;
    private ByteArrayOutputStream baos;

    public Screen(Control control, ByteArrayOutputStream baos) {
        this.controler = control;
        this.baos = baos;

        setSize(1400, 900);
        setTitle("Robot applicatie");
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        layout.setVgap(0);
        layout.setHgap(0);
        setLayout(layout);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        left = new LeftScreen(control);
        right = new RightScreen(baos, this);
        add(left);
        add(right);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
