package task.sololearn.com.task.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class NotificationID {
    private final static AtomicInteger c = new AtomicInteger(0);

    public static int getID() {
        return c.incrementAndGet();

    }

    public static int getCurrentID() {
        return c.get();
    }
}