package org.acme.example.pub;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class BarResourceTest {

    @Test
    public void testProcess() {
        String order = """
                 {"product":"Heineken","customer":"Ana","orderId":"1"}
                """;
        given().body(order).header("Content-Type", "application/json")
                .when().post("/http")
                .then()
                .statusCode(200)
                .body(notNullValue());
    }

}