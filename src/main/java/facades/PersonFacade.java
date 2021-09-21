/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.PersonDTO;
import dtos.PersonsDTO;
import entities.Address;
import entities.Person;
import errorhandling.MissingFieldsException;
import errorhandling.PersonNotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author mikke
 */
public class PersonFacade implements IPersonFacade{
    private static PersonFacade instance;
    private static EntityManagerFactory emf;
    
    //Private Constructor to ensure Singleton
    private PersonFacade() {}
    
    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    @Override
    public PersonDTO addPerson(PersonDTO pdto) throws MissingFieldsException{
        if(pdto.getfName() == null || pdto.getlName() == null || pdto.getPhone() == null){
            throw new MissingFieldsException("One or more fields is missing!");
        }
        Person p = new Person(pdto.getfName(),pdto.getlName(),pdto.getPhone());
        if(pdto.getCity() != null && pdto.getZip() != 0 && pdto.getStreet() != null){
            p.setAddress(new Address(pdto.getStreet(),pdto.getZip(),pdto.getCity()));
        }
        
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PersonDTO(p);
    }

    @Override
    public PersonDTO deletePerson(int id) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        Person person;
        try {
            em.getTransaction().begin();
                person = em.find(Person.class, id);
                if(person == null){
                    throw new PersonNotFoundException("Could not delete, provided id does not exist!");
                }
                em.remove(person);
                em.getTransaction().commit();
            return new PersonDTO(person);
        } finally {
            em.close();
        }
        
    }

    @Override
    public PersonDTO getPerson(int id) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        Person p = em.find(Person.class, id);
        if(p == null){
            throw new PersonNotFoundException("No person with provided id found");
        }else{
            return new PersonDTO(p);
        }
     
    }

    @Override
    public PersonsDTO getAllPersons() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
        List<Person> persons = query.getResultList();
        return new PersonsDTO(persons);
    }

    @Override
    public PersonDTO editPerson(PersonDTO p) throws MissingFieldsException {
        if(p.getfName() == null || p.getlName() == null || p.getPhone() == null){
            throw new MissingFieldsException("One or more fields is missing!");
        }
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
                Person tempPerson = em.find(Person.class, p.getId());
                tempPerson = tempPerson.updateFromDto(p);
            em.getTransaction().commit();
            return new PersonDTO(tempPerson);
        } finally {
            em.close();
        }
    }
    
    
}
