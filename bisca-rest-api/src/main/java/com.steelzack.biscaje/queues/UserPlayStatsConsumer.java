package com.steelzack.biscaje.queues;

import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;
import javax.jms.Queue;

/**
 * Created by joaofilipesabinoesperancinha on 06-07-16.
 */
@Stateless
@Remote(RemoteMessageConsumer.class)
@JMSDestinationDefinitions(
        value = {
                @JMSDestinationDefinition(
                        name = "jms/StatsQueue",
                        interfaceName = "javax.jms.Queue",
                        destinationName = "playStats"
                )
        }
)
public class UserPlayStatsConsumer implements RemoteMessageConsumer{

    private static final long serialVersionUID = 2996906737179119206L;
    /**
     * JMS Context, This combines in a single object the functionality of two
     * separate objects from the JMS 1.1 API: a Connection and a Session.
     *
     */
    @Inject
    @JMSConnectionFactory("MyConnectionFactory")
    JMSContext context;
    /**
     * Queue
     */
    @Resource(mappedName = "jms/StatsQueue")
    Queue queue;

    /**
     * Receive Message.
     */
    @Override
    public Stats receiveMessage() {
        Stats message = context.createConsumer(queue)
                .receiveBody(Stats.class);
        return message;
    }}
