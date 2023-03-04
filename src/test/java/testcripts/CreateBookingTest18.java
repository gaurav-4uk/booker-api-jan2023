package testcripts;
//DeleteBooking
//10.2 --
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
import io.restassured.mapper.ObjectMapper;
import io.restassured.response.Response;
import pojo.request.createbooking.Bookingdates;
import pojo.request.createbooking.CreateBookingRequest;

public class CreateBookingTest18 {
	String respToken;
	int bookingID;
	CreateBookingRequest payload;
	
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
	
	@Test (enabled = false)
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
		
		payload = new CreateBookingRequest();
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
		System.out.println("New Booking ID : "+bookingID);
		//bookingID = -30;	//for negative testing //
		Assert.assertTrue(bookingID>0);
		
		/*------Checking if the content of bookingid is integer by converting it to integer using Integer wrapper class-----
				Assert.assertTrue(Integer.valueOf(res.jsonPath().getInt("bookingid")) instanceof Integer);
		*/
		
		validateResponse(res, payload, "booking.");
				
	}
	
	private void validateResponse(Response res, CreateBookingRequest payload, String obj) {
		Assert.assertEquals(res.jsonPath().getString(obj+"firstname"), payload.getFirstname());
		Assert.assertEquals(res.jsonPath().getString(obj+"lastname"), payload.getLastname());
		Assert.assertEquals(res.jsonPath().getInt(obj+"totalprice"), payload.getTotalprice());
		Assert.assertEquals(res.jsonPath().getBoolean(obj+"depositpaid"), payload.isDepositpaid());
		Assert.assertEquals(res.jsonPath().getString(obj+"additionalneeds"), payload.getAdditionalneeds());
		Assert.assertEquals(res.jsonPath().getString(obj+"bookingdates.checkin"), payload.getBookingdates().getCheckin());
		Assert.assertEquals(res.jsonPath().getString(obj+"bookingdates.checkout"), payload.getBookingdates().getCheckout());
	}
	
	@Test (priority = 1, enabled = false)
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
	
	@Test (priority = 2, enabled = false)
	public void getBookingIDTest() {
		
		Response res = RestAssured.given()
				.headers("Accept", "application/json")
				.log().all()
				.when()
				.get("/booking/"+bookingID);
		
		Assert.assertEquals(res.getStatusCode(), Status_Code.OK);
		System.out.println(res.asPrettyString());
		
		validateResponse(res, payload, "");
		
	}
	
	@Test (priority = 2, enabled = false)
	public void getBookingIDDeserializedTest() {
		
		Response res = RestAssured.given()
				.headers("Accept", "application/json")
				.log().all()
				.when()
				.get("/booking/"+bookingID);
		
		Assert.assertEquals(res.getStatusCode(), Status_Code.OK);
		System.out.println(res.asPrettyString());
		
		//------Deserialization------------
		CreateBookingRequest responseBody = res.as(CreateBookingRequest.class);
		//System.out.println(responseBody);
		
		//payload : all details of request
		//responseBody : all details from getBooking
		
		Assert.assertTrue(payload.equals(responseBody));
		
	}
	
	@Test (priority = 3, enabled=false)
	public void updateBookingIDTest() {
		
		payload.setFirstname("Dimple");
		Response res = RestAssured.given()
				.headers("Content-Type", "application/json")
				.headers("Accept","application/json")
				.headers("Cookie","token="+respToken)
				.log().all()
				.body(payload)
				.when()
				.put("/booking/"+bookingID);
		
		Assert.assertEquals(res.getStatusCode(), Status_Code.OK);
		System.out.println(res.asPrettyString());
		
		CreateBookingRequest respBody = res.as(CreateBookingRequest.class);
		Assert.assertTrue(respBody.equals(payload));
		
	}
	
	@Test (priority = 4, enabled=false)
	public void partialUpdateBookingIDTest() {
		
		payload.setFirstname("Dimple Gaurav");
		//payload.setAdditionalneeds("lunch");
		
		Response res = RestAssured.given()
				.headers("Content-Type", "application/json")
				.headers("Accept","application/json")
				.headers("Cookie","token="+respToken)
				.log().all()
				.body(payload)
	//			.body(payload, payload.setDepositpaid(false))
				.when()
				.patch("/booking/"+bookingID);
		
		Assert.assertEquals(res.getStatusCode(), Status_Code.OK);
		System.out.println(res.asPrettyString());
		
		CreateBookingRequest respBody = res.as(CreateBookingRequest.class);
		Assert.assertTrue(respBody.equals(payload));
		
	}
	
	@Test (priority = 5)
	public void deleteBookingIDTest() {
		
		Response res = RestAssured.given()
				.headers("Content-Type", "application/json")
				.headers("Cookie","token="+respToken)
				.log().all()
				.when()
				.delete("/booking/"+bookingID);
		
		Assert.assertEquals(res.getStatusCode(), Status_Code.CREATED);
		System.out.println(res.asPrettyString());
		
		Response res1 = RestAssured.given()
				.headers("Accept", "application/json")
				.log().all()
				.when()
				.get("/booking/"+bookingID);
		
		Assert.assertEquals(res1.getStatusCode(), Status_Code.NOT_FOUND);
		System.out.println(res1.asPrettyString());
		
	}
	
	@Test (enabled = false)
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
