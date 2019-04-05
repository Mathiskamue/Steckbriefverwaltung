<%-- 
    Copyright © 2018 Dennis Schulmeister-Zimolong

    E-Mail: dhbw@windows3.de
    Webseite: https://www.wpvs.de/

    Dieser Quellcode ist lizenziert unter einer
    Creative Commons Namensnennung 4.0 International Lizenz.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        Liste der Aufgaben
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/task_list.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/dashboard/"/>">Dashboard</a>
        </div>

        <div class="menuitem">
            <a href="<c:url value="/app/steckbrief/steckbrief/new/"/>">Aufgabe anlegen</a>
        </div>

        <div class="menuitem">
            <a href="<c:url value="/app/steckbrief/categories/"/>">Kategorien bearbeiten</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <%-- Suchfilter --%>
        <form method="GET" class="horizontal" id="search">
            <input type="text" name="search_text" value="${param.search_text}" placeholder="Beschreibung"/>

            <select name="search_category">
                <option value="">Alle Kategorien</option>

                <c:forEach items="${categories}" var="category">
                    <option value="${category.id}" ${param.search_category == category.id ? 'selected' : ''}>
                        <c:out value="${category.name}" />
                    </option>
                </c:forEach>
            </select>

            <select name="search_status">
                <option value="">Alle Stati</option>

                <c:forEach items="${statuses}" var="status">
                    <option value="${status}" ${param.search_status == status ? 'selected' : ''}>
                        <c:out value="${status.label}"/>
                    </option>
                </c:forEach>
            </select>

            <button class="icon-search" type="submit">
                Suchen
            </button>
        </form>

        <%-- Gefundene Aufgaben --%>
        <c:choose>
            <c:when test="${empty steckbrief}">
                <p>
                    Es wurden keine Aufgaben gefunden. 🐈
                </p>
            </c:when>
            <c:otherwise>
                <jsp:useBean id="utils" class="dhbwka.wwi.vertsys.javaee.jtodo.common.web.WebUtils"/>
                
                <table class="tabelle">
                    <thead>
                        <tr>
                            <th>Bezeichnung</th>
                            <th>Kategorie</th>
                            <th>Eigentümer</th>
                            <th>Status</th>
                            <th>Fällig am</th>
                        </tr>
                    </thead>
                    <c:forEach items="${steckbrief}" var="steckbrief">
                        <tr>
                            <td>
                                <a href="<c:url value="/app/tasks/task/${steckbrief.id}/"/>">
                                    <c:out value="${steckbrief.name}"/>
                                </a>
                            </td>
                            <td>
                                <c:out value="${steckbrief.category.name}"/>
                            </td>
                            <td>
                                <c:out value="${steckbrief.owner.username}"/>
                            </td>
                            <td>
                                <c:out value="${steckbrief.status.label}"/>
                            </td>
                            <td>
                                <c:out value="${utils.formatDate(steckbrief.dueDate)}"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </jsp:attribute>
</template:base>