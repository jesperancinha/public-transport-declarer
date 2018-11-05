package com.jesperancinha.biscaje.queues;

/**
 * Created by joaofilipesabinoesperancinha on 04-07-16.
 */
public class UserPlayStats {

    public static void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }

}
