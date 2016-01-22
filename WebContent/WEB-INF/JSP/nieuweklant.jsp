<%@page contentType="text/html" pageEncoding="UTF-8" session="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri='http://vdab.be/tags' prefix='vdab'%>
<!doctype html>
<html>
<head>
<vdab:head title="Cultuurhuis > nieuwe klant"></vdab:head>
</head>
<body>
	<vdab:header title="nieuwe klant"></vdab:header>
	<div>
		<form method="post">
		
		<label>Voornaam:
				<input name='voornaam' value='${param.voornaam}' autofocus required>
		</label>
		
		<label>Familienaam:
				<input name='familienaam' value='${param.familienaam}' required>
		</label>
		
		<label>Straat:
				<input name='straat' value='${param.straat}' required>
		</label>
		
		<label>Huisnr:
				<input name='huisnr' value='${param.huisnr}' required>
		</label>
		
		<label>Postcode:
				<input name='postcode' value='${param.postcode}' required>
		</label>
		
		<label>Gemeente:
				<input name='gemeente' value='${param.gemeente}' required>
		</label>
		
		<label>Gebruikersnaam:
				<input name='gebruikersnaam' value='${param.gebruikersnaam}' required>
		</label>
		
		<label>Paswoord:
				<input type="password" name='paswoord' value='${param.paswoord}' required>
		</label>
		
		<label>Herhaal paswoord:
				<input type="password" name='verifpaswoord' value='${param.verifpaswoord}' required>
		</label>
		<label>		
			<input type='submit' value='OK'>
		</label>
			
		</form>
	</div>
	<c:if test="${not empty fouten}">
		<div>
			<ul>
				<c:forEach var="fout" items="${fouten}">
					<li>${fout}</li>
				</c:forEach>
			</ul>
		</div>
	</c:if>
</body>
</html>