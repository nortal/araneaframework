<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.0">

	<ui:widgetContext>
		<ui:form id="form">
			<tui:component>

				<ui:formElement id="richArea">
					<ui:richTextarea cols="100" rows="20" />
				</ui:formElement>

				<ui:formElement id="button">
					<ui:button updateRegions="preview" />
				</ui:formElement>

			</tui:component>
		</ui:form>

		<ui:updateRegion id="preview">
			<div style="border-style: solid; border-width: 1px; padding: 7px">
			<c:out value="${viewData.preview}" escapeXml="false" /></div>
		</ui:updateRegion>
	</ui:widgetContext>

</jsp:root>
