<?xml version="1.0" encoding="UTF-8"?>
<jsp:root 
	xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/template" version="1.2">
	<ui:widgetContext>
		<ui:form id="nameForm">

			<ui:componentHeader>
				<ui:componentName>
					Name
				</ui:componentName>
			</ui:componentHeader>

			<ui:component>

				<ui:componentForm>
					<ui:row>
						<ui:formElement id="name">
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>
							<ui:cell>
								<ui:textInput />
							</ui:cell>
						</ui:formElement>
						
						<ui:cell styleClass="name">
						</ui:cell>
						<ui:cell>
						</ui:cell>
					</ui:row>
				</ui:componentForm>

				<ui:componentActions>
					<ui:eventButton labelId="#Hello" eventId="hello"/>
				</ui:componentActions>
			</ui:component>

		</ui:form>	
	</ui:widgetContext>
</jsp:root>