<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jstl/core" xmlns:ui="http://araneaframework.org/tag-library/template" version="1.2">		
	<ui:widgetContext>
	
		<h2>Editing company</h2>
		
		<p>
			Here you can modify data of selected company.
		</p>
		
		<p>
			Field <b>Name</b> must be filled.
		</p>
				
		<ui:form id="form">
				
			<p>
				<jsp:text>Name: </jsp:text><ui:newLine/>
				<ui:textInput id="name"/>
			</p>
			
			<p>
				<jsp:text>Address: </jsp:text><ui:newLine/>
				<ui:textInput id="address"/>
			</p>
			
		</ui:form>				
		
		<ui:eventButton eventId="save" labelId="#Save"/>
		<ui:eventButton eventId="cancel" labelId="#Cancel"/>

	</ui:widgetContext>		
</jsp:root>