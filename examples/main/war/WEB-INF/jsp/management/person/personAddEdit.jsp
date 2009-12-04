<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	version="2.1">
	
	<!-- View for tutorial (and template application) BeanFormWidget example
		 (org.araneaframework.example.main.web.person.PersonAddEditWidget) -->
	<!-- Not as richly commented as SimpleFormWidget's view (simpleForm.jsp), take a look
	     at that if you have forgotten meanings of some tags -->
	<ui:widgetContext>

		<ui:form id="personForm">

			<tui:componentHeader>
				<tui:componentName>
					<fmt:message key="${viewData.label}"/>
				</tui:componentName>
			</tui:componentHeader>

			<tui:component>

				<tui:componentForm rowClasses="cols4" cellClasses="name,inpt">
					<ui:row>
						<ui:formElement id="name">
							<ui:cell>
								<ui:label />
							</ui:cell>
							<ui:cell>
								<ui:textInput />
							</ui:cell>
						</ui:formElement>

						<ui:formElement id="surname">
							<ui:cell>
								<ui:label />
							</ui:cell>
							<ui:cell>
								<ui:textInput />
							</ui:cell>
						</ui:formElement>
					</ui:row>

					<ui:row>
						<ui:formElement id="phone">
							<ui:cell>
								<ui:label />
							</ui:cell>
							<ui:cell>
								<ui:textInput />
							</ui:cell>
						</ui:formElement>

						<ui:formElement id="birthdate">
							<ui:cell>
								<ui:label />
							</ui:cell>
							<ui:cell>
								<ui:dateInput />
							</ui:cell>
						</ui:formElement>
					</ui:row>					

					<ui:row>
						<ui:formElement id="salary">
							<ui:cell>
								<ui:label />
							</ui:cell>
							<ui:cell colspan="3">
								<ui:floatInput />
							</ui:cell>
						</ui:formElement>
					</ui:row>
				</tui:componentForm>

				<!-- template design tag -->
				<tui:componentActions>
					<!-- Sends all form data and calls "save" event (handleEventSave) -->
					<ui:eventButton eventId="save" labelId="#OK" />
					<!-- Sends all form data and calls "cancel" event (handleEventCancel) -->
					<ui:eventButton eventId="cancel" labelId="#Cancel" />
				</tui:componentActions>
			</tui:component>

		</ui:form>

	</ui:widgetContext>
</jsp:root>
