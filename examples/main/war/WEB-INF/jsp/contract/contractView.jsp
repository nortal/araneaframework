<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jstl/core" xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template" version="1.2">		
	<ui:widgetContext>
	
		<h2>Viewing contract</h2>

		<p>
			Here you can see the data of selected contract.
			To get additional information of the company or person connected to this contract, click on it's name.
		</p>
				
		<p>
			<jsp:text>Company: </jsp:text>
			<ui:eventLinkButton eventId="viewCompany">
				<c:out value="${viewData.contract.company.name}"/>
			</ui:eventLinkButton>
		</p>
		
		<p>
			<jsp:text>Person: </jsp:text>
			<ui:eventLinkButton eventId="viewPerson">
				<c:out value="${viewData.contract.person.name}"/>
			</ui:eventLinkButton>
		</p>
			
		<p>
			<jsp:text>Notes: </jsp:text>
			<c:out value="${viewData.contract.notes}"/>
		</p>

		<ui:eventButton eventId="return" labelId="#Back"/>

	</ui:widgetContext>		
</jsp:root>