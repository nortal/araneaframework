<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.0">

	<!-- Component starts here -->
	<ui:widgetContext>
		<ui:widgetInclude id="wrapped"/>		

		<tui:componentActions>
			<ui:eventButton eventId="gobacknow" labelId="common.Return"/> 
		</tui:componentActions>			
	</ui:widgetContext>
</jsp:root>
