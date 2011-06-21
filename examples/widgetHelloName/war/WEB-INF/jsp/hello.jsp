<?xml version="1.0" encoding="UTF-8"?>
<jsp:root 
	xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.1">

	<ui:widgetContext>
		<html>
			<head>
				<ui:importScripts/>
				<title>Aranea Widget Hello Name Example</title>
			</head>

			<ui:body>
				Hello <c:out value="${widget.name}"/>!
			</ui:body>
		</html>
	</ui:widgetContext>

</jsp:root>
