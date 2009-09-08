<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<ui:widgetContext>
		<ui:form id="testform">

			<tui:componentHeader>
				<tui:componentName>
					<fmt:message key="ac.demo.title" />
				</tui:componentName>
			</tui:componentHeader>

			<tui:component>
				<p><fmt:message key="ac.demo.intro" /></p>
				<p><fmt:message key="ac.demo.howto" /></p>
				<br />

				<tui:componentForm>

					<ui:row>

						<ui:formElement id="acinput">
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>

							<ui:cell styleClass="inpt">
								<ui:autoCompleteTextInput/>
							</ui:cell>
						</ui:formElement>

					</ui:row>

				</tui:componentForm>

				<tui:componentActions>
					<ui:eventButton eventId="test" labelId="common.Submit" />
				</tui:componentActions>
			</tui:component>
		</ui:form>

	</ui:widgetContext>
</jsp:root>
