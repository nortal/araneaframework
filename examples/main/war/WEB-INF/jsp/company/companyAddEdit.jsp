<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	version="2.0">
	<ui:widgetContext>
		<tui:componentHeader>
			<tui:componentName>
				<fmt:message key="${viewData.formLabel}"/>
			</tui:componentName>
		</tui:componentHeader>

		<tui:component>

			<ui:form id="form">
				<tui:componentForm rowClasses="cols4" cellClasses="name,inpt">
					<ui:formElement id="name">
						<ui:row>
							<ui:cell>
								<ui:label />
							</ui:cell>
							<ui:cell>
								<ui:textInput />
							</ui:cell>
						</ui:row>
					</ui:formElement>

					<ui:formElement id="address">
						<ui:row>
							<ui:cell>
								<ui:label />
							</ui:cell>
							<ui:cell>
								<ui:textInput />
							</ui:cell>
						</ui:row>
					</ui:formElement>
				</tui:componentForm>

			</ui:form>

			<tui:componentActions>
				<ui:eventButton eventId="save" labelId="#Save" />
				<ui:eventButton eventId="cancel" labelId="#Cancel" />
			</tui:componentActions>
		</tui:component>

	</ui:widgetContext>
</jsp:root>
