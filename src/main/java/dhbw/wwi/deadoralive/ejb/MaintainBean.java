/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbw.wwi.deadoralive.ejb;

import dhbwka.wwi.vertsys.javaee.jtodo.common.jpa.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author thoma
 */
@Stateless
public class MaintainBean {
    
    @PersistenceContext
    EntityManager em;
    
    
    /* public List<Entity> findAll(String username) {
    String select = "Select w From User w WHERE w.username = :username";
    return em.createQuery(select).getResultList();
    }*/
     
    /*   public User benutzerDaten(String username){
    return (User) em.createQuery("SELECT e FROM User e WHERE e.username = :username")
    .setParameter("username",username)
    .getResultList();
    
    }*/
      public User benutzerDaten(String username) {
        return em.find(User.class, username);
    }

    
    
}
