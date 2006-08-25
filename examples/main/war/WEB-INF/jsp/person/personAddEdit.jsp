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
					<fmt:message key="${viewData.label}"/>
				</ui:componentName>
			</ui:componentHeader>

			<ui:component>

				<ui:componentForm>
					<ui:row>
						<ui:formElement id="name">
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>
							<ui:cell>
								<ui:textInput />
							</ui:cell>
						</ui:formElement>

						<ui:formElement id="surname">
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>
							<ui:cell>
								<ui:textInput />
							</ui:cell>
						</ui:formElement>
					</ui:row>

					<ui:row>
						<ui:formElement id="phone">
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>
							<ui:cell>
								<ui:textInput />
							</ui:cell>
						</ui:formElement>

						<ui:formElement id="birthdate">
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>
							<ui:cell>
								<ui:dateInput />
							</ui:cell>
						</ui:formElement>
					</ui:row>					

					<ui:row>
						<ui:formElement id="salary">
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>
							<ui:cell colspan="3">
								<ui:floatInput />
							</ui:cell>
						</ui:formElement>
					</ui:row>					
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
