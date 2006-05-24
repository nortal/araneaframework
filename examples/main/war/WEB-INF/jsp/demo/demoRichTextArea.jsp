<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<ui:widgetContext>
		<ui:form id="form">
			<ui:component>
				<ui:formElement id="richArea">
								<ui:richtextarea cols="100" rows="20"/>
				</ui:formElement>


				<ui:formElement id="button">
					<ui:button/>
				</ui:formElement>
			</ui:component>
		</ui:form>
		
			<div style="border-style: solid; border-width: 1px; padding: 7px">
				<c:out value="${contextWidget.data.preview}" escapeXml="false" />
			</div>
				
		
	</ui:widgetContext>
</jsp:root>
