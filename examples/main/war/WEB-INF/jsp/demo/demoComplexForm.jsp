<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	version="1.2">
	<ui:widgetContext>
		<ui:form id="complexForm">

			<ui:componentHeader>
				<ui:componentName>
					<fmt:message key="${contextWidget.data.formLabel}"/>
				</ui:componentName>
			</ui:componentHeader>

			<ui:component>

				<ui:componentForm>
					<ui:row>
						<ui:formElement id="beastSelection">
							<ui:cell width="25%"/>
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>
							<ui:cell>
								<ui:select />
							</ui:cell>
							<ui:cell width="25%"/>
						</ui:formElement>
					</ui:row>

					<!-- A way to test whether form elements are present. -->
					<c:if test="${not empty form.elements['concreteBeastControl']}">
						<ui:row>
							<ui:formElement id="selectedBeastDesc">
								<ui:cell colSpan="2" styleClass="wrap-centered" width="50%">
									<ui:textDisplay/>
								</ui:cell>
							</ui:formElement>
		
							<ui:formElement id="concreteBeastControl">
								<ui:cell styleClass="centered-name">
									<ui:label />
								</ui:cell>
								<ui:cell>
									<!-- ui:multiSelect could be used instead -->
									<ui:checkboxMultiSelect type="vertical" />
								</ui:cell>
							</ui:formElement>
						</ui:row>
					</c:if>
				</ui:componentForm>

				<ui:componentActions>
					<ui:eventButton eventId="save" labelId="#Add" />
					<ui:eventButton eventId="cancel" labelId="#Cancel" />
				</ui:componentActions>
			</ui:component>

		</ui:form>

	</ui:widgetContext>
</jsp:root>
