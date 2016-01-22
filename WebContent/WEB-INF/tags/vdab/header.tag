<%@tag description='header body' pageEncoding='UTF-8'%>
<%@attribute name='title' required='true' type='java.lang.String'%>
<%@taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core'%>
<header>
	<img alt="${title}" src="<c:url value='/images/${title}.png'/>">
	<h1>Het Cultuurhuis: ${title}</h1>	
	<nav>
		<c:if test="${title != 'voorstellingen'}">
			<a href="<c:url value='/'/>">Voorstellingen</a>
		</c:if>
		<c:if test="${not empty mandje}">
			<c:if test="${title != 'reservatiemandje'}">
				<a href="<c:url value='/reservatiemandje.htm'/>">Reservatiemandje</a>
			</c:if>
			<c:if test="${title != 'bevestiging reservaties'}">
				<a href="<c:url value='/bevestigreservatie.htm'/>">Bevestiging reservatie</a>
			</c:if>
		</c:if>		
	</nav>
</header>
