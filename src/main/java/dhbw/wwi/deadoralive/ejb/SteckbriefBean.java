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

import dhbw.wwi.deadoralive.jpa.Steckbrief;
import dhbwka.wwi.vertsys.javaee.jtodo.common.ejb.EntityBean;
import dhbwka.wwi.vertsys.javaee.jtodo.tasks.jpa.Category;
import dhbwka.wwi.vertsys.javaee.jtodo.tasks.jpa.Task;
import dhbwka.wwi.vertsys.javaee.jtodo.tasks.jpa.TaskStatus;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author DEETMUMI
 */
@Stateless
public class SteckbriefBean extends EntityBean<Steckbrief, Long> {
    
    public SteckbriefBean() {
        super(Steckbrief.class);
    }
   
 
   
    
     public List<Steckbrief> findByQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            query = "";
        }
        
        query = "%" + query + "%";

        return em.createQuery("SELECT s.name, s.category, s.kopfgeld, s.personenBeschreibung, s.status FROM Steckbrief s"
                            + "    WHERE s.name       LIKE :query")
                            
                .setParameter("query", query)
                .getResultList();
}
    
    /**
     *
     * @param search
     * @param category
     * @param status
     * @return
     */
    public List<Steckbrief> search(String search, Category category, TaskStatus status) {
        // Hilfsobjekt zum Bauen des Query
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        
        // SELECT t FROM Task t
        CriteriaQuery<Steckbrief> query = cb.createQuery(Steckbrief.class);
        Root<Steckbrief> from = query.from(Steckbrief.class);
        query.select(from);

        // ORDER BY dueDate, dueTime
        query.orderBy(cb.asc(from.get("dueDate")));
        
        // WHERE t.shortText LIKE :search
        Predicate p = cb.conjunction();
        
        if (search != null && !search.trim().isEmpty()) {
            p = cb.and(p, cb.like(from.get("name"), "%" + search + "%"));
            query.where(p);
        }
        
        // WHERE t.category = :category
        if (category != null) {
            p = cb.and(p, cb.equal(from.get("category"), category));
            query.where(p);
        }
        
        // WHERE t.status = :status
        if (status != null) {
            p = cb.and(p, cb.equal(from.get("status"), status));
            query.where(p);
        }
        
        return em.createQuery(query).getResultList();
    }
    
    
}
