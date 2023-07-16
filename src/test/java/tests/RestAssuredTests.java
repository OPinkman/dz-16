package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.*;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class RestAssuredTests extends BeforeTestMethod {
    @Test
    public void getAllBookingsByIdTest(){
        bookingsById.then().statusCode(200);
    }
    @Test
//    сюди добавити перевірку на корреткну структуру джсона
    public void createBooking(){
        CreateBookingBody body = new CreateBookingBody().builder()
                .firstname("Alexey")
                .lastname("Petrov")
                .totalprice(123)
                .depositpaid(true)
                .bookingdates(BookingDateBody.builder()
                        .checkin("2023-07-15")
                        .checkout("2023-07-20")
                        .build())
                .additionalneeds("Huge pillow")
                .build();
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(body)
                .post("/booking");
        response.then().assertThat().body(matchesJsonSchemaInClasspath("jsonSchemaBookingInfo.json"));
    }
    @Test
    public void changeBookingPrice(){
        bookingsById.then().statusCode(200);
        int bookingId = bookingsById.jsonPath().get("bookingid.find{it<100}");
        JSONObject body = new JSONObject();
        body.put("totalprice", priceForTest);
        Response response = RestAssured.given()
                .queryParam("id",bookingId)
                .body(body.toString())
                .patch("/booking/"+bookingId);
        response.prettyPrint();
        response.then().statusCode(200);
        response.then().assertThat().body("totalprice", equalTo(priceForTest));
    }
    @Test
    public void changeBookingAddinfoandName (){
        bookingsById.then().statusCode(200);
        int bookingId = bookingsById.jsonPath().get("bookingid.find{it>100}");
        Response response = RestAssured.get("/booking/" + bookingId);
        String responseBody = response.getBody().asString();
        JSONObject bookingInfo = new JSONObject(responseBody);
        JSONObject bookingDates = bookingInfo.getJSONObject("bookingdates");
        CreateBookingBody body = new CreateBookingBody().builder()
                .firstname(changeName)
                .lastname(bookingInfo.getString("lastname"))
                .totalprice(bookingInfo.getInt("totalprice"))
                .depositpaid(bookingInfo.getBoolean("depositpaid"))
                .bookingdates(BookingDateBody.builder()
                        .checkin(bookingDates.getString("checkin"))
                        .checkout(bookingDates.getString("checkout"))
                        .build())
                .additionalneeds(changeAddinfo)
                .build();
        Response response2 = RestAssured.given()
                .queryParam("id",bookingId)
                .body(body.toString())
                .put("/booking/"+bookingId);
        response.prettyPrint();
        response.then().statusCode(200);
        response.then().assertThat().body("firstname", equalTo(changeName));
        response.then().assertThat().body("additionalneeds", equalTo(changeAddinfo));
    }
    @Test
    public void deleteBookingById(){
        bookingsById.then().statusCode(200);
        int bookingId = bookingsById.jsonPath().get("bookingid.find{it>1000}");
        int testBookingId = bookingId;
        Response response = RestAssured.given()
                .queryParam("id",bookingId)
                .delete("/booking/"+bookingId);
        response.prettyPrint();
        response.then().statusCode(201);
        response.then().assertThat().body(not(containsString("\"id\": " + testBookingId)));
    }
}
