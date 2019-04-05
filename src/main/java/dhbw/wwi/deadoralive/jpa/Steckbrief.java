/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbw.wwi.deadoralive.jpa;

import dhbwka.wwi.vertsys.javaee.jtodo.common.jpa.User;
import dhbwka.wwi.vertsys.javaee.jtodo.tasks.jpa.Category;
import dhbwka.wwi.vertsys.javaee.jtodo.tasks.jpa.TaskStatus;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author DEETMUMI
 */
@Entity
public class Steckbrief implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    @NotNull(message = "Die Aufgabe muss einem Benutzer geordnet werden.")
    private User owner;
    
    @ManyToOne
    private Category category;
    
    @Column(length = 50)
    @NotNull(message = "Der Name darf nicht leer sein!!!!")
    @Size(min = 1, max = 50, message = "Der Name muss zwischen ein und 50 Zeichen lang sein.")
    private String name;
    private int kopfgeld;
    @Lob
    @NotNull
    private String personenBeschreibung;
    @NotNull(message = "Das Datum darf nicht leer sein.")
    private Date dueDate;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private TaskStatus status = TaskStatus.OPEN;
    
    
    public Steckbrief(){
        
    }
    public Steckbrief(User owner, Category category, String name,int kopfgeld, String personenBeschreibung, Date dueDate){
        this.owner = owner;
        this.category = category;
        this.name = name;
        this.kopfgeld = kopfgeld;
        this.personenBeschreibung = personenBeschreibung;
        this.dueDate = dueDate;
        
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKopfgeld() {
        return kopfgeld;
    }

    public void setKopfgeld(int kopfgeld) {
        this.kopfgeld = kopfgeld;
    }

    public String getPersonenBeschreibung() {
        return personenBeschreibung;
    }

    public void setPersonenBeschreibung(String personenBeschreibung) {
        this.personenBeschreibung = personenBeschreibung;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    
    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Steckbrief)) {
            return false;
        }
        Steckbrief other = (Steckbrief) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhbw.wwi.deadoralive.jpa.Steckbrief[ id=" + id + " ]";
    }
    
}
