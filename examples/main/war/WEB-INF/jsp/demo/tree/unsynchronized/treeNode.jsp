<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.1">

	<ui:widgetContext>
		A link for <a href="#" onclick="return Aranea.Tree.displayAction(this, 'test', 'someData');">test action</a> in node display widget (clicked <c:out value="${viewData.counter}"/> times).
		<a href="#" onclick="return Aranea.Tree.displayAction(this, 'sleep', 'someData');">Sleep</a>
	</ui:widgetContext>

</jsp:root>
