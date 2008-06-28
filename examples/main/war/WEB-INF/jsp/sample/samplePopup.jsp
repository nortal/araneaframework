<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.0">
	
	<!-- Component starts here -->
	<ui:widgetContext>
		<tui:componentHeader>
			<tui:componentName><fmt:message key="${viewData.title}"/></tui:componentName>
		</tui:componentHeader>
		
		<tui:component>
			<c:if test="${viewData.returnValue != null}">
				<c:out value="${viewData.returnValue}"/>
			</c:if>
			
			<ui:updateRegion id="testPopupOpeningWithAJAX">
			
			<tui:componentActions>
				<ui:eventButton eventId="createThread" labelId="#Open popup (create new session thread)."/>
				<ui:eventButton eventId="openUrl" labelId="#Open some URL (/.)" updateRegions="testPopupOpeningWithAJAX"/>
				<ui:eventButton eventId="openNewCustomFlow" labelId="#Open interactive popup."/>
				<ui:eventButton eventId="openMountedPopup" labelId="#Open mounted URL."/>
				<ui:eventButton eventId="openImmediatelyReturningCustomFlow" labelId="#Open immediately returning interactive popup."/>
 				<ui:eventButton 
 					eventId="endFlow" 
 					labelId="#End this flow."/>
			</tui:componentActions>

			</ui:updateRegion>
		</tui:component>

	</ui:widgetContext>
</jsp:root>
