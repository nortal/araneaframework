<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jstl/core" xmlns:ui="http://araneaframework.org/tag-library/template" version="1.2">		
	<ui:widgetContext>
	
		<h3>Company</h3>
		
		<p>
			Here you must choose a company to connect it with this contract.
		</p>
		
		<p>
			<jsp:text>Company: </jsp:text>
			<c:if test="${viewData.company != null}">
				<c:out value="${viewData.company.name}"/>
			</c:if>
			<ui:eventButton eventId="chooseCompany" labelId="#Choose"/>			
		</p>

	</ui:widgetContext>	
</jsp:root>