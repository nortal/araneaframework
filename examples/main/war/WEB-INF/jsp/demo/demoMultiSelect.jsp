<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt" version="1.2">
	<ui:widgetContext>
		<ui:componentHeader>
			<ui:componentName>MultiSelectDemo</ui:componentName>
		</ui:componentHeader>
		
		<ui:form id="form">
			<ui:component>

				<ui:componentForm>
					<ui:row>
						<ui:formElement id="multiselect">
							<ui:cell>
								<ui:label/>
							</ui:cell>
							<ui:cell>
								<ui:multiSelect/>
							</ui:cell>
						</ui:formElement>
					</ui:row>
				</ui:componentForm>
				
				<ui:componentActions>
					<ui:eventButton eventId="test" labelId="#Test"/>
				</ui:componentActions>

			</ui:component>
		</ui:form>
	</ui:widgetContext>
</jsp:root>
