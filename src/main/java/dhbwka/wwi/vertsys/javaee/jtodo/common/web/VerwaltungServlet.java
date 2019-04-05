/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.jtodo.common.web;

import dhbw.wwi.deadoralive.ejb.MaintainBean;
import dhbwka.wwi.vertsys.javaee.jtodo.common.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.jtodo.common.ejb.ValidationBean;
import dhbwka.wwi.vertsys.javaee.jtodo.common.jpa.User;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.persistence.Entity;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

/**
 *
 * @author thoma
 */
@WebServlet(urlPatterns = {"/app/maintain/"})
public class VerwaltungServlet extends HttpServlet {

    @EJB
    UserBean userBean;
    @EJB
    MaintainBean maintainBean;
    @EJB
    ValidationBean validationBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = new User();

        User userName = this.userBean.getCurrentUser();
        String username = userName.getUsername();
        User testuser = this.maintainBean.benutzerDaten(username);

        String vorname = testuser.getVorname();
        String nachname = testuser.getNachname();
   

        request.setAttribute("username", username);
        request.setAttribute("vorname", vorname);
        request.setAttribute("nachname", nachname);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/benutzer/benutzer.jsp");
        dispatcher.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        // Formulareingaben auslesen        
        String username = request.getParameter("maintain_username");
        String vorname = request.getParameter("maintain_vorname");
        String nachname = request.getParameter("maintain_nachname");
        String oldPassword = request.getParameter("maintain_oldPassword");
        String password1 = request.getParameter("maintain_password1");
        String password2 = request.getParameter("maintain_password2");
        
       
        // Eingaben prüfen
        User user = new User(username, oldPassword, vorname , nachname);
        List<String> errors = this.validationBean.validate(user);
        this.validationBean.validate(user.getPassword(), errors);
        
         User currentUser = this.userBean.getCurrentUser();
        
        if (password1 != null && password2 != null && !password1.equals(password2)) {
            errors.add("Die beiden Passwörter stimmen nicht überein.");
        }
        
        if(vorname != null && !"".equals(vorname)){
            currentUser.setVorname(vorname);
        }
        if(nachname != null && !"".equals(nachname)){
            currentUser.setNachname(nachname);
        }
        if(password1 != null && !"".equals(password1)){
            currentUser.setPassword(password1);
        }
        
        
        if (errors.isEmpty()) {
            // Keine Fehler: Startseite aufrufen
            //request.login(username, password1);
            this.userBean.update(currentUser);
            response.sendRedirect(WebUtils.appUrl(request, "/app/dashboard/"));
        } else {
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);
            
            HttpSession session = request.getSession();
            session.setAttribute("signup_form", formValues);
            
            response.sendRedirect(request.getRequestURI());
        }
    }
        
    /*User userUpdate = new User();
    
    String username = request.getParameter("maintain_username");
    String vorname = request.getParameter("maintain_vorname");
    String nachname = request.getParameter("maintain_nachname");
    String oldPassword = request.getParameter("maintain_oldPassword");
    String newPassword = request.getParameter("maintain_newPassword");
    
    System.out.println(oldPassword);
    System.out.println(newPassword);
    
    try {
    this.userBean.changePassword(userUpdate, oldPassword, newPassword);
    } catch (UserBean.InvalidCredentialsException ex) {
    ex.getMessage();
    }
    
    userUpdate.setUsername(username);
    userUpdate.setVorname(vorname);
    userUpdate.setNachname(nachname);
    
    this.userBean.update(userUpdate);
    
    response.sendRedirect(WebUtils.appUrl(request, "/app/dashboard/"));*/
    }


