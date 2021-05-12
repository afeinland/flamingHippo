import com.tngtech.java.junit.dataprovider.*;
import dataentities.Location;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import io.restassured.http.ContentType;
import org.junit.runner.RunWith;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;



@RunWith(DataProviderRunner.class)
public class TestCountryZipCode extends BaseTest {

    @Test
    public void testSingleUSZipCode() { // TODO test other countries
        String zipcode = "90210";
        String endpoint = "/us/" + zipcode;

        given().
        when().
            get(BASE_URL + endpoint).
        then().
//            log().all().
        assertThat().
            statusCode(200).
            contentType(ContentType.JSON).
            body("'post code'", equalTo(zipcode)). // 90210
            body("country", equalTo("United States")).
            body("'country abbreviation'", equalTo("US")).
            body("places[0].'place name'", equalTo("Beverly Hills")).
            body("places[0].'latitude'", equalTo("34.0901")).
            body("places[0].'longitude'", equalTo("-118.4065")).
            body("places[0].'state'", equalTo("California")).
            body("places[0].'state abbreviation'", equalTo("CA")).
            body("places[0].size()", is(5));
    }

    @DataProvider
    public static Object[][] usZipCodes() {
        return new Object[][] {
            {"US", "90210", "Beverly Hills"},
            {"US", "92124", "San Diego"},
            {"US", "92111", "San Diego"},
            {"US", "97214", "Portland"},
            {"US", "80302", "Boulder"}
        };
    }
    @Test
    @UseDataProvider("usZipCodes")
    public void testUSZipCodes(String countryCode, String zipCode, String city) {
        given().
            spec(requestSpec).
            pathParam("countryCode", countryCode).pathParam("zipCode", zipCode).
        when().
            get("/{countryCode}/{zipCode}").
        then().
            spec(responseSpec).
        assertThat().
            body("places.'place name'[0]", equalTo(city)).
            body("'post code'", equalTo(zipCode)).
            body("places.'place name'", hasSize(1));
    }

    @DataProvider
    public static Object[][] differentZipCodeFormats () {
        return new Object[][] {
            {"AD", "AD700", "Escaldes-Engordany"},
            {"AR", "9431", "LAGO PUELO"},
            {"BR", "01000-000", "São Paulo"},
            {"CA", "A0A", "Southeastern Avalon Peninsula (Ferryland)"},
            {"CZ", "798 62", "Rozstání"},
            {"FO", "970", "Sumba"},
            {"JP", "100-0001", "Chiyoda"},
            {"LK", "96167", "Idalgashinna"},
            {"LU", "L-1009", "Luxembourg"},
            {"MD", "MD-7731", "Varatic"}
        };
    }
    @Test
    @UseDataProvider("differentZipCodeFormats")
    public void testDifferentZipCodeFormats(String countryCode, String zipCode, String city) {
        given().
            spec(requestSpec).
            pathParam("countryCode", countryCode).pathParam("zipCode", zipCode).
        when().
            get("/{countryCode}/{zipCode}").
        then().
            spec(responseSpec).
            log().all().
        assertThat().
            body("places.'place name'[0]", equalTo(city)).
            body("'post code'", equalTo(zipCode)).
            body("places.'place name'", hasSize(1));
    }

    @Test // TODO finish this test
    public void testExtractZipCode() {
        String state =
        given().
            spec(requestSpec).
        when().
            get("/us/90210").
        then().
            spec(responseSpec).
            extract().
            path("places[0].state");

        Assert.assertEquals("California", state);
    }

    @Test // TODO finish this test
    public void testDeserializationLocation() {
        Location location =
        given().
            spec(requestSpec).
        when().
            get("/us/92124").
        as(Location.class);

        Assert.assertEquals("San Diego", location.getPlaces().get(0).getPlaceName());
    }

    @Ignore
    @Test // TODO finish this test
    public void testSerialization() {
         Location location = new Location();

         given().
             contentType(ContentType.JSON).
             body(location).
             log().body().
         when().
             post(BASE_URL). // TODO mock endpoint to post to
        then().
             assertThat().
             statusCode(200);
    }
}
