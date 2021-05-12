import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@RunWith(DataProviderRunner.class)
public class TestCountryStateCity extends BaseTest{


    @Test
    public void testCaliforniaCities() {

    }


    @Test
    public void testIncompleteCityName() { // TODO test 'san', 'san d'

    }
}
