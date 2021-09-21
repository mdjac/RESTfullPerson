/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.PersonDTO;
import dtos.PersonsDTO;

/**
 *
 * @author mikke
 */
public interface IPersonFacade {
  public PersonDTO addPerson(String fName, String lName, String phone) throws Exception;  
  public PersonDTO deletePerson(int id) throws Exception;  
  public PersonDTO getPerson(int id) throws Exception;  
  public PersonsDTO getAllPersons();  
  public PersonDTO editPerson(PersonDTO p) throws Exception;  
}

