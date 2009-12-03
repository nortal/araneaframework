<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.1">

	<!-- Component starts here -->
	<ui:widgetContext>

		<tui:componentHeader>
			<tui:componentName><fmt:message key="modalDialogTest.title"/></tui:componentName>
		</tui:componentHeader>

		<tui:component>
			<tui:componentForm>

				<ui:form id="form">
					<table>
						<ui:row>

							<ui:cell styleClass="name">
								<ui:label id="classSelect"/>
							</ui:cell>

							<ui:cell styleClass="inpt">
								<ui:select id="classSelect" localizeDisplayItems="false"/>
							</ui:cell>

						</ui:row>
					</table>
				</ui:form>

			</tui:componentForm>
		</tui:component>

	</ui:widgetContext>
</jsp:root>
