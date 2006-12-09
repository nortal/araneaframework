<?xml version="1.0" encoding="UTF-8"?>
<jsp:root 
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard_rt"
	xmlns:t="http://araneaframework.org/tag-library/template"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html" version="2.0">

	<ui:widgetContext>

		<t:componentHeader>
			<t:componentName>Flows?</t:componentName>
		</t:componentHeader>

		<t:component>
			<ui:widgetInclude id="sadWidget"/>

			<t:componentActions>
				<ui:eventButton eventId="next" labelId="#Try next!"/>
			</t:componentActions>
		</t:component>

	</ui:widgetContext>
</jsp:root>
