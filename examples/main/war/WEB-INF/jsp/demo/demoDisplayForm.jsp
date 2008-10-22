<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt" version="2.0">
	<ui:widgetContext>
		<ui:form id="displayForm">

			<!-- Label -->
			<tui:componentHeader>
				<tui:componentName>Demo display form</tui:componentName>
			</tui:componentHeader>

			<tui:component>

				<!-- Body -->
				<tui:componentForm>

					<ui:row>
						<ui:cell styleClass="name">
							<ui:label id="condDisplay" />
						</ui:cell>

						<ui:cell styleClass="data">
							<ui:conditionalDisplay id="condDisplay">
								<ui:conditionTrue>
									<tui:image code="triangle" />
								</ui:conditionTrue>
							</ui:conditionalDisplay>
						</ui:cell>
					</ui:row>

					<ui:row>
						<ui:cell styleClass="name">
							<ui:label id="textDisplay" />
						</ui:cell>

						<ui:cell styleClass="data">
							<ui:textDisplay id="textDisplay" />
						</ui:cell>
					</ui:row>

					<ui:row>
						<ui:cell styleClass="name">
							<ui:label id="valueDisplay" />
						</ui:cell>

						<ui:cell styleClass="data">
							<ui:valueDisplay id="valueDisplay" var="value">
								<c:out value="${value * 3}" />%
								</ui:valueDisplay>
						</ui:cell>
					</ui:row>

				</tui:componentForm>

			</tui:component>

		</ui:form>
	</ui:widgetContext>
</jsp:root>
