<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core" 
	xmlns:fmt="http://java.sun.com/jstl/fmt" 
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<ui:widgetContext>

		<ui:componentHeader>
			<ui:componentName>GWT Hello</ui:componentName>
        </ui:componentHeader>

		<ui:component>

			<ui:eventButton eventId="test" labelId="#Empty submit"/>

			<ui:gwtWidgetInclude id="hello1"/>
			<ui:gwtWidgetInclude id="hello2"/>
			<ui:gwtWidgetInclude id="hello3"/>

		</ui:component>

	</ui:widgetContext>

</jsp:root>
