<%-- 
    Document   : steckbrief_rest
    Created on : 05.04.2019, 21:56:04
    Author     : DEETMUMI
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        Rest
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/rest.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/dashboard/"/>">Dashboard</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <form method="post" class="stacked">
              <input type="hidden" name="csrf_token" value="${csrf_token}">
              
        <table class="bb">
        <tr>
                <td>
                    Alle Datensätze lesen
                </td>
                <td class="verb">
                    GET
                </td>
                <td>
                    <a href="http://localhost:8080/DeadOrAlive/api/Steckbrief/">Steckbrief/</a>
                </td>
            </tr>
        </table>
              <br>
        <table class="bb">
            
             <tr>
                <td>
                    Datensatz mit Id 99 auslesen
                </td>
                <td class="verb">
                    GET
                </td>
                <td>
                    <a href="http://localhost:8080/DeadOrAlive/api/Steckbrief/99">Steckbrief/99</a>
                </td>
                
            </tr>
            <br>
             <tr>
                <td>
                    Datensatz mit gewünschter Id auslesen
                </td>
                <td class="verb">
                    GET
                </td>
                <td>
                    <input name="id">
                    <button name="action" value="getId"> GET </button>
                </td>
            </tr>
           </table>
              <br>
        <table class="bb">
             <tr>
                <td>
                    Datensatz Terminname/bezeichnung mit Inhalt m auslesen
                </td>
                <td class="verb">
                    GET
                </td>
                <td>
                    <a href="http://localhost:8080/DeadOrAlive/api/Steckbrief/?query=m">Steckbrief/?query=m</a>
                </td>
            </tr>
               <br>
             <tr>
                <td>
                    Datensatz mit gewünschter Beschreibung auslesen
                </td>
                <td class="verb">
                    GET
                </td>
                <td>
                    <input name="text">
                    <button name="action" value="getText"> GET </button>
                </td>
            </tr>
        
              
            </table>
            </form>
    </jsp:attribute>
</template:base>
