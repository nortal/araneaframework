<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.0">

	<ui:widgetContext>

		<!-- Label -->
		<tui:componentHeader>
			<tui:componentName>Tutorial complex tree</tui:componentName>
        </tui:componentHeader>

		<tui:component>

			<ui:tree id="tree"/>

			<p><ui:eventLinkButton eventId="addNode" labelId="#Add a top-level node to tree"/></p>

		</tui:component>

	</ui:widgetContext>

</jsp:root>
