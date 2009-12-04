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

		<p><fmt:message key="contract.choose.person"/></p>

		<p>
			<b><jsp:text><fmt:message key="contract.person"/>: </jsp:text></b>
			<c:if test="${not empty widget.person}">
				<c:out value="${widget.person.name} ${widget.person.surname} "/>
			</c:if>
			<ui:eventButton eventId="choosePerson" labelId="common.choose"/>
		</p>

	</ui:widgetContext>	
</jsp:root>