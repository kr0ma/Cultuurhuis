<%@page contentType="text/html" pageEncoding="UTF-8" session="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix='fmt' uri='http://java.sun.com/jsp/jstl/fmt'%>
<%@taglib uri='http://vdab.be/tags' prefix='vdab'%>
<!doctype html>
<html>
<head>
<vdab:head title="Cultuurhuis > voorstellingen"></vdab:head>
</head>
<body>
	<vdab:header title="voorstellingen"></vdab:header>
	<div>
		<h2>Genres</h2>
		<c:forEach var="genre" items="${genres}">
			<c:url value='' var='genreURL'>
				<c:param name='genreID' value="${genre.id}" />
			</c:url>
			<a href="<c:out value='${genreURL}'/>">${genre.naam}</a>
		</c:forEach>
	</div>
	<c:if test="${not empty voorstellingen}">
		<div>
		<h2>${huidigGenre.naam} voorstellingen</h2>
		<table>
			<tr>
				<th>Datum</th>
				<th>Titel</th>
				<th>Uitvoerders</th>
				<th>Prijs</th>
				<th>Vrije plaatsen</th>
				<th>Reserveren</th>
			</tr>
			<c:forEach var="voorstelling" items="${voorstellingen}">
				<tr>
					<td><fmt:formatDate value='${voorstelling.datum}' type='both' dateStyle='short' timeStyle='short'/></td>
					<td>${voorstelling.titel}</td>
					<td>${voorstelling.uitvoerders}</td>
					<td class="number">€<fmt:formatNumber value='${voorstelling.prijs}' minFractionDigits='2'/> </td>
					<td class="number">${voorstelling.vrijeplaatsen}</td>
					<c:url value='/voorstelling/reserveren.htm' var='reserverenURL'>
						<c:param name='voorstellingID' value="${voorstelling.id}" />
					</c:url>
					<c:choose>
						<c:when test="${voorstelling.vrijeplaatsen > 0}">
							<td><a href="<c:out value='${reserverenURL}'/>">Reserveren</a></td>
						</c:when>
						<c:otherwise>
							<td></td>
						</c:otherwise>
					</c:choose>					
				</tr>
			</c:forEach>
		</table>
		</div>
	</c:if>
</body>
</html>