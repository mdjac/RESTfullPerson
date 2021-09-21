/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.PersonDTO;
import errorhandling.MissingFieldsException;
import errorhandling.PersonNotFoundException;
import utils.EMF_Creator;
import facades.FacadeExample;
import facades.PersonFacade;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//Todo Remove or change relevant parts before ACTUAL use
@Path("person")
public class PersonResource {

    private final EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
       
    private final PersonFacade facade =  PersonFacade.getPersonFacade(emf);
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
          
  
    @GET
    @Produces({MediaType.APPLICATION_JSON}) 
    public Response getAllPersons() {
        return Response.ok(gson.toJson(facade.getAllPersons()), MediaType.APPLICATION_JSON).build();
    }
    
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonById(@PathParam("id") int id) throws PersonNotFoundException {
        PersonDTO pdto = facade.getPerson(id);
        return Response.ok(gson.toJson(pdto), MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPerson (String person) throws MissingFieldsException{
        PersonDTO pDTO = gson.fromJson(person, PersonDTO.class);
        pDTO = facade.addPerson(pDTO.getfName(), pDTO.getlName(), pDTO.getPhone());
        return Response.ok(gson.toJson(pDTO), MediaType.APPLICATION_JSON).build();
    }
    
    @Path("{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePerson (@PathParam("id")int id, String person) throws MissingFieldsException{
        PersonDTO pDTO = gson.fromJson(person, PersonDTO.class);
        pDTO.setId(id);
        pDTO = facade.editPerson(pDTO);
        return Response.ok(gson.toJson(pDTO), MediaType.APPLICATION_JSON).build();
    }
    
    @Path("{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePerson (@PathParam("id")int id) throws PersonNotFoundException{
        PersonDTO pDto = facade.deletePerson(id);
        return Response.ok("{\"status\" : \"removed id:"+pDto.getId()+"\"}", MediaType.APPLICATION_JSON).build();
    }
   
}
