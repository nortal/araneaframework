<?xml version="1.0" encoding="UTF-8"?>
<jsp:root 
	xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:c="http://java.sun.com/jstl/core" 
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template" version="1.2">
	
	<ui:widgetContext>

	<div id="footer">
		<div class="box1">
		     <c:if test="${not empty viewData['srcLink']}">
		 	   <c:out value="${viewData['srcLink']}" escapeXml="false"/><i>|</i>
			</c:if>
		     <c:if test="${not empty viewData['templateSrcLink']}">
		 	   <c:out value="${viewData['templateSrcLink']}" escapeXml="false"/><i>|</i>
			</c:if>
			<ui:entity code="copy"/> <a href="http://www.webmedia.ee" target="_blank">Webmedia</a> 2008 <i>|</i>
			<b>Aranea <c:out value="${viewData['aranea-version']}"/></b><i>|</i> 
			<a href="mailto:info@araneaframework.org">info@araneaframework.org</a>
		</div>
	</div>

	</ui:widgetContext>
</jsp:root>