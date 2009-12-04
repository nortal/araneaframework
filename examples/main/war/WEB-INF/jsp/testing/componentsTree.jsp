<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="1.2">
	<ui:widgetContext>
		<h1><fmt:message key="componentsTree.title" /></h1>
		<p><fmt:message key="componentsTree.desc1" /></p>
		<p><fmt:message key="componentsTree.desc2" /></p>
		<p><c:out value="${viewData.tree}" escapeXml="false" /></p>
	</ui:widgetContext>
</jsp:root>
