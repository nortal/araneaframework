<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<!-- Component starts here -->
	<ui:widgetContext>
		<ui:widgetInclude id="wrapped"/>		

		<tui:componentActions>
			<ui:eventButton eventId="gobacknow" labelId="common.Return"/> 
		</tui:componentActions>			
	</ui:widgetContext>
</jsp:root>
