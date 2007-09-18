<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core" 
	xmlns:fmt="http://java.sun.com/jstl/fmt" 
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<ui:widgetContext>
		<ui:form id="form1">
			<tui:componentHeader>
				<tui:componentName><fmt:message key="seamless.withoutbg.form"/></tui:componentName>
		     </tui:componentHeader>
	
			<tui:component>
				<ui:row>
					<ui:formElement id="futureDate">
						<ui:cell>
							<ui:label/>
						</ui:cell>
						<ui:cell>
							<ui:dateInput/>								
						</ui:cell>
					</ui:formElement>
				</ui:row>
			</tui:component>
		</ui:form>
	
		<ui:form id="form2">
			<tui:componentHeader>
				<tui:componentName><fmt:message key="seamless.bg.form"/></tui:componentName>
	        </tui:componentHeader>

			<tui:component>
				<ui:row>
					<ui:formElement id="futureDate">
						<ui:cell>
							<ui:label/>
						</ui:cell>
						<ui:cell>
							<ui:dateInput/>								
						</ui:cell>
					</ui:formElement>
				</ui:row>
			</tui:component>
		</ui:form>

	</ui:widgetContext>
</jsp:root>
