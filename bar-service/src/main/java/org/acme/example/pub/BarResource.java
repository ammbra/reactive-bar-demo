package org.acme.example.pub;


import org.acme.example.pub.service.BartenderService;
import org.acme.example.pub.model.Drink;
import org.acme.example.pub.model.Order;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Path("/")
public class BarResource {

    @RestClient
    BartenderService service;

    @Inject
    Logger logger;

    @POST
    @Path("http")
    public Drink process(Order order) {
        logger.infof("Received order %s on /http", order);
        Drink beverage = service.order(order.withOrderId(getId()));
        return beverage.ready();
    }

    @Channel("orders") Emitter<Order> orders;
    @Channel("queue") Emitter<Drink> queue;

    @POST
    @Path("notify")
    public Order notify(Order order) {
        order = order.withOrderId(getId());
        logger.infof("Received order %s on /notify", order);
        orders.send(order);
        queue.send(Drink.queued(order));
        return order;
    }

    private String getId() {
        return UUID.randomUUID().toString();
    }

}
