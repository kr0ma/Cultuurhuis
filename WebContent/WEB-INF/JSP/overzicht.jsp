<%@page contentType="text/html" pageEncoding="UTF-8" session="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix='fmt' uri='http://java.sun.com/jsp/jstl/fmt'%>
<%@taglib uri='http://vdab.be/tags' prefix='vdab'%>
<!doctype html>
<html>
<head>
<vdab:head title="Cultuurhuis > overzicht"></vdab:head>
</head>
<body>
	<vdab:header title="overzicht"></vdab:header>
</body>
<c:if test="${not empty gelukteReservaties}">
	<div>
		<h2>Gelukte reserveringen</h2>
		<table>
			<tr>
				<th>Datum</th>
				<th>Titel</th>
				<th>Uitvoerders</th>
				<th>Prijs(€)</th>
				<th>Plaatsen</th>				
			</tr>
			<c:forEach var="reservatie" items="${gelukteReservaties.entrySet()}">
				<tr>
					<td><fmt:formatDate value='${reservatie.getKey().datum}' type='both' dateStyle='short' timeStyle='short'/></td>
					<td>${reservatie.getKey().titel}</td>
					<td>${reservatie.getKey().uitvoerders}</td>
					<td class="number">€<fmt:formatNumber value='${reservatie.getKey().prijs}' minFractionDigits='2'/></td>
					<td class="number">${reservatie.value}</td>					
				</tr>
			</c:forEach>
		</table>
	</div>
</c:if>

<c:if test="${not empty mislukteReservaties}">
	<div>
		<h2>Mislukte reserveringen</h2>
		<table>
			<tr>
				<th>Datum</th>
				<th>Titel</th>
				<th>Uitvoerders</th>
				<th>Prijs(€)</th>
				<th>Plaatsen</th>	
				<th>Vrije plaatsen</th>			
			</tr>
			<c:forEach var="reservatie" items="${mislukteReservaties.entrySet()}">
				<tr>
					<td><fmt:formatDate value='${reservatie.getKey().datum}' type='both' dateStyle='short' timeStyle='short'/></td>
					<td>${reservatie.getKey().titel}</td>
					<td>${reservatie.getKey().uitvoerders}</td>
					<td class="number">€<fmt:formatNumber value='${reservatie.getKey().prijs}' minFractionDigits='2'/></td>
					<td class="number">${reservatie.value}</td>
					<td class="number">${reservatie.getKey().vrijeplaatsen}</td>					
				</tr>
			</c:forEach>
		</table>
	</div>
</c:if>
</html>