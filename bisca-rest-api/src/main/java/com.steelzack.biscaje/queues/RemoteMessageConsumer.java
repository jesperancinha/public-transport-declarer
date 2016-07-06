package com.steelzack.biscaje.queues;

import javax.ejb.Local;
import java.io.Serializable;

/**
 * Created by joaofilipesabinoesperancinha on 06-07-16.
 */

@Local
public interface RemoteMessageConsumer extends Serializable{
    Stats receiveMessage();
}
