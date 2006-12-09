<?xml version="1.0" encoding="UTF-8"?>
<jsp:root 
	xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard" version="1.2">
	<ui:widgetContext>
		Hello <c:out value="${widget.name}"/>!<br/>
		<ui:eventButton labelId="#Back" eventId="back"/>
	</ui:widgetContext>
</jsp:root>
