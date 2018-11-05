package com.jesperancinha.biscaje.queues;

import org.junit.Test;

/**
 * Created by joaofilipesabinoesperancinha on 07-07-16.
 */
public class UserPlayStatsTest {

    @Test
    public void thread() throws Exception {
        UserPlayStats.thread(new UserPlayStatsProducer(Stats.builder().nPlayers(2).build()), false);
        Thread.sleep(1000);
        final UserPlayStatsConsumer userPlayStatsConsumer = new UserPlayStatsConsumer();
        UserPlayStats. thread(userPlayStatsConsumer, false);
        Thread.sleep(1000);
    }

}