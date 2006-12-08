<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core" 
	xmlns:fmt="http://java.sun.com/jstl/fmt" 
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<ui:widgetContext>

		<ui:componentHeader>
			<ui:componentName>Some other widget :)</ui:componentName>
        </ui:componentHeader>

		<ui:component>

			<p>Nothing to see here, please move along.</p>

			<p>			
				<ui:eventButton eventId="finish" labelId="#Go back"/>
			</p>

		</ui:component>

	</ui:widgetContext>

</jsp:root>
