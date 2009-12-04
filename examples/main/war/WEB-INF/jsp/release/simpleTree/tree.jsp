<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.1">

	<ui:widgetContext>

		<!-- Label -->
		<tui:componentHeader>
			<tui:componentName><fmt:message key="tree.title"/></tui:componentName>
		</tui:componentHeader>

		<tui:component>
			<p><fmt:message key="tree.intro"/></p>
			<p><fmt:message key="tree.more"/></p>

			<p><fmt:message key="tree.events"/></p>
			<ui:tree id="tree1"/>

			<p><fmt:message key="tree.actions"/></p>
			<ui:tree id="tree2"/>
		</tui:component>

	</ui:widgetContext>

</jsp:root>
