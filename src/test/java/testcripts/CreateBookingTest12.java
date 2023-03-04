package testcripts;
//GetBookingIds
//10.1 -- with Jackson Databind dependency in pom.xml
/* 
 * 1. Jackson library - for serialisation(converting Java object to Json format of payload) 
 * 		and deserialisation (converting response Json to Java object).
 * 2. Create POJO from Json payload
 * 3. Set data into Pojo
 * 4. Set that object in payload. [.body(payload)]
 * 
 */
import java.util.List;

/* 
 * 1. Jackson library - for serialisation(converting Java object to Json format of payload) 
 * 		and deserialisation (converting response Json to Java object).
 * 2. Create POJO from Json payload
 * 3. Set data into Pojo
 * 4. Set that object in payload. [.body(payload)]
 * 
 */
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import constants.Status_Code;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import pojo.request.createbooking.Bookingdates;
import pojo.request.createbooking.CreateBookingRequest;

public class CreateBookingTest12 {
	String respToken;
	int bookingID;
	
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
	
	//@Test
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
		Assert.assertEquals(res.getStatusCode(), Status_Code.OK);
		
	}
	
	@Test
	public void createBookingTestWithPOJO() {
		
		Bookingdates bookingDates = new Bookingdates();
		bookingDates.setCheckin("2018-01-01");
		bookingDates.setCheckout("2019-01-01");
		
		CreateBookingRequest payload = new CreateBookingRequest();
		payload.setFirstname("Gaurav");
		payload.setLastname("Kothadia");
		payload.setTotalprice(5);
		payload.setDepositpaid(true);
		payload.setBookingdates(bookingDates);
		payload.setAdditionalneeds("breakfast");
		
		Response res = RestAssured.given()
		.headers("Content-Type", "application/json")
		.headers("Accept","application/json")
		.body(payload)
		.log().all()
		.when()
		.post("/booking");
		
		System.out.println(res.getStatusCode());
		System.out.println(res.getStatusLine());
		Assert.assertEquals(res.getStatusCode(), Status_Code.OK);
		
		bookingID = res.jsonPath().getInt("bookingid");
		//bookingID = -30;	//for negative testing //
		Assert.assertTrue(bookingID>0);
		
		/*------Checking if the content of bookingid is integer by converting it to integer using Integer wrapper class-----
		
		Assert.assertTrue(Integer.valueOf(res.jsonPath().getInt("bookingid")) instanceof Integer);
		
		*/
		
		Assert.assertEquals(res.jsonPath().getString("booking.firstname"), payload.getFirstname());
		Assert.assertEquals(res.jsonPath().getString("booking.lastname"), payload.getLastname());
		Assert.assertEquals(res.jsonPath().getInt("booking.totalprice"), payload.getTotalprice());
		Assert.assertEquals(res.jsonPath().getBoolean("booking.depositpaid"), payload.isDepositpaid());
		Assert.assertEquals(res.jsonPath().getString("booking.additionalneeds"), payload.getAdditionalneeds());
		Assert.assertEquals(res.jsonPath().getString("booking.bookingdates.checkin"), payload.getBookingdates().getCheckin());
		Assert.assertEquals(res.jsonPath().getString("booking.bookingdates.checkout"), payload.getBookingdates().getCheckout());
				
	}
	
	@Test (priority = 1)
	public void getAllBookingTest() {
		Response res = RestAssured.given()
				.headers("Accept", "application/json")
				.log().all()
				.when()
				.get("/booking");
		
		Assert.assertEquals(res.getStatusCode(), Status_Code.OK);
		//System.out.println(res.asPrettyString());
		
		List<Integer> listOfBookingID = res.jsonPath().getList("bookingid");
		System.out.println(listOfBookingID.size());
		Assert.assertTrue(listOfBookingID.contains(bookingID), "Booking ID is NOT found in the database."+bookingID);
	
	}
	
	//@Test
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
