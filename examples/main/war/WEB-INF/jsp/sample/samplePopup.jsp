<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<!-- Component starts here -->
	<ui:widgetContext>
		<ui:componentHeader>
			<ui:componentName>Popup example</ui:componentName>
		</ui:componentHeader>
		
		<ui:component>
			<ui:componentActions>
				<ui:eventButton eventId="createThread" labelId="#Open popup (create new session thread)."/>
				<ui:eventButton eventId="openUrl" labelId="#Open some URL (/.)"/>
				<ui:eventButton eventId="openNewCustomFlow" labelId="#Open flow as root flow."/>
				<ui:eventButton eventId="endFlow" labelId="#End this flow."/>
			</ui:componentActions>
		</ui:component>

	</ui:widgetContext>
</jsp:root>
