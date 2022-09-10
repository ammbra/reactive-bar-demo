package org.acme.example.pub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.mutiny.Multi;
import org.acme.example.pub.model.Drink;
import org.eclipse.microprofile.reactive.messaging.Channel;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.Duration;

@Path("queue")
public class BoardResource {

    @Channel("drinks")
    Multi<Drink> queue;

    @Inject
    ObjectMapper mapper;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> getQueue() {
        return Multi.createBy().merging()
                .streams(
                        queue.map(this::toJson),
                        getPingStream()
                );
    }

    Multi<String> getPingStream() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(10))
                .onItem().transform(x -> "{}");
    }

    private String toJson(Drink b) {
        try {
            return mapper.writeValueAsString(b);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

}
