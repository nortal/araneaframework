<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/template" 
	
	version="1.2">		
	<ui:widgetContext>
			<h2>Adding new Company</h2>
			
			<p>
				Here you can fill the data of new company.
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

			<ui:containerFooter>
				<ui:eventButton eventId="save" labelId="#Add"/>
			</ui:containerFooter>
	</ui:widgetContext>	
</jsp:root>