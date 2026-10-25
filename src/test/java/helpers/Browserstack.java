package helpers;

import config.BrowserstackConfig;
import org.aeonbits.owner.ConfigFactory;

import static io.restassured.RestAssured.given;

public class Browserstack {
    private static final BrowserstackConfig config = ConfigFactory.create(BrowserstackConfig.class, System.getProperties(), System.getenv());

    public static String videoUrl(String sessionId) {

        String url = String.format("https://api-cloud.browserstack.com/app-automate/sessions/%s.json", sessionId);

        return given()
                .auth().basic(config.browserStackUser(), config.browserStackKey())
                .get(url)
                .then()
                .log().status()
                .statusCode(200)
                .extract().path("automation_session.video_url");
    }
}