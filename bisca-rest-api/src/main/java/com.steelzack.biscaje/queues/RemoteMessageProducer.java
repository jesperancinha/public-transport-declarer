package com.steelzack.biscaje.queues;

import javax.ejb.Local;
import javax.jms.JMSException;
import java.io.Serializable;

/**
 * Created by joaofilipesabinoesperancinha on 06-07-16.
 */
@Local
public interface RemoteMessageProducer extends Serializable {
    void sendMessage(Stats stats) throws JMSException;
}
