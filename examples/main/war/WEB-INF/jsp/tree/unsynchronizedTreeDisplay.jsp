<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<ui:widgetContext>
		A link for <a href="#" onclick="return new AraneaTree().displayAction(this, 'test', 'someData');">test action</a> in node display widget (clicked <c:out value="${viewData.counter}"/> times).
		<a href="#" onclick="return new AraneaTree().displayAction(this, 'sleep', 'someData');">Sleep</a>
	</ui:widgetContext>

</jsp:root>
