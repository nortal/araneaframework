<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template" version="2.0">		
	<ui:widgetContext>
	
		<h3>Person</h3>
		
		<p>
			Here you must choose a person to connect it with this contract.
		</p>		
		
		<p>
			<jsp:text>Person: </jsp:text>
			<c:if test="${widget.person != null}">
				<c:out value="${widget.person.name} ${widget.person.surname}"/>
			</c:if>
			<ui:eventButton eventId="choosePerson" labelId="#Choose"/>			
		</p>

	</ui:widgetContext>	
</jsp:root>