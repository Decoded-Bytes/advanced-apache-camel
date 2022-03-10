package com.decodedbytes.config;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.camel.component.hazelcast.policy.HazelcastRoutePolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class HazelCastConfiguration {

    @Bean
    public HazelcastRoutePolicy getHazelCastRoutePolicy1(){

        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        HazelcastRoutePolicy policy = new HazelcastRoutePolicy(hz);

        
        policy.setLockMapName("testLock");
        policy.setLockKey("testLockKey");
        policy.setLockValue("testLockValue");
        policy.setTryLockTimeout(5, TimeUnit.SECONDS);
        return policy;
    }

}
