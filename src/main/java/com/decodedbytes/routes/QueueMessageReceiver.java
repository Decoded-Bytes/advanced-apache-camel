package com.decodedbytes.routes;

import com.decodedbytes.policies.CustomRoutePolicy;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.RoutePolicy;
import org.springframework.stereotype.Component;

@Component
public class QueueMessageReceiver extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        RoutePolicy dependentRoutePolicy = new CustomRoutePolicy("batchMessageRouteId","activeMQRouteId");

        from("activemq:queue:nameaddressqueue")
                .autoStartup(false)
                .routeId("activeMQRouteId")
                .routePolicy(dependentRoutePolicy)
                .log(LoggingLevel.INFO, ">>>>>>>>>>> Received Queue Message: ${body}");
    }
}
