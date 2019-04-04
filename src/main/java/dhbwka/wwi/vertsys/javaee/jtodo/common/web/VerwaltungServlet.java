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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = new User();

        User userName = this.userBean.getCurrentUser();
        String username = userName.getUsername();
        User testuser = this.maintainBean.benutzerDaten(username);

        String vorname = testuser.getVorname();
        String nachname = testuser.getNachname();
        
        System.out.println(vorname);

        request.setAttribute("username", username);
        request.setAttribute("vorname", vorname);
        request.setAttribute("nachname", nachname);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/benutzer/benutzer.jsp");
        dispatcher.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User userUpdate = new User();

        String username = request.getParameter("username");
        String vorname = request.getParameter("vorname");
        String nachname = request.getParameter("nachname");
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        
        System.out.println(nachname);

        /*try {
        this.userBean.changePassword(userUpdate, oldPassword, newPassword);
        } catch (UserBean.InvalidCredentialsException ex) {
        ex.getMessage();
        }*/

        userUpdate.setUsername(username);
        userUpdate.setVorname(vorname);
        userUpdate.setNachname(nachname);

        this.userBean.update(userUpdate);

        response.sendRedirect(request.getRequestURI());
    }

}
