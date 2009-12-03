<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	version="2.1">
	<ui:widgetContext>

		<tui:componentHeader>
			<tui:componentName><fmt:message key="radioselect.title"/></tui:componentName>
		</tui:componentHeader>

		<ui:form id="form">
			<tui:component>
				<tui:componentForm>
					<ui:row>
						<ui:formElement id="select">
							<ui:cell>
								<ui:label/>
							</ui:cell>
							<ui:cell>
								<ui:radioSelect/>
							</ui:cell>
						</ui:formElement>
					</ui:row>
				</tui:componentForm>

				<tui:componentActions>
					<ui:eventButton eventId="test" labelId="radioselect.button"/>
				</tui:componentActions>
			</tui:component>
		</ui:form>

	</ui:widgetContext>
</jsp:root>
