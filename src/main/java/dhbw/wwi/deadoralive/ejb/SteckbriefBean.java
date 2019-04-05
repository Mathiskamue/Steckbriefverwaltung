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
import javax.ejb.Stateless;

/**
 *
 * @author DEETMUMI
 */
@Stateless
public class SteckbriefBean extends EntityBean<Steckbrief, Long> {
    
    public SteckbriefBean() {
        super(Steckbrief.class);
    }
    
    
}
