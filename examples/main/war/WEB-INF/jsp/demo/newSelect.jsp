<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	version="1.2">
	<ui:widgetContext>
		<ui:form id="form">

			<tui:componentHeader>
				<tui:componentName>
					<fmt:message key="${viewData.formLabel}"/>
				</tui:componentName>
			</tui:componentHeader>

			<tui:component>

				<tui:componentForm>
					<ui:row>
						<ui:formElement id="personSelect">
							<ui:cell width="25%"/>
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>
							<ui:cell>
								<tui:select />
							</ui:cell>
							<ui:cell width="25%"/>
						</ui:formElement>
					</ui:row>

				</tui:componentForm>
				
				<tui:componentActions>
					<ui:eventButton labelId="#EVENT"/>
				</tui:componentActions>

			</tui:component>

		</ui:form>

	</ui:widgetContext>
</jsp:root>
