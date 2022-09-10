package org.acme.example.pub;

import io.smallrye.reactive.messaging.annotations.Blocking;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Random;

@ApplicationScoped
public class KafkaBartender {

    @Inject Logger logger;

    @ConfigProperty(name = "name.collection")
    List<String> names;
    private final Random random = new Random();


    @Incoming("orders")
    @Outgoing("queue")
    @Blocking
    public Drink process(Order order) {
        return prepare(order);
    }

    Drink prepare(Order order) {
        int delay = getPreparationTime();
        int index = random.nextInt(names.size());

        String bartender = names.get(index);

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Drink drink = Drink.from(order, bartender, Drink.Status.READY);
        logger.infof("Order %s for %s is ready: %s and was prepared by %s", order.product(), order.customer(), drink.servedBy());
        return drink;
    }

    int getPreparationTime() {
        return random.nextInt(10) * 1000;
    }

}
