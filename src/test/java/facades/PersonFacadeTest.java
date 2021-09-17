/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.PersonDTO;
import dtos.PersonsDTO;
import entities.Person;
import utils.EMF_Creator;
import entities.RenameMe;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.hasItems;
import org.junit.jupiter.api.Assertions;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;
    private static Person p1,p2,p3,p4;

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
       emf = EMF_Creator.createEntityManagerFactoryForTest();
       facade = PersonFacade.getPersonFacade(emf);
    }


    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        p1 = new Person("fName1", "lName1", "phone1"); 
        p2 = new Person("fName2", "lName2", "phone2"); 
        p3 = new Person("fName3", "lName3", "phone3"); 
        p4 = new Person("fName4", "lName4", "phone4"); 
        EntityManager em = emf.createEntityManager();
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

   @Test
    public void testGetPerson() throws Exception {
        PersonDTO actPerson = facade.getPerson(p3.getId());
        assertEquals(actPerson.getId(),p3.getId());
    }

    @Test
    public void testGetAllPersons() throws Exception {
        PersonsDTO actPersons = facade.getAllPersons();
        assertEquals(actPersons.getAll().size(),4);
    }
    
    @Test
    public void testAddPerson() throws Exception {
        Person newP = new Person("Test","Add","Person");
        PersonDTO pDTO = facade.addPerson(newP.getFirstName(), newP.getLastName(), newP.getPhone());
        Assertions.assertNotNull(pDTO.getId());
    }
    
    @Test
    public void testEditPerson() throws Exception {
        PersonDTO actPDto = new PersonDTO(p3);
        PersonDTO newPDto = new PersonDTO("Updated fn","Updated ln","phone3");
        newPDto.setId(p3.getId());
        
        actPDto = facade.editPerson(newPDto);
        assertEquals(actPDto, newPDto);
    }
    
    @Test
    public void testDeletePerson() throws Exception {
       facade.deletePerson(p1.getId());
       EntityManager em = emf.createEntityManager();
       em.getTransaction().begin();
            Person p = em.find(Person.class, p1.getId());
       em.getTransaction().commit();
       em.close();
       Assertions.assertNull(p);
    }

}