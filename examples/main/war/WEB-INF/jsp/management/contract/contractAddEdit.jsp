<?xml version="1.0" encoding="UTF-8"?>
<jsp:root
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.1">
	<ui:widgetContext>

		<h2><fmt:message key="contract.add.title"/></h2>

		<p><fmt:message key="contract.add.desc"/></p>

		<p><fmt:message key="contract.add.allFilled"/></p>

		<tui:wizard id="wizard">
			<tui:wizardHeader/>
			<tui:wizardBody/>
			<tui:wizardFooter/>
		</tui:wizard>

	</ui:widgetContext>
</jsp:root>