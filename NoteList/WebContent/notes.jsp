<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<title>Notizen</title>
<link rel="stylesheet" href="css/notelist.css">
<script src="js/notelist.js"></script>
</head>
<body>
	<h2>
		Notizen von
		<c:out value="${User.username}" />
	</h2>

	<table>
		<tr>
			<td colspan="2" align="left"><h3>Notiz erfassen</h3></td>
		</tr>
		<tr>
			<td><label for="betreff"> Betreff: </label></td>
			<td><input type="text" id="subject"></td>
		</tr>
		<tr>
			<td><label for="text"> Text: </label></td>
			<td><textarea id="content" cols="35" rows="4"></textarea></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><button type="button" onclick="clearNotes()">Liste
					löschen</button>
				<button type="button" onclick="addNote()">Notiz hinzufügen</button>
				<a href="index.html">
					<button onclick="logout()">Beenden</button>
			</a></td>
		</tr>
	</table>
	<hr />
	<div id="notelist"></div>

</body>
</html>