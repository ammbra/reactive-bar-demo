package org.acme.example.pub.model;

public record Order(String product, String customer, String orderId) {

    public Order withOrderId(String id) {
        return new Order(product, customer, id);
    }
}