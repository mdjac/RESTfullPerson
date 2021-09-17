/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.RenameMeDTO;
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
        
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
            em.persist(new Person("test1", "test2", "test3"));
            em.persist(new Person("test4", "test5", "test6"));
        em.getTransaction().commit();
        em.close();

    }
    
    public static void main(String[] args) {
        populate();
    }
}
