<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core" 
	xmlns:fmt="http://java.sun.com/jstl/fmt" 
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<ui:widgetContext>

		<ui:componentHeader>
			<ui:componentName>GWT Mail App</ui:componentName>
        </ui:componentHeader>

		<ui:component>

			<p>
				<ui:eventButton eventId="test" labelId="#Empty submit"/>
			</p>
			<p>
				<ui:eventButton eventId="start" labelId="#Start new flow"/>
			</p>

			<ui:gwtWidgetInclude id="mail" styleClass="gwtMail"/>

		</ui:component>

	</ui:widgetContext>

</jsp:root>
