package com.decodedbytes.routes;

import com.decodedbytes.policies.CustomRoutePolicy;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.hazelcast.policy.HazelcastRoutePolicy;
import org.apache.camel.spi.RoutePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class QueueMessageReceiver extends RouteBuilder {

    @Autowired
    HazelcastRoutePolicy routePolicy;

    @Override
    public void configure() throws Exception {

        RoutePolicy dependentRoutePolicy = new CustomRoutePolicy("batchMessageRouteId","activeMQRouteId");

        from("activemq:queue:nameaddressqueue")
                .autoStartup(false)
                .routeId("activeMQRouteId")
                .routePolicy(dependentRoutePolicy, routePolicy)
                .log(LoggingLevel.INFO, ">>>>>>>>>>> Received Queue Message: ${body}");
    }
}
