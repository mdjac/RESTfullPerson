/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

/**
 *
 * @author mikke
 */
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import dtos.PersonDTO;
import entities.Person;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test
//@Disabled

public class PersonResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Person p1, p2, p3, p4;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        p1 = new Person("fn1","ln1","phone1");
        p2 = new Person("fn2","ln2","phone2");
        p3 = new Person("fn3","ln3","phone3");
        p4 = new Person("fn4","ln4","phone4");
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.persist(p4);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    
    
    //@Test
    public void testWrongURL() {
        given().when().get("/person/url/fines/ikke").then().statusCode(500);
    }
    
    
    
    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/person").then().statusCode(200);
    }
    
    
    
    @Test
    public void testGetAllPersons() {
        given()
                .contentType("application/json")
                .get("/person").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("all", hasSize(4));
    }
    
    @Test
    public void testGetPersonById() {
        given()
                .contentType("application/json")
                .get("/person/"+p3.getId()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("fName", is(p3.getFirstName()));
    }
    
    
    @Test
    public void testAddPerson() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("fName", "testPOST");
        requestParams.put("lName", "test");
        requestParams.put("phone", "test");

        given()
                .contentType("application/json")
                .body(requestParams.toString())
                .when()
                .post("/person")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", greaterThan(0));
    }
    
    
    @Test
    public void testUpdatePerson() {
        int id = p2.getId();
        
        //JSONObject requestParams = new JSONObject();
        //requestParams.put("fName", "testPUT");
        //requestParams.put("lName", "testPUT");
        //requestParams.put("phone", "testPUT");

        PersonDTO pDTO = new PersonDTO("testPUT", "testlNamePUT", "phonenumber");
        given()
                .contentType("application/json")
                //.body(requestParams.toString())
                .body(pDTO)
                .when()
                .put("/person/"+id)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", is(id))
                .body("fName", is("testPUT"));
    }
    
 
    @Test
    public void testDeletePerson() {
        int id = p2.getId();
        
        given()
                .contentType("application/json")
                .delete("/person/"+id).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("status", is("removed id:"+id));
    }
    
    @Test
    public void testGetPersonByIdFail(){
        int id = 10000;
        given()
                .contentType("application/json")
                .get("/person/"+id).then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode());
    }
    
    @Test
    public void testDeletePersonFail(){
        int id = 10000;
        given()
                .contentType("application/json")
                .delete("/person/"+id).then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode());
    }
    
    //@Test
    public void testAddPersonFail() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("fName", "testPOST");
        requestParams.put("lName", "test");
        requestParams.put("phone", "test");

        given()
                .contentType("application/json")
                .body(requestParams.toString())
                .when()
                .post("/person")
                .then()
                .assertThat()
                .statusCode(400);
    }
 
    
}
