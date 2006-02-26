<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jstl/core" xmlns:ui="http://araneaframework.org/tag-library/template" version="1.2">		
	<ui:widgetContext>
	
		<h2>Viewing person</h2>
		
		<p>
			Here you can see the data of selected person.
		</p>
				
		<p>
			<jsp:text>First name: </jsp:text>
			<c:out value="${contextWidget.data.person.name}"/>
		</p>
		
		<p>
			<jsp:text>Last name: </jsp:text>
			<c:out value="${contextWidget.data.person.surname}"/>
		</p>
		
		<p>
			<jsp:text>Phone no: </jsp:text>
			<c:out value="${contextWidget.data.person.phone}"/>
		</p>
		
		<ui:eventButton eventId="return" labelId="#Back"/>

	</ui:widgetContext>		
</jsp:root>