package testcripts;
//9.3
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class CreateBookingTest6 {
	String respToken;
	
	@BeforeMethod
	public void generateToken() {
		RestAssured.baseURI = "https://restful-booker.herokuapp.com";
		
		Response res = RestAssured.given()
			.headers("Content-Type","application/json")
			.body("{\r\n"
					+ "    \"username\" : \"admin\",\r\n"
					+ "    \"password\" : \"password123\"\r\n"
					+ "}")
			.when()
			.post("/auth");
		System.out.println("response .asPrettyString(): "+res.asPrettyString());
//		System.out.println("response .asString(): "+res.asString());
//		System.out.println("response .prettyPrint(): "+res.prettyPrint());
		
		System.out.println("response .statusCode(): "+res.statusCode());
		Assert.assertEquals(res.statusCode(), 200);
		
		 respToken = res.jsonPath().getString("token");
		System.out.println(respToken);
	}
	
	@Test
	public void createBookingTest() {
		Response res = RestAssured.given()
		.headers("Content-Type", "application/json")
		.headers("Accept","application/json")
		.body("{\r\n"
				+ "    \"firstname\" : \"Gaurav\",\r\n"
				+ "    \"lastname\" : \"Kothadia\",\r\n"
				+ "    \"totalprice\" : 111,\r\n"
				+ "    \"depositpaid\" : true,\r\n"
				+ "    \"bookingdates\" : {\r\n"
				+ "        \"checkin\" : \"2018-01-01\",\r\n"
				+ "        \"checkout\" : \"2019-01-01\"\r\n"
				+ "    },\r\n"
				+ "    \"additionalneeds\" : \"Breakfast\"\r\n"
				+ "}")
		.when()
		.post("/booking");
		
		System.out.println(res.getStatusCode());
		System.out.println(res.getStatusLine());
		Assert.assertEquals(res.getStatusCode(), 200);
		
	}
	
	@Test
	public void createBookingTestInPlainMode() {
		RestAssured.baseURI = "https://restful-booker.herokuapp.com";
		
		Response res = RestAssured.given()
			.headers("Content-Type","application/json")
			.body("{\r\n"
					+ "    \"username\" : \"admin\",\r\n"
					+ "    \"password\" : \"password123\"\r\n"
					+ "}")
			.when()
			.post("/auth");
		System.out.println("response .asPrettyString(): "+res.asPrettyString());
		System.out.println("response .asString(): "+res.asString());
		System.out.println("response .prettyPrint(): "+res.prettyPrint());
		
		System.out.println("response .statusCode(): "+res.statusCode());
		Assert.assertEquals(res.statusCode(), 200);
		
		 respToken = res.jsonPath().getString("token");
		System.out.println(respToken);
	}

}
