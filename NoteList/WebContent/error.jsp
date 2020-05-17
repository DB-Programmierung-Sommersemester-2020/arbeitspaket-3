<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="css/notelist.css">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<title>Error</title>
</head>
<body>
<h1>Ein Fehler ist aufgetreten</h1>

<c:out value="${errorMessage}"/>

<a href=".">Zurück zur Startseite</a>
</body>
</html>