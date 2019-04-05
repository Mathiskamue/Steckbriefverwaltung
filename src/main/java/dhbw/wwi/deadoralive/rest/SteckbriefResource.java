/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbw.wwi.deadoralive.rest;

import dhbw.wwi.deadoralive.ejb.SteckbriefBean;
import dhbw.wwi.deadoralive.jpa.Steckbrief;
import java.util.List;
import javax.ejb.EJB;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author DEETMUMI
 */
@Path("Steckbrief")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SteckbriefResource {
    
    @EJB
    private SteckbriefBean steckbriefBean; 
    
    @GET
    public List<Steckbrief> findSteckbriefe() {
        return this.steckbriefBean.findAll();
    }
    
    @POST
    public Steckbrief saveNewSteckbrief(@Valid Steckbrief steckbrief) {
        return this.steckbriefBean.saveNew(steckbrief);
    }
    
    @GET
    @Path("{id}")
    public Steckbrief getSteckbrief(@PathParam("id")long id){
        return this.steckbriefBean.findById(id);
    }
   
    @PUT
    @Path("{id}")
    public Steckbrief updateSteckbrief(@PathParam("id") long id, @Valid Steckbrief steckbrief) {
        steckbrief.setId(id);
        return this.steckbriefBean.update(steckbrief);
    }
    
    @DELETE
    @Path("{id}")
    public Steckbrief deleteSteckbrief(@PathParam("id") long id) {
        Steckbrief steckbrief = this.steckbriefBean.findById(id);

        if (steckbrief != null) {
            this.steckbriefBean.delete(steckbrief);
        }
        return steckbrief;
    }
}
    
    

