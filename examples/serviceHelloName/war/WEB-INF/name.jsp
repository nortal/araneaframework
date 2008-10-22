<?xml version="1.0" encoding="UTF-8"?>
<jsp:root 
	xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0">
	<jsp:directive.page contentType="text/html; charset=UTF-8"/>
	
	<html>
		<head><title>What's your name?</title></head>
		<body>
			<form action="main/hello">
				What is your name? <input name="name" type="text"/><br/>
				<input type="submit" value="Say hello"/>
			</form>
		</body>
	</html>
</jsp:root>
