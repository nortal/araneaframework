<?xml version="1.0" encoding="UTF-8"?>
<jsp:root
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/template"
version="1.2">		
	<ui:widgetContext>
	
		<h2>Editing person</h2>
				
		<p>
			Here you can modify data of selected person.
		</p>
		
		<p>
			Fields <b>First name</b> and <b>Last name</b> must be filled.
		</p>		
		
		<ui:form id="form">
		
			<p>
				<jsp:text>First name: </jsp:text><ui:newLine/>
				<ui:textInput id="name"/>
			</p>
			
			<p>
				<jsp:text>Last name: </jsp:text><ui:newLine/>
				<ui:textInput id="surname"/>
			</p>
			
			<p>
				<jsp:text>Phone no: </jsp:text><ui:newLine/>
				<ui:textInput id="phone"/>
			</p>
			
			<p>
				<jsp:text>Birthdate: </jsp:text><ui:newLine/>
				<ui:dateInput id="birthDate"/>
			</p>
			
		</ui:form>				
		
		<ui:eventButton eventId="save" labelId="#Save"/>
		<ui:eventButton eventId="cancel" labelId="#Cancel"/>

	</ui:widgetContext>		
</jsp:root>