package com.decodedbytes.policies;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Route;
import org.apache.camel.support.RoutePolicySupport;

public class CustomRoutePolicy extends RoutePolicySupport {

    private final String routeName1;
    private final String routeName2;

    public CustomRoutePolicy(String routeName1, String routeName2) {
        this.routeName1 = routeName1;
        this.routeName2 = routeName2;
    }

    @Override
    public void onStart(Route route) {
        CamelContext camelContext = route.getCamelContext();
        try {
            camelContext.getRouteController().startRoute(routeName1);
            camelContext.getRouteController().startRoute(routeName2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop(Route route) {
        CamelContext camelContext = route.getCamelContext();
        try {
            camelContext.getRouteController().stopRoute(routeName1);
            camelContext.getRouteController().stopRoute(routeName2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
