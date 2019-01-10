package abc.def;

import abc.def.ui.HelperUIThread;
import org.junit.Test;

import java.util.Scanner;

import static org.junit.Assert.*;

public class SimpleHelperTest {

    @Test
    public void switchUI() {
        System.out.println("Test begin");
        try {
            for (int i=0; i<3; i++) {
                SimpleHelper.switchUI();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Test end");
    }
}