<?xml version="1.0" encoding="UTF-8"?>
<jsp:root 
	xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:ui="http://araneaframework.org/tag-library/template"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt" version="1.2">
	<ui:widgetContext>
		<ui:form id="displayForm">

			<ui:container>

				<!-- Label -->
				<ui:containerLabel>
						Demo display form
				</ui:containerLabel>

				<!-- Body -->
				<ui:containerFormBody>

					<ui:row>
						<ui:cell styleClass="label">
							<ui:label id="condDisplay" />
						</ui:cell>

						<ui:cell>
							<ui:conditionalDisplay id="condDisplay">
								<ui:conditionTrue>
									<ui:image code="triangle"/>
								</ui:conditionTrue>
							</ui:conditionalDisplay>
						</ui:cell>
					</ui:row>

					<ui:row>
						<ui:cell styleClass="label">
							<ui:label id="textDisplay" />
						</ui:cell>

						<ui:cell>
							<ui:textDisplay id="textDisplay" />
						</ui:cell>
					</ui:row>

					<ui:row>
						<ui:cell styleClass="label">
							<ui:label id="valueDisplay" />
						</ui:cell>

						<ui:cell>
							<ui:valueDisplay id="valueDisplay" var="value">
								<c:out value="${value * 3}" />%
								</ui:valueDisplay>
						</ui:cell>
					</ui:row>

				</ui:containerFormBody>
				
			</ui:container>

		</ui:form>
	</ui:widgetContext>
</jsp:root>
