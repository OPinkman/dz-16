package tests;


import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;

import java.util.List;

public class BeforeTestMethod {
    protected int priceForTest = 567;
    protected String changeName = "Pipi-pupu";
    protected String changeAddinfo = "At least 10 pillows must be in my bad";
    protected Response bookingsById = RestAssured.given().log().all().get("https://restful-booker.herokuapp.com/booking");

    @BeforeTest
    public void setup(){
        RestAssured.baseURI = ("https://restful-booker.herokuapp.com");
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .build();
    }
}
