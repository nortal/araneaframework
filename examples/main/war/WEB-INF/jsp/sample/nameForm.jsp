<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.0">

	<!-- Component starts here -->
	<ui:widgetContext>

		<ui:form id="form">

			<tui:componentHeader>
				<tui:componentName>What are you, stranger?</tui:componentName>
			</tui:componentHeader>

			<tui:component>
				<tui:componentForm rowClasses="cols4">
					<ui:formElement id="name">
						<ui:row>
							<ui:cell styleClass="name">
								<ui:label/>
							</ui:cell>
	
							<ui:cell styleClass="inpt">
								<ui:textInput/>
							</ui:cell>
						</ui:row>
					</ui:formElement>
				</tui:componentForm>

				<!-- template design tag -->
				<tui:componentActions>
					<ui:eventButton eventId="return" labelId="#confirm"/>
				</tui:componentActions>
			</tui:component>

		</ui:form>

	</ui:widgetContext>
</jsp:root>
