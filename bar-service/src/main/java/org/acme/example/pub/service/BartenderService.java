package org.acme.example.pub.service;

import org.acme.example.pub.model.Drink;
import org.acme.example.pub.model.Order;
import org.eclipse.microprofile.faulttolerance.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

@Path("bartender")
@RegisterRestClient(configKey = "bartender-http")
public interface BartenderService {

    @POST
    @Timeout(value = 5, unit = ChronoUnit.SECONDS)
    @Retry(maxRetries = 10, delay = 1, delayUnit = ChronoUnit.SECONDS)
    @Fallback(DrinkFallback.class)
    Drink order(Order order);


    public record DrinkFallback() implements FallbackHandler<Drink> {


        @Override
        public Drink handle(ExecutionContext executionContext) {
            Order order = (Order) Arrays.stream(executionContext.getParameters()).findFirst().get();
            return new Drink(order.product(), order.customer(), null, order.orderId(), Drink.Status.FAILED);
        }
    }
}
