<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	version="2.1">
	<ui:widgetContext>
		<ui:form id="complexForm">

			<tui:componentHeader>
				<tui:componentName>
					<!-- The label is defined in DemoComplexForm class. -->
					<fmt:message key="${viewData.formLabel}"/>
				</tui:componentName>
			</tui:componentHeader>

			<tui:component>
				<tui:componentForm>

					<ui:row>
						<ui:formElement id="beastSelection">
							<ui:cell style="width: 30%"/>
							<ui:cell styleClass="name" style="width: 20%">
								<ui:label styleClass="nowrap"/>
							</ui:cell>
							<ui:cell styleClass="inpt">
								<ui:select localizeDisplayItems="true"/>
							</ui:cell>
						</ui:formElement>
					</ui:row>

					<c:if test="${not empty form.elements['concreteBeastControl']}">
						<ui:row>
							<ui:cell styleClass="wrap-centered">
								<ui:textDisplay id="selectedBeastDesc" localizeText="true"/>
							</ui:cell>
		
							<ui:formElement id="concreteBeastControl">
								<ui:cell styleClass="name">
									<ui:label />
								</ui:cell>
								<ui:cell>
									<!-- Render MultiSelectControl with checkboxes. (Also, instead ui:multiSelect could be used.) -->
									<ui:checkboxMultiSelect type="vertical" localizeDisplayItems="true" />
								</ui:cell>
							</ui:formElement>
						</ui:row>
					</c:if>

				</tui:componentForm>
			</tui:component>

		</ui:form>
	</ui:widgetContext>

</jsp:root>
