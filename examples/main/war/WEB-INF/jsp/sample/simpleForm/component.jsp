<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt" 
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<!-- Component starts here -->
	<ui:widgetContext>

		<ui:form id="simpleForm">
		<ui:exampleContainerHeader>Tutorial first form</ui:exampleContainerHeader>

 		 <ui:container>

				<!-- Body -->
				<ui:containerFormBody>
					<ui:row>
						<ui:cell styleClass="aranea-label">
							<ui:label id="checkbox1" />
						</ui:cell>

						<ui:cell>
							<ui:formElement id="checkbox1">
								<ui:checkbox id="checkbox1" />
							</ui:formElement>
						</ui:cell>
					</ui:row>

					<ui:row>
						<ui:cell styleClass="aranea-label">
							<ui:label id="textbox1" />
						</ui:cell>

						<ui:cell>
							<ui:textInput id="textbox1" styleClass="aranea-short-text-input"/>
						</ui:cell>
					</ui:row>
				</ui:containerFormBody>

				<!-- Footer -->
				<ui:containerFooter>
					<ui:eventButton id="button1" eventId="testSimpleForm" labelId="#Button"/>
				</ui:containerFooter>
			</ui:container>

		</ui:form>

	</ui:widgetContext>
</jsp:root>
