/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */

import dhbw.wwi.deadoralive.ejb.SteckbriefBean;
import dhbw.wwi.deadoralive.jpa.Steckbrief;
import dhbwka.wwi.vertsys.javaee.jtodo.common.web.WebUtils;
import dhbwka.wwi.vertsys.javaee.jtodo.common.web.FormValues;
import dhbwka.wwi.vertsys.javaee.jtodo.tasks.ejb.CategoryBean;
import dhbwka.wwi.vertsys.javaee.jtodo.tasks.ejb.TaskBean;
import dhbwka.wwi.vertsys.javaee.jtodo.common.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.jtodo.common.ejb.ValidationBean;
import dhbwka.wwi.vertsys.javaee.jtodo.tasks.jpa.Task;
import dhbwka.wwi.vertsys.javaee.jtodo.tasks.jpa.TaskStatus;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Seite zum Anlegen oder Bearbeiten einer Aufgabe.
 */
@WebServlet(urlPatterns = "/app/steckbrief/steckbrief/*")
public class SteckbriefEditServlet extends HttpServlet {

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

        // Verfügbare Kategorien und Stati für die Suchfelder ermitteln
        request.setAttribute("categories", this.categoryBean.findAllSorted());
        request.setAttribute("statuses", TaskStatus.values());

        // Zu bearbeitende Aufgabe einlesen
        HttpSession session = request.getSession();

        Steckbrief steckbrief = this.getRequestedSteckbrief(request);

        if (steckbrief == null) {
            steckbrief = new Steckbrief();
        }

        request.setAttribute("edit", steckbrief.getId() != null);

        if (session.getAttribute("steckbrief_form") == null) {
            // Keine Formulardaten mit fehlerhaften Daten in der Session,
            // daher Formulardaten aus dem Datenbankobjekt übernehmen
            request.setAttribute("steckbrief_form", this.createSteckbriefForm(steckbrief));
        }

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/steckbrief/steckbrief_edit.jsp").forward(request, response);

        session.removeAttribute("steckbrief_form");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Angeforderte Aktion ausführen
        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }

        switch (action) {
            case "save":
                this.saveSteckbrief(request, response);
                break;
            case "delete":
                this.deleteSteckbrief(request, response);
                break;
        }
    }

    /**
     * Aufgerufen in doPost(): Neue oder vorhandene Aufgabe speichern
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void saveSteckbrief(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        List<String> errors = new ArrayList<>();

        String steckbriefCategory = request.getParameter("steckbrief_category");
        String steckbriefDueDate = request.getParameter("steckbrief_due_date");
        String steckbriefStatus = request.getParameter("steckbrief_status");
        String steckbriefName = request.getParameter("steckbrief_name");
        String steckbriefKopfgeld = request.getParameter("steckbrief_kopfgeld");
        String steckbriefPersonenBeschreibung = request.getParameter("steckbrief_personen_beschreibung");

        Steckbrief steckbrief = this.getRequestedSteckbrief(request);

        if (steckbriefCategory != null && !steckbriefCategory.trim().isEmpty()) {
            try {
                steckbrief.setCategory(this.categoryBean.findById(Long.parseLong(steckbriefCategory)));
            } catch (NumberFormatException ex) {
                // Ungültige oder keine ID mitgegeben
            }
        }

        Date dueDate = WebUtils.parseDate(steckbriefDueDate);

        if (dueDate != null) {
            steckbrief.setDueDate(dueDate);
        } else {
            errors.add("Das Datum muss dem Format dd.mm.yyyy entsprechen.");
        }

        try {
            steckbrief.setStatus(TaskStatus.valueOf(steckbriefStatus));
        } catch (IllegalArgumentException ex) {
            errors.add("Der ausgewählte Status ist nicht vorhanden.");
        }

        steckbrief.setPersonenBeschreibung(steckbriefName);
        steckbrief.setPersonenBeschreibung(steckbriefPersonenBeschreibung);

        this.validationBean.validate(steckbrief, errors);

        // Datensatz speichern
        if (errors.isEmpty()) {
            this.steckbriefBean.update(steckbrief);
        }

        // Weiter zur nächsten Seite
        if (errors.isEmpty()) {
            // Keine Fehler: Startseite aufrufen
            response.sendRedirect(WebUtils.appUrl(request, "/app/steckbrief/list/"));
        } else {
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("steckbrief_form", formValues);

            response.sendRedirect(request.getRequestURI());
        }
    }

    /**
     * Aufgerufen in doPost: Vorhandene Aufgabe löschen
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void deleteSteckbrief(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Datensatz löschen
        Steckbrief steckbrief = this.getRequestedSteckbrief(request);
        this.steckbriefBean.delete(steckbrief);

        // Zurück zur Übersicht
        response.sendRedirect(WebUtils.appUrl(request, "/app/steckbrief/list/"));
    }

    /**
     * Zu bearbeitende Aufgabe aus der URL ermitteln und zurückgeben. Gibt
     * entweder einen vorhandenen Datensatz oder ein neues, leeres Objekt
     * zurück.
     *
     * @param request HTTP-Anfrage
     * @return Zu bearbeitende Aufgabe
     */
    private Steckbrief getRequestedSteckbrief(HttpServletRequest request) {
        // Zunächst davon ausgehen, dass ein neuer Satz angelegt werden soll
        Steckbrief steckbrief = new Steckbrief();
        steckbrief.setOwner(this.userBean.getCurrentUser());
        steckbrief.setDueDate(new Date(System.currentTimeMillis()));

        // ID aus der URL herausschneiden
        String steckbriefId = request.getPathInfo();

        if (steckbriefId == null) {
            steckbriefId = "";
        }

        steckbriefId = steckbriefId.substring(1);

        if (steckbriefId.endsWith("/")) {
            steckbriefId = steckbriefId.substring(0, steckbriefId.length() - 1);
        }

        // Versuchen, den Datensatz mit der übergebenen ID zu finden
        try {
            steckbrief = this.steckbriefBean.findById(Long.parseLong(steckbriefId));
        } catch (NumberFormatException ex) {
            // Ungültige oder keine ID in der URL enthalten
        }

        return steckbrief;
    }

    /**
     * Neues FormValues-Objekt erzeugen und mit den Daten eines aus der
     * Datenbank eingelesenen Datensatzes füllen. Dadurch müssen in der JSP
     * keine hässlichen Fallunterscheidungen gemacht werden, ob die Werte im
     * Formular aus der Entity oder aus einer vorherigen Formulareingabe
     * stammen.
     *
     * @param task Die zu bearbeitende Aufgabe
     * @return Neues, gefülltes FormValues-Objekt
     */
    private FormValues createSteckbriefForm(Steckbrief steckbrief) {
        Map<String, String[]> values = new HashMap<>();

        values.put("steckbrief_owner", new String[]{
            steckbrief.getOwner().getUsername()
        });

        if (steckbrief.getCategory() != null) {
            values.put("steckbrief_category", new String[]{
                "" + steckbrief.getCategory().getId()
            });
        }

        values.put("steckbrief_due_date", new String[]{
            WebUtils.formatDate(steckbrief.getDueDate())
        });

        values.put("steckbrief_status", new String[]{
            steckbrief.getStatus().toString()
        });

        values.put("steckbrief_name", new String[]{
            steckbrief.getName()
        });

        values.put("steckbrief_personen_beschreibung", new String[]{
            steckbrief.getPersonenBeschreibung()
        });

        FormValues formValues = new FormValues();
        formValues.setValues(values);
        return formValues;
    }

}
