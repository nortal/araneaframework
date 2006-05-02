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
					<ui:newRow>
						<ui:formElement id="name">
							<ui:newCell styleClass="name">
								<ui:label />
							</ui:newCell>
							<ui:newCell styleClass="data">
								<ui:textDisplay styleClass="display" />
							</ui:newCell>
						</ui:formElement>

						<ui:formElement id="surname">
							<ui:newCell styleClass="name">
								<ui:label />
							</ui:newCell>
							<ui:newCell styleClass="data">
								<ui:textDisplay />
							</ui:newCell>
						</ui:formElement>
					</ui:newRow>

					<ui:newRow>
						<ui:formElement id="phone">
							<ui:newCell styleClass="name">
								<ui:label />
							</ui:newCell>
							<ui:newCell styleClass="data">
								<ui:textDisplay />
							</ui:newCell>
						</ui:formElement>

						<ui:formElement id="birthdate">
							<ui:newCell styleClass="name">
								<ui:label />
							</ui:newCell>
							<ui:newCell styleClass="data">
								<ui:dateInputDisplay />
							</ui:newCell>
						</ui:formElement>
					</ui:newRow>
				</ui:componentForm>

				<ui:componentActions>
					<ui:eventButton eventId="return" labelId="#Back" />
				</ui:componentActions>
			</ui:component>

		</ui:form>
	</ui:widgetContext>
</jsp:root>
