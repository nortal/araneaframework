<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template" version="2.0">		
	<ui:widgetContext>
	
		<h3>Company</h3>
		
		<p>
			Here you must choose a company to connect it with this contract.
		</p>
		
		<p>
			<jsp:text>Company: </jsp:text>
			<c:if test="${widget.company != null}">
				<c:out value="${widget.company.name}"/>
			</c:if>
			<ui:eventButton eventId="chooseCompany" labelId="#Choose"/>			
		</p>

	</ui:widgetContext>	
</jsp:root>