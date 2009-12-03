<?xml version="1.0" encoding="UTF-8"?>
<jsp:root
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.1">
	<ui:widgetContext>

		<h2><fmt:message key="contract.company"/></h2>

		<p><fmt:message key="contract.choose.company"/></p>

		<p>
			<b><jsp:text><fmt:message key="contract.company"/>: </jsp:text></b>
			<c:if test="${not empty widget.company}">
				<c:out value="${widget.company.name} "/>
			</c:if>
			<ui:eventButton eventId="chooseCompany" labelId="common.choose"/>
		</p>

	</ui:widgetContext>	
</jsp:root>