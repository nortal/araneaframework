<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jstl/core" xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template" version="1.2">		
	<ui:widgetContext>
	
		<h3>Additional data</h3>
		
		<p>
			Here you can modify additional data of this contract.
		</p>		
		
		<p>
			Field <b>Notes</b> must be filled.
		</p>		
		
		<ui:form id="form">
		
			<p>
				<jsp:text>Notes: </jsp:text><ui:newLine/>
				<ui:textInput id="notes"/>
			</p>
			
			<p>
				<jsp:text>Total: </jsp:text><ui:newLine/>
				<ui:floatInput id="total" />
			</p>			
			
		</ui:form>

	</ui:widgetContext>		
</jsp:root>