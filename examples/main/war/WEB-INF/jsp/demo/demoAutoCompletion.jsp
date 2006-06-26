<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<!-- Component starts here -->
	<ui:widgetContext>

		<ui:form id="testform">

			<ui:componentHeader>
				<ui:componentName>Tutorial first form</ui:componentName>
			</ui:componentHeader>

			<!-- Another custom template tag, purely design-focused (look ComponentTag for source)-->
			<ui:component>
			
				<ui:componentForm>

					<!-- As we can insert rows now, we just do that. -->
					<ui:row>
						<ui:formElement id="acinput">
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>

							<ui:cell styleClass="inpt">
								<ui:autoCompleteTextInput />
							</ui:cell>
						</ui:formElement>
					</ui:row>


	    		</ui:componentForm>

				<ui:componentActions>
					<ui:eventButton eventId="test"/>
				</ui:componentActions>
			</ui:component>

		</ui:form>

	</ui:widgetContext>
</jsp:root>
