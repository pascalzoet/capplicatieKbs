package Main;

public class Main {
    public static void main(String[] args) {
        Control control = new Control();
        Screen screen = new Screen(control);
        screen.setVisible(true);
    }
}
