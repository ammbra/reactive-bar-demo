package org.acme.example.pub.model;

public record Drink(String product, String customer, String servedBy, String orderId, Status status) {


    public static Drink queued(Order order) {
        return new Drink(order.product(), order.customer(), null, order.orderId(), Status.IN_PROGRESS);
    }

    public Drink ready() {
        if ( status != Status.FAILED) {
            return new Drink(product, customer, servedBy, orderId, Status.READY);
        }
        return this;
    }

    public enum Status {
        RECEIVED,
        IN_PROGRESS,
        READY,
        FAILED
    }

}

