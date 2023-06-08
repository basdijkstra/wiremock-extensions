import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import extensions.ContentTypeMatcher;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.given;

public class ContentTypeMatchingTest {

    private RequestSpecification requestSpec;

    @RegisterExtension
    static WireMockExtension wiremock = WireMockExtension.newInstance().
            options(wireMockConfig().
                    port(9876).
                    extensions(new ContentTypeMatcher())
            ).build();

    @BeforeEach
    public void createRequestSpec() {

        requestSpec = new RequestSpecBuilder().
                setBaseUri("http://localhost").
                setPort(9876).
                build();
    }

    public void stubForContentTypeMatching() {

        wiremock.stubFor(requestMatching(
                "content-type-matcher",
                Parameters.one("Content-Type", "application/json")
        )
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("Content-Type is JSON")
                ));
    }

    @Test
    public void callWireMockWithJsonRequest_checkStatusCodeEquals200() {

        stubForContentTypeMatching();

        given().
                spec(requestSpec).
        and().
                contentType(ContentType.JSON).
        when().
                get("/content-type-matching").
        then().
                assertThat().
                statusCode(200).
        and().
                body(org.hamcrest.Matchers.equalTo("Content-Type is JSON"));
    }

    @Test
    public void callWireMockWithXmlRequest_checkStatusCodeEquals404() {

        stubForContentTypeMatching();

        given().
                spec(requestSpec).
        and().
                contentType(ContentType.XML).
        when().
                get("/content-type-matching").
        then().
                assertThat().
                statusCode(404);
    }
}
