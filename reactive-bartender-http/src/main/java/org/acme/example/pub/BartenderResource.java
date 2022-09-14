package org.acme.example.pub;

import javax.ws.rs.Path;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.POST;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



@Path("bartender")
public class BartenderResource {

    @Inject
    Logger logger;

    @ConfigProperty(name = "name.collection")
    List<String> names;
    private final Random random = new Random();

    private final ExecutorService queue = Executors.newSingleThreadExecutor();

    @POST
    public Uni<Drink> process(Order order) {
        return Uni.createFrom().item(() -> {
            Drink beverage = prepare(order);
            logger.infof("Order %s for %s is ready and was prepared by %s", order.product(), order.customer(), beverage.servedBy());
            return beverage;
        }).runSubscriptionOn(queue);
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

        return Drink.from(order, bartender);
    }


    int getPreparationTime() {
        return random.nextInt(10) * 1000;
    }

}
