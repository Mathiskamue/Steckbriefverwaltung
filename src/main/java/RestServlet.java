
import dhbw.wwi.deadoralive.ejb.SteckbriefBean;
import dhbwka.wwi.vertsys.javaee.jtodo.common.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.jtodo.common.ejb.ValidationBean;
import dhbwka.wwi.vertsys.javaee.jtodo.common.web.WebUtils;
import dhbwka.wwi.vertsys.javaee.jtodo.tasks.ejb.CategoryBean;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */

/**
 *
 * @author DEETMUMI
 */
/**
 * Seite zum Anlegen oder Bearbeiten einer Aufgabe.
 */
@WebServlet(urlPatterns = "/app/rest/")
public class RestServlet extends HttpServlet {

    @EJB
    SteckbriefBean steckbriefBean;

    @EJB
    CategoryBean categoryBean;

    @EJB
    UserBean userBean;

    @EJB
    ValidationBean validationBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
request.setCharacterEncoding("UTF-8");

        // Zu bearbeitende Aufgabe einlesen
        HttpSession session = request.getSession();

                                
        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/steckbrief/steckbrief_rest.jsp").forward(request, response);

    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        // Angeforderte Aktion ausführen
        String action = request.getParameter("action");
        String id = request.getParameter("id");
        String text = request.getParameter("text");
        String status = request.getParameter("status");
        
        
        String url =  "/api/Steckbrief/";
        
       if("getStatus".equals(action)){
           url = url + "?query=" + status;
       }
        if("getText".equals(action)){
           url = url + "?query=" +  text;
       }
         if("getId".equals(action)){
           url = url + id;
       }
         response.sendRedirect(WebUtils.appUrl(request, url));
}


}
