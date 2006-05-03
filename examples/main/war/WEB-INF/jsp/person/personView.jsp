<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">
	<ui:widgetContext>
		<ui:form id="personForm">

			<ui:componentHeader>
				<ui:componentName>Viewing person</ui:componentName>
			</ui:componentHeader>

			<ui:component>
				<ui:componentForm>
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
				</ui:componentForm>

				<ui:componentActions>
					<ui:eventButton eventId="return" labelId="#Back" />
				</ui:componentActions>
			</ui:component>

		</ui:form>
	</ui:widgetContext>
</jsp:root>
