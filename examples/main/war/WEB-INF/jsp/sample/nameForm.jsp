<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<!-- Component starts here -->
	<ui:widgetContext>

		<ui:form id="form">

			<ui:componentHeader>
				<ui:componentName>What are you, stranger?</ui:componentName>
			</ui:componentHeader>

			<ui:component>
				<ui:componentForm rowClasses="cols4">
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
				</ui:componentForm>

				<!-- template design tag -->
				<ui:componentActions>
					<ui:eventButton eventId="return" labelId="#confirm"/>
				</ui:componentActions>
			</ui:component>

		</ui:form>

	</ui:widgetContext>
</jsp:root>
