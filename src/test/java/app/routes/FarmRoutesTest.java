package app.routes;

import app.config.ApplicationConfig;
import app.config.HibernateTestConfig;
import app.dtos.FarmDTO;
import app.entities.Farm;
import app.testutils.TestPopulator;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class FarmRoutesTest {
    private static final EntityManagerFactory emf = HibernateTestConfig.getEntityManagerFactory();
    private static Javalin app;
    private Map<String, Farm> farms;

    @BeforeAll
    static void setUpAll() {
        app = ApplicationConfig.startServer(7777, emf);
        RestAssured.baseURI = "http://localhost:7777/api";
    }

    @AfterAll
    static void tearDownAll() {
        ApplicationConfig.stopServer(app);
    }

    @BeforeEach
    void setUp() {
        farms = TestPopulator.populate(emf);
    }

    @Test
    @DisplayName("Server is running")
    void serverIsRunning() {
        given()
                .when()
                .get("/health")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Get /farms returns all farms")
    void getAllFarms() {
        given()
                .when()
                .get("/farms")
                .then()
                .body("size()", is(2))
                .statusCode(200);
    }

    @Test
    @DisplayName("Get farm by id")
    void getFarmById() {
        Long id = farms.get("gamlevaelde").getId();
        given()
                .when()
                .get("/farms/" + id)
                .then()
                .statusCode(200)
                .body("name", is("Gamlevælde"))
                .body("id", is(id.intValue()));
    }

    @Test
    @DisplayName("Farm not found")
    void getFarmNotFound() {
        given()
                .when()
                .get("/farms/999")
                .then()
                .statusCode(404)
                .body("status", is(404))
                .body("msg", containsString("No farm"));
    }

    @Test
    @DisplayName("Farm has invalid id")
    void getFarmWithInvalidId() {
        given()
                .when()
                .get("/farms/abc")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Create a farm")
    void createFarm() {
        given()
                .contentType(ContentType.JSON)
                .body(new FarmDTO(null, "Rosmanne", null, null))
                .when()
                .post("/farms")
                .then()
                .statusCode(201)
                .body("name", is("Rosmanne"))
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Farm has no name")
    void createFarmWithoutName() {
        given()
                .contentType(ContentType.JSON)
                .body(new FarmDTO(null, null, null, null))
                .when()
                .post("/farms")
                .then()
                .statusCode(400)
                .body("msg", is("Farm name is required"));
    }

    @Test
    @DisplayName("Missing coordinate data")
    void createFarmWithOnlyLatitude() {
        given()
                .contentType(ContentType.JSON)
                .body(new FarmDTO(null, "Buggemøllen", 55.0, null))
                .when()
                .post("/farms")
                .then()
                .statusCode(400)
                .body("msg", is("Send both latitude and longitude, or neither"));
    }

    @Test
    @DisplayName("Update a farm")
    void updateFarm() {
        Long id = farms.get("buggemoellen").getId();
        given()
                .contentType(ContentType.JSON)
                .body(new FarmDTO(null, "Buggemøllen Syd", 55.0, 14.8))
                .when()
                .put("/farms/" + id)
                .then()
                .statusCode(200)
                .body("name", is("Buggemøllen Syd"))
                .body("latitude", is(55.0f));
    }

    @Test
    @DisplayName("Update farm not found")
    void updateFarmNotFound() {
        given()
                .contentType(ContentType.JSON)
                .body(new FarmDTO(null, "Rosmanne", null, null))
                .when()
                .put("/farms/999")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Delete a farm")
    void deleteFarm() {
        Long id = farms.get("gamlevaelde").getId();
        given()
                .when()
                .delete("/farms/" + id)
                .then()
                .statusCode(204);
        given()
                .when()
                .get("/farms/" + id)
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Delete farm not found")
    void deleteFarmNotFound() {
        given()
                .when()
                .delete("/farms/999")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Get the sections on a farm")
    void getSectionsOnFarm() {
        Long id = farms.get("gamlevaelde").getId();
        given()
                .when()
                .get("/farms/" + id + "/sections")
                .then()
                .statusCode(200)
                .body("size()", is(3))
                .body("name", hasItems("MATING", "GESTATION", "FARROWING"));
    }

    @Test
    @DisplayName("Weather for a farm without coordinates")
    void weatherWithoutCoordinates() {
        Long id = farms.get("buggemoellen").getId();
        given()
                .when()
                .get("/farms/" + id + "/weather")
                .then()
                .statusCode(409)
                .body("msg", containsString("has no coordinates"));
    }

    @Test
    @DisplayName("Endpoint does not exist")
    void unknownEndpoint() {
        given()
                .when()
                .get("/cows")
                .then()
                .statusCode(404)
                .body("msg", is("There is no endpoint GET /api/cows"));
    }
}
