package abc.def.ui;

import abc.def.SimpleHelper;

import javax.swing.*;

public class HelperUIThread extends Thread {
    private JFrame frame;

    @Override
    public void run() {
        SimpleHelper.loaded = true;
        frame = new JFrame("HelperUI");
        frame.setContentPane(new HelperUI().rootPanel);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.pack();
    }

    public void switchDisplay() {
        if (frame != null) {
            frame.setVisible(!frame.isVisible());
        }
    }
}
