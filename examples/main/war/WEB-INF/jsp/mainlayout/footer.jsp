<?xml version="1.0" encoding="UTF-8"?>
<jsp:root 
	xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:c="http://java.sun.com/jstl/core" 
	xmlns:ui="http://araneaframework.org/tag-library/template" version="1.2">
	
	<ui:widgetContext>

	<div id="footer">
		<div class="box1">
		     <c:if test="${not empty contextWidget.data['srcLink']}">
		 	   <c:out value="${contextWidget.data['srcLink']}" escapeXml="false"/><i>|</i>
			</c:if>
		     <c:if test="${not empty contextWidget.data['templateSrcLink']}">
		 	   <c:out value="${contextWidget.data['templateSrcLink']}" escapeXml="false"/><i>|</i>
			</c:if>
			<ui:entity code="copy"/> <a href="http://www.webmedia.ee">Webmedia</a> 2006 <i>|</i>
			Aranea <c:out value="${contextWidget.data['aranea-version']}"/><i>|</i> 
			<a href="mailto:info@araneaframework.org">info@araneaframework.org</a>
		</div>
	</div>

	</ui:widgetContext>
</jsp:root>