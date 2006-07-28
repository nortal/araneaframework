<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">
	
	<!-- Component starts here -->
	<ui:widgetContext>
		<ui:componentHeader>
			<ui:componentName><fmt:message key="${contextWidget.data.title}"/></ui:componentName>
		</ui:componentHeader>
		
		<ui:component>
			<c:if test="${contextWidget.data.returnValue != null}">
				<c:out value="${contextWidget.data.returnValue}"/>
			</c:if>
		
			<ui:componentActions>
				<ui:eventButton eventId="createThread" labelId="#Open popup (create new session thread)."/>
				<ui:eventButton eventId="openUrl" labelId="#Open some URL (/.)"/>
				<ui:eventButton eventId="openNewCustomFlow" labelId="#Open interactive popup."/>
				<ui:eventButton eventId="openMountedUrl" labelId="#Open mounted URL."/>
 				<ui:eventButton 
 					eventId="endFlow" 
 					labelId="#End this flow." />
			</ui:componentActions>
		</ui:component>

	</ui:widgetContext>
</jsp:root>
