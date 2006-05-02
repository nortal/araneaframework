<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt" version="1.2">
	<ui:widgetContext>
		<ui:form id="displayForm">

			<!-- Label -->
			<ui:componentHeader>
				<ui:componentName>Demo display form</ui:componentName>
			</ui:componentHeader>

			<ui:component>

				<!-- Body -->
				<ui:componentForm>

					<ui:newRow>
						<ui:newCell styleClass="name">
							<ui:label id="condDisplay" />
						</ui:newCell>

						<ui:newCell styleClass="data">
							<ui:conditionalDisplay id="condDisplay">
								<ui:conditionTrue>
									<ui:image code="triangle" />
								</ui:conditionTrue>
							</ui:conditionalDisplay>
						</ui:newCell>
					</ui:newRow>

					<ui:newRow>
						<ui:newCell styleClass="name">
							<ui:label id="textDisplay" />
						</ui:newCell>

						<ui:newCell styleClass="data">
							<ui:textDisplay id="textDisplay" />
						</ui:newCell>
					</ui:newRow>

					<ui:newRow>
						<ui:newCell styleClass="name">
							<ui:label id="valueDisplay" />
						</ui:newCell>

						<ui:newCell styleClass="data">
							<ui:valueDisplay id="valueDisplay" var="value">
								<c:out value="${value * 3}" />%
								</ui:valueDisplay>
						</ui:newCell>
					</ui:newRow>

				</ui:componentForm>

			</ui:component>

		</ui:form>
	</ui:widgetContext>
</jsp:root>
