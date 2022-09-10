package org.acme.example.pub;

public record Drink(String product, String customer, String servedBy, String orderId, Status status) {


    public enum Status {
        RECEIVED,
        IN_PROGRESS,
        READY;

    }

    public static Drink from(Order order, String bartenderName, Status status) {
        return new Drink(order.product(),
                         order.customer(),
                         bartenderName,
                         order.orderId(),
                         status);
    }

}
