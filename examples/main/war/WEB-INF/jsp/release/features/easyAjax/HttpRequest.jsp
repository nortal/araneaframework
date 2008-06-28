<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	version="2.0">
	<ui:widgetContext>
		<ui:form id="complexForm">

			<tui:componentHeader>
				<tui:componentName>
					<!-- The label, defined in DemoComplexForm class. -->
					<fmt:message key="${viewData.formLabel}"/>
				</tui:componentName>
			</tui:componentHeader>

			<tui:component>

				<tui:componentForm>
					<ui:row>
						<ui:formElement id="beastSelection">
							<ui:cell width="25%"/>
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>
							<ui:cell>
								<ui:select/>
							</ui:cell>
							<ui:cell width="25%"/>
						</ui:formElement>
					</ui:row>

					<c:if test="${not empty form.elements['concreteBeastControl']}">
						<ui:row>
							<ui:formElement id="selectedBeastDesc">
								<ui:cell colspan="2" styleClass="wrap-centered" width="50%">
									<ui:textDisplay/>
								</ui:cell>
							</ui:formElement>
		
							<ui:formElement id="concreteBeastControl">
								<ui:cell styleClass="centered-name">
									<ui:label />
								</ui:cell>
								<ui:cell>
									<!-- Render MultiSelectControl with checkboxes. 
									     Instead ui:multiSelect could be used ... -->
									<ui:checkboxMultiSelect type="vertical" />
								</ui:cell>
							</ui:formElement>
						</ui:row>
					</c:if>

				</tui:componentForm>

			</tui:component>

		</ui:form>

	</ui:widgetContext>
</jsp:root>
