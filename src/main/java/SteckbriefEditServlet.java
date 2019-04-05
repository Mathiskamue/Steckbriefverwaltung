/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */

import dhbw.wwi.deadoralive.ejb.SteckbriefBean;
import dhbw.wwi.deadoralive.jpa.Steckbrief;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author DEETMUMI
 */


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
@WebServlet(urlPatterns = "/app/tasks/task/*")
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
        request.setAttribute("edit", steckbrief.getId() != 0);
                                
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
                this.saveNewSteckbrief(request, response);
                break;
            case "delete":
                this.deleteTask(request, response);
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
    private void saveTask(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        List<String> errors = new ArrayList<>();

        String taskCategory = request.getParameter("task_category");
        String taskDueDate = request.getParameter("task_due_date");
        String taskDueTime = request.getParameter("task_due_time");
        String taskStatus = request.getParameter("task_status");
        String taskShortText = request.getParameter("task_short_text");
        String taskLongText = request.getParameter("task_long_text");

        Task task = this.getRequestedTask(request);

        if (taskCategory != null && !taskCategory.trim().isEmpty()) {
            try {
                task.setCategory(this.categoryBean.findById(Long.parseLong(taskCategory)));
            } catch (NumberFormatException ex) {
                // Ungültige oder keine ID mitgegeben
            }
        }

        Date dueDate = WebUtils.parseDate(taskDueDate);
        Time dueTime = WebUtils.parseTime(taskDueTime);

        if (dueDate != null) {
            task.setDueDate(dueDate);
        } else {
            errors.add("Das Datum muss dem Format dd.mm.yyyy entsprechen.");
        }

        if (dueTime != null) {
            task.setDueTime(dueTime);
        } else {
            errors.add("Die Uhrzeit muss dem Format hh:mm:ss entsprechen.");
        }

        try {
            task.setStatus(TaskStatus.valueOf(taskStatus));
        } catch (IllegalArgumentException ex) {
            errors.add("Der ausgewählte Status ist nicht vorhanden.");
        }

        task.setShortText(taskShortText);
        task.setLongText(taskLongText);

        this.validationBean.validate(task, errors);

        // Datensatz speichern
        if (errors.isEmpty()) {
            this.taskBean.update(task);
        }

        // Weiter zur nächsten Seite
        if (errors.isEmpty()) {
            // Keine Fehler: Startseite aufrufen
            response.sendRedirect(WebUtils.appUrl(request, "/app/tasks/list/"));
        } else {
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("task_form", formValues);

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
    private void deleteTask(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Datensatz löschen
        Task task = this.getRequestedTask(request);
        this.taskBean.delete(task);

        // Zurück zur Übersicht
        response.sendRedirect(WebUtils.appUrl(request, "/app/tasks/list/"));
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

        values.put("task_owner", new String[]{
            steckbrief.getOwner().getUsername()
        });

        if (steckbrief.getCategory() != null) {
            values.put("task_category", new String[]{
                "" + steckbrief.getCategory().getId()
            });
        }

        values.put("task_due_date", new String[]{
            WebUtils.formatDate(steckbrief.getDueDate())
        });


        values.put("task_status", new String[]{
            steckbrief.getStatus().toString()
        });

        values.put("task_short_text", new String[]{
            steckbrief.getName()
        });

        values.put("task_long_text", new String[]{
            steckbrief.getPersonenBeschreibung()
        });

        FormValues formValues = new FormValues();
        formValues.setValues(values);
        return formValues;
    }

}

