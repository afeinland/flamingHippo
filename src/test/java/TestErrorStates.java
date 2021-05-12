import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.restassured.RestAssured.given;

@RunWith(DataProviderRunner.class)
public class TestErrorStates extends BaseTest{

    @DataProvider
    public static Object[][] invalidPathParams() {
        return new Object[][] {
            {"/US/1234"}, // incorrect zip code
            {"/US/11223344"}, // incorrect zip code
            {"/U/90210"}, // incorrect country code
            {"/ABCDEFG/90210"}, // incorrect country code
            {"/US/C/Belmont"}, // incorrect state
            {"/US/CAAAAAA/Belmont"}, // incorrect state
            {"/US/CA/Belmontttttttt"}, // incorrect city
            {"/US/CA/.Belmont"}, // incorrect city
            {"/90210/US"} // swapped country and zip code
        };
    }
    @Test
    // Test bad path (invalid zip or country code)
    @UseDataProvider("invalidPathParams")
    public void testInvalidPathParameters(String path) {
       given().
           spec(requestSpec).
       when().
           get(path).
       then().
//           log().all().
       assertThat().
           contentType(ContentType.JSON).
           statusCode(404);
    }

    @DataProvider
    public static Object[][] missingPathParams() {
        return new Object[][] {
            {"//90210"}, // missing country
            {"/ /90210"}, // missing country
            {"/US//92111"}, // missing state
            {"/US/' '/92111"}, // missing state
            {"/US/CA/"}, // missing city
            {"/US/CA/' '"}, // missing city
            {"/SP"}, // missing zip code
            {"/SP/ "} // missing zip code
        };
    }
    // Test missing path params (missing country, missing state, missing city, missing zip)
    @Test
    @UseDataProvider("missingPathParams")
    public void testMissingPathParameters(String path) {
        given().
            spec(requestSpec).
        when().
            get("path").
        then().
            log().all().
        assertThat().
            contentType(ContentType.HTML).
            statusCode(404);
    }
}
