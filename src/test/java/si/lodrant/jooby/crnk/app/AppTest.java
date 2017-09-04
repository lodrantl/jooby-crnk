package si.lodrant.jooby.crnk.app;

import org.jooby.test.JoobyRule;
import org.junit.Rule;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class AppTest {

    /**
     * Every test gets a new and blank App instance
     */
    @Rule
    public JoobyRule app = new JoobyRule(new CrnkApp());

    @Test
    public void basicTest() {
        get("/api/boxes").then()
                .assertThat()
                .statusCode(200)
                .contentType("application/vnd.api+json;charset=UTF-8");
    }

    @Test
    public void jpaTest() {
        get("/api/beer").then()
                .assertThat()
                .statusCode(200)
                .contentType("application/vnd.api+json;charset=UTF-8");
    }
}