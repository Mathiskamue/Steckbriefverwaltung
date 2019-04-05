<%-- 
    Document   : steckbrief_edit
    Created on : 04.04.2019, 11:36:27
    Author     : DEETMUMI
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    
    <jsp:attribute name="title">
        <c:choose>
            <c:when test="${edit}">
                Steckbrief bearbeiten
            </c:when>
            <c:otherwise>
                Steckbrief anlegen
            </c:otherwise>
        </c:choose>
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/task_edit.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/dashboard/"/>">Dashboard</a>
        </div>
        
        <div class="menuitem">
            <a href="<c:url value="/app/steckbrief/list/"/>">Liste</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <form method="post" class="stacked">
            <div class="column">
                <%-- CSRF-Token --%>
                <input type="hidden" name="csrf_token" value="${csrf_token}">

                <%-- Eingabefelder --%>
                <label for="steckbrief_owner">Eigentümer:</label>
                <div class="side-by-side">
                    <input type="text" name="steckbrief_owner" value="${task_form.values["steckbrief_owner"][0]}" readonly="readonly">
                </div>

                <label for="steckbrief_category">Kategorie:</label>
                <div class="side-by-side">
                    <select name="steckbrief_category">
                        <option value="">Keine Kategorie</option>

                        <c:forEach items="${categories}" var="category">
                            <option value="${category.id}" ${task_form.values["steckbrief_category"][0] == category.id.toString() ? 'selected' : ''}>
                                <c:out value="${category.name}" />
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <label for="steckbrief_due_date">
                    Fällig am:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <input type="text" name="steckbrief_due_date" value="${task_form.values["task_due_date"][0]}">
                </div>

                <label for="steckbrief_status">
                    Status:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side margin">
                    <select name="steckbrief_status">
                        <c:forEach items="${statuses}" var="status">
                            <option value="${status}" ${steckbrief_form.values["steckbrief_status"][0] == status ? 'selected' : ''}>
                                <c:out value="${status.label}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <label for="steckbrief_name">
                    Bezeichnung:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <input type="text" name="steckbrief_name" value="${steckbrief_form.values["steckbrief_name"][0]}">
                </div>
                
                <label for="steckbrief_name">
                    Kopfgeld:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <%--Variable ändern --%>
                    <input type="text" name="steckbrief_name" value="${steckbrief_form.values["steckbrief_name"][0]}">
                </div>

                <label for="steckbrief_personen_beschreibung">
                   Personenbeschreibung:
                </label>
                <div class="side-by-side">
                    <textarea name="steckbrief_personen_beschreibung"><c:out value="${steckbrief_form.values['steckbrief_personen_beschreibung'][0]}"/></textarea>
                </div>

                <%-- Button zum Abschicken --%>
                <div class="side-by-side">
                    <button class="icon-pencil" type="submit" name="action" value="save">
                        Sichern
                    </button>

                    <c:if test="${edit}">
                        <button class="icon-trash" type="submit" name="action" value="delete">
                            Löschen
                        </button>
                    </c:if>
                </div>
            </div>

            <%-- Fehlermeldungen --%>
            <c:if test="${!empty steckbrief_form.errors}">
                <ul class="errors">
                    <c:forEach items="${steckbrief_form.errors}" var="error">
                        <li>${error}</li>
                    </c:forEach>
                </ul>
            </c:if>
        </form>
    </jsp:attribute>
</template:base>
