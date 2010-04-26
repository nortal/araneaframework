<?xml version="1.0" encoding="UTF-8"?>
<jsp:root 
	xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	version="2.1">

	<jsp:directive.page contentType="text/html; charset=UTF-8"/>

	<html>
		<head><title>Hello World</title></head>
		<body>
			Hello <c:out value="${helloName}"/>
		</body>
	</html>
</jsp:root>
