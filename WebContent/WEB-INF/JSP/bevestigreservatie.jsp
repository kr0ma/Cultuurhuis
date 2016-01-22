<%@page contentType="text/html" pageEncoding="UTF-8" session="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri='http://vdab.be/tags' prefix='vdab'%>
<!doctype html>
<html>
<head>
<vdab:head title="Cultuurhuis > bevestig reservatie"></vdab:head>
</head>
<body>
	<vdab:header title="bevestiging reservaties"></vdab:header>
	<div>
		<h2>Stap 1: Wie ben je ?</h2>
		<form method="post">
			<label>Gebruikersnaam:
				<input name='gebruikersnaam' autofocus value='${not empty klant ? klant.gebruikersnaam : param.gebruikersnaam}' <c:if test="${not empty klant}">disabled="disabled"</c:if>>
			</label>
			<label>Paswoord:
				<input type="password" name='paswoord' value='${param.paswoord}' <c:if test="${not empty klant}">disabled="disabled"</c:if>>
					
			</label>
			<input type='submit' value='Zoek me op' name="zoekmeop" <c:if test="${not empty klant}">disabled="disabled"</c:if>>
			<span class="usermessage">${not empty fout? fout: klant}</span>
		</form>
		<form action="<c:url value='/nieuweklant.htm'/>">	
			<input type='submit' value='Ik ben nieuw' <c:if test="${not empty klant}">disabled="disabled"</c:if>>
		</form>		
	</div>
	<div>
		<h2>Stap 2:Bevestigen</h2>
		<form method="post">
			<input type='submit' value='Bevestigen' name="bevestigen" <c:if test="${empty klant}">disabled="disabled"</c:if>>
		</form>
	</div>
</body>
</html>