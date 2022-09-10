package org.acme.example.pub;


public record Drink(String product, String customer, String servedBy, String orderId) {

public static Drink from(Order order, String bartenderName) {
        return new Drink(
        order.product(),
        order.customer(),
        bartenderName,
        order.orderId());
        }
}
