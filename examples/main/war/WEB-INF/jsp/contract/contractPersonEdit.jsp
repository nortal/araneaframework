<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jstl/core" xmlns:ui="http://araneaframework.org/tag-library/template" version="1.2">		
	<ui:widgetContext>
	
		<h3>Person</h3>
		
		<p>
			Here you must choose a person to connect it with this contract.
		</p>		
		
		<p>
			<jsp:text>Person: </jsp:text>
			<c:if test="${contextWidget.data.person != null}">
				<c:out value="${contextWidget.data.person.name} ${contextWidget.data.person.surname}"/>
			</c:if>
			<ui:eventButton eventId="choosePerson" labelId="#Choose"/>			
		</p>

	</ui:widgetContext>	
</jsp:root>