<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core" 
	xmlns:fmt="http://java.sun.com/jstl/fmt" 
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<ui:widgetContext>
		Here is a link to <ui:eventLinkButton eventId="invertCollapsed" labelId="#toggle"/> the collapsed state of this node.
<!--
		A link for
		<ui:element name="a">
			<ui:attribute name="href" value="#"/>
			<ui:attribute name="onclick" value="_ap.action(this, 'test', '${widgetId}', 'someData', true, function(request, response) { Element.update('${widgetId}', request.responseText); }); return false;"/>
			<ui:elementContent>test action</ui:elementContent>
		</ui:element>
		in node display widget (clicked <c:out value="${viewData.counter}"/> times).
-->
	</ui:widgetContext>

</jsp:root>
