package com.simba.redis.service;

import com.simba.redis.model.Subscriber;

/**
 * redis集群
 */

public interface Cluster {


    void  sub(Subscriber subscriber, String... channels);

}
