<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">
	
	<!-- Component starts here -->
	<ui:widgetContext>
		<ui:componentHeader>
			<ui:componentName><fmt:message key="${viewData.title}"/></ui:componentName>
		</ui:componentHeader>
		
		<ui:component>
			<c:if test="${viewData.returnValue != null}">
				<c:out value="${viewData.returnValue}"/>
			</c:if>
			
			<ui:updateRegion id="testPopupOpeningWithAJAX">
			
			<ui:componentActions>
				<ui:eventButton eventId="createThread" labelId="#Open popup (create new session thread)."/>
				<ui:eventButton eventId="openUrl" labelId="#Open some URL (/.)" updateRegions="testPopupOpeningWithAJAX"/>
				<ui:eventButton eventId="openNewCustomFlow" labelId="#Open interactive popup."/>
				<ui:eventButton eventId="openMountedPopup" labelId="#Open mounted URL."/>
 				<ui:eventButton 
 					eventId="endFlow" 
 					labelId="#End this flow."/>
			</ui:componentActions>

			</ui:updateRegion>
		</ui:component>

	</ui:widgetContext>
</jsp:root>
