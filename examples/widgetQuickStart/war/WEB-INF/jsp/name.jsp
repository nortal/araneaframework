<?xml version="1.0" encoding="UTF-8"?>
<jsp:root 
	xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/template" version="1.2">
	<ui:widgetContext>
		<ui:form id="nameForm">
			<ui:layout styleClass="form">
				<ui:row>
					<ui:cell styleClass="name">
						<ui:label id="name"/>
					</ui:cell>
					<ui:cell styleClass="inpt">
						<ui:textInput id="name"/>						
					</ui:cell>											
				</ui:row>
			</ui:layout>
			<div class="actions">
				<ui:eventButton labelId="#Hello" eventId="hello"/>
			</div>
		</ui:form>
	</ui:widgetContext>
</jsp:root>