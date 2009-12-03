<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.1">
	<ui:widgetContext>
		<ui:form id="personForm">

			<tui:componentHeader>
				<tui:componentName>Viewing person</tui:componentName>
			</tui:componentHeader>

			<tui:component>
				<tui:componentForm>
					<ui:row>
						<ui:formElement id="name">
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>
							<ui:cell styleClass="data">
								<ui:textDisplay styleClass="display" />
							</ui:cell>
						</ui:formElement>

						<ui:formElement id="surname">
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>
							<ui:cell styleClass="data">
								<ui:textDisplay />
							</ui:cell>
						</ui:formElement>
					</ui:row>

					<ui:row>
						<ui:formElement id="phone">
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>
							<ui:cell styleClass="data">
								<ui:textDisplay />
							</ui:cell>
						</ui:formElement>

						<ui:formElement id="birthdate">
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>
							<ui:cell styleClass="data">
								<ui:dateInputDisplay />
							</ui:cell>
						</ui:formElement>
					</ui:row>
					
					<ui:row>
						<ui:formElement id="salary">
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>
							<ui:cell styleClass="data">
								<ui:floatInputDisplay />
							</ui:cell>
						</ui:formElement>
					</ui:row>					
				</tui:componentForm>

				<tui:componentActions>
					<ui:eventButton eventId="return" labelId="#Back" />
				</tui:componentActions>
			</tui:component>

		</ui:form>
	</ui:widgetContext>
</jsp:root>
