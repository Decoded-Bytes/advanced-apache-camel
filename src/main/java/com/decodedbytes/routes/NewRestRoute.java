package com.decodedbytes.routes;

import com.decodedbytes.beans.InboundNameAddress;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.net.ConnectException;

@Component
public class NewRestRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration().component("servlet").host("0.0.0.0").port(8080).bindingMode(RestBindingMode.json).enableCORS(true);

        onException(JMSException.class, ConnectException.class)
                .handled(true)
                .log(LoggingLevel.INFO, "JMS connection could not be established");

        rest("masterclass")
                .produces("application/json")
                .post("nameAddress").type(InboundNameAddress.class).route().routeId("newRestRouteId")
                .log(LoggingLevel.INFO, String.valueOf(simple("${body}")))
                .to("direct:persistMessage")
                .wireTap("seda:sendToQueue")

                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .transform().simple("Message Processed: ${body}")
                .endRest();

        from("timer:startBatch?repeatCount=1&delay=10000")
                .routeId("timerRunOnceIdAt15")
                .to("controlbus:route?routeId=batchMessageRouteId&action=start")
                .to("controlbus:route?routeId=batchMessageRouteId&action=status")
                .to("controlbus:route?routeId=activeMQRouteId&action=status");

//        from("timer:startBatch?repeatCount=1&delay=20000")
//                .routeId("timerRunOnceIdAt30")
//                .to("controlbus:route?routeId=activeMQRouteId&action=stop")
//                .to("controlbus:route?routeId=batchMessageRouteId&action=status")
//                .to("controlbus:route?routeId=activeMQRouteId&action=status");


        from("direct:persistMessage")
                .routeId("persistMessageRouteId")
                .to("jpa:"+InboundNameAddress.class.getName());

        from("seda:sendToQueue")
                .routeId("sendToQueueRouteId")
                .to("activemq:queue:nameaddressqueue");

    }
}
