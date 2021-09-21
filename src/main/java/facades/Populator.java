/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.RenameMeDTO;
import entities.Address;
import entities.Person;
import entities.RenameMe;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;



/**
 *
 * @author tha
 */
public class Populator {
    public static void populate(){
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        //FacadeExample fe = FacadeExample.getFacadeExample(emf);
        //fe.create(new RenameMeDTO(new RenameMe("First 1", "Last 1")));
        //fe.create(new RenameMeDTO(new RenameMe("First 2", "Last 2")));
        //fe.create(new RenameMeDTO(new RenameMe("First 3", "Last 3")));
        Address a1 = new Address("Testvej",1000,"Testby");
        
        Person p1 = new Person("test1", "test1", "test1");
        p1.setAddress(a1);
        Person p2 = new Person("test2", "test2", "test2");
        p2.setAddress(new Address("Testvej",2000,"Testby"));
        Person p3 = new Person("test3", "test3", "test3");
        p3.setAddress(a1);
        
        
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
        em.getTransaction().commit();
        em.close();

    }
    
    public static void main(String[] args) {
        populate();
    }
}
