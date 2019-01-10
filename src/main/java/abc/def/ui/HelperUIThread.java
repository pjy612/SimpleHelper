package abc.def.ui;

import javax.swing.*;

public class HelperUIThread extends Thread {
    private JFrame frame;

    @Override
    public void run() {
        frame = new JFrame("HelperUI");
        frame.setContentPane(new HelperUI().rootPanel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.pack();
    }

    public void switchDisplay() {
        if (frame != null) {
            frame.setVisible(!frame.isVisible());
        }
    }
}
