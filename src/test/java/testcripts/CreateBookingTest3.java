package testcripts;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class CreateBookingTest3 {
	@Test
	public void createBookingTest() {
		RestAssured.baseURI = "https://restful-booker.herokuapp.com";
		
		Response res = RestAssured.given()
			.headers("Content-Type","application/json")
			.body("{\r\n"
					+ "    \"username\" : \"admin\",\r\n"
					+ "    \"password\" : \"password123\"\r\n"
					+ "}")
			.when()
			.post("/auth");
		System.out.println(res.asPrettyString());
	}

}
