<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	version="1.2">
	
	<!-- View for tutorial (and template application) BeanFormWidget example
		 (org.araneaframework.example.main.web.person.PersonAddEditWidget) -->
	<!-- Not as richly commented as SimpleFormWidget's view (simpleForm.jsp), take a look
	     at that if you have forgotten meanings of some tags -->
	<ui:widgetContext>

		<ui:form id="personForm">

			<ui:componentHeader>
				<ui:componentName>
					<fmt:message key="${contextWidget.data.label}"/>
				</ui:componentName>
			</ui:componentHeader>

			<ui:component>

				<ui:componentForm>
					<ui:newRow>
						<ui:formElement id="name">
							<ui:newCell styleClass="name">
								<ui:label />
							</ui:newCell>
							<ui:newCell>
								<ui:textInput />
							</ui:newCell>
						</ui:formElement>

						<ui:formElement id="surname">
							<ui:newCell styleClass="name">
								<ui:label />
							</ui:newCell>
							<ui:newCell>
								<ui:textInput />
							</ui:newCell>
						</ui:formElement>
					</ui:newRow>

					<ui:newRow>
						<ui:formElement id="phone">
							<ui:newCell styleClass="name">
								<ui:label />
							</ui:newCell>
							<ui:newCell>
								<ui:textInput />
							</ui:newCell>
						</ui:formElement>

						<ui:formElement id="birthdate">
							<ui:newCell styleClass="name">
								<ui:label />
							</ui:newCell>
							<ui:newCell>
								<ui:dateInput />
							</ui:newCell>
						</ui:formElement>
					</ui:newRow>
				</ui:componentForm>

				<!-- template design tag -->
				<ui:componentActions>
					<!-- Sends all form data and calls "save" event (handleEventSave) -->
					<ui:eventButton eventId="save" labelId="#Add" />
					<!-- Sends all form data and calls "cancel" event (handleEventCancel) -->
					<ui:eventButton eventId="cancel" labelId="#Cancel" />
				</ui:componentActions>
			</ui:component>

		</ui:form>

	</ui:widgetContext>
</jsp:root>
