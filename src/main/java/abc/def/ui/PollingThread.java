package abc.def.ui;

import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.locks.Lock;

public class PollingThread extends Thread implements Runnable {

    public static Queue<Runnable> queue = new LinkedTransferQueue<Runnable>();

    @Override
    public void run() {
        while (true) {
            while (!queue.isEmpty()) {
                queue.poll().run();
            }
            //把队列中的消息全部打印完之后让线程阻塞
            synchronized (Lock.class) {
                try {
                    Lock.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
