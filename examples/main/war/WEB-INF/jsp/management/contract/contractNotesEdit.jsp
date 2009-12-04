<?xml version="1.0" encoding="UTF-8"?>
<jsp:root
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.1">
	<ui:widgetContext>

		<h2><fmt:message key="contract.person"/></h2>

		<p><fmt:message key="contract.choose.additional"/></p>

		<p><fmt:message key="contract.notes.required"/></p>

		<ui:form id="form">
			<p>
				<jsp:text><fmt:message key="contract.notes"/>: </jsp:text>
				<ui:newLine/>
				<ui:textInput id="notes"/>
			</p>
			<p>
				<jsp:text><fmt:message key="contract.total"/>: </jsp:text><ui:newLine/>
				<ui:floatInput id="total" />
			</p>
		</ui:form>

	</ui:widgetContext>
</jsp:root>