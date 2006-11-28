<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core" 
	xmlns:fmt="http://java.sun.com/jstl/fmt" 
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<ui:widgetContext>
		Here is a link to <ui:eventLinkButton eventId="invertCollapsed" labelId="#toggle"/> the collapsed state of this node.
		A link for <a href="#" onclick="return AraneaTree.displayAction(this, 'test', 'someData');">test action</a> in node display widget (clicked <c:out value="${viewData.counter}"/> times).
		<a href="#" onclick="return AraneaTree.displayAction(this, 'sleep', 'someData');">Sleep</a>
	</ui:widgetContext>

</jsp:root>
