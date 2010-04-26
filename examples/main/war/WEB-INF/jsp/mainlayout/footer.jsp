<?xml version="1.0" encoding="UTF-8"?>
<jsp:root 
	xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:c="http://java.sun.com/jsp/jstl/core" 
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.1">

	<ui:widgetContext>

	<div id="footer">
		<div class="box1">
			<c:if test="${not empty viewData['srcLink']}">
				${viewData['srcLink']}<i>|</i>
			</c:if>

			<c:if test="${not empty viewData['templateSrcLink']}">
				${viewData['templateSrcLink']}<i>|</i>
			</c:if>

			<ui:entity code="copy"/>
			<a href="http://www.webmedia.ee" target="_blank">Webmedia</a> 2009 <i>|</i>

			<b>Aranea <c:out value="${viewData['aranea-version']}"/></b><i>|</i>
			<a href="mailto:info@araneaframework.org">info@araneaframework.org</a>
		</div>
	</div>

	</ui:widgetContext>
</jsp:root>