package com.jesperancinha.biscaje.queues;

import javax.ejb.Local;
import javax.jms.ExceptionListener;

/**
 * Created by joaofilipesabinoesperancinha on 06-07-16.
 */

@Local
public interface RemoteMessageConsumer extends  Runnable, ExceptionListener {
}
