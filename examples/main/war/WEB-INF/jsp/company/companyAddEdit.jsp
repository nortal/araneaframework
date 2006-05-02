<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	version="1.2">
	<ui:widgetContext>
		<ui:componentHeader>
			<ui:componentName>
				<fmt:message key="${contextWidget.data.formLabel}"/>
			</ui:componentName>
		</ui:componentHeader>

		<ui:component>

			<ui:form id="form">
				<ui:componentForm>
					<ui:formElement id="name">
						<ui:newRow styleClass="cols4">
							<ui:newCell styleClass="name">
								<ui:label />
							</ui:newCell>
							<ui:newCell styleClass="inpt">
								<ui:textInput />
							</ui:newCell>
						</ui:newRow>
					</ui:formElement>

					<ui:formElement id="address">
						<ui:newRow styleClass="cols4">
							<ui:newCell styleClass="name">
								<ui:label />
							</ui:newCell>
							<ui:newCell styleClass="inpt">
								<ui:textInput />
							</ui:newCell>
						</ui:newRow>
					</ui:formElement>
				</ui:componentForm>

			</ui:form>

			<ui:componentActions>
				<ui:eventButton eventId="save" labelId="#Save" />
				<ui:eventButton eventId="cancel" labelId="#Cancel" />
			</ui:componentActions>
		</ui:component>

	</ui:widgetContext>
</jsp:root>
