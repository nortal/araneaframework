<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	version="1.2">
	<ui:widgetContext>
		<ui:widgetInclude id="1"/>
		<ui:widgetInclude id="2"/>
		
		<p>
			Code of the two components on this page is identical.
			They behave a little differently though<ui:entity code="mdash"/>upper component
			communicates with server by HTTP requests and the bottom one by XMLHttpRequest.
			This was just achieved by adding about two lines of code to template that renders 
			bottom component. Using so called 'update regions' it is easy to make application
			behave in AJAXian manner.
		</p>
	</ui:widgetContext>
</jsp:root>
