<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	version="1.2">
	<ui:widgetContext>
		<ui:form id="form">

			<tui:componentHeader>
				<tui:componentName>Combo text input</tui:componentName>
			</tui:componentHeader>

			<tui:component>
			
				<tui:componentForm rowClasses="cols4" cellClasses="name, inpt">
					<ui:row>
						<ui:cell />
						
						<ui:formElement id="comboInput">
							<ui:cell>
								<ui:label/>
							</ui:cell>
	
							<ui:cell>
								<ui:comboTextInput/>
							</ui:cell>
						</ui:formElement>

						<ui:cell />
					</ui:row>

				</tui:componentForm>

				<!-- pure design tag -->
				<tui:componentActions>
					<ui:eventButton eventId="submit" labelId="#Submit"/>
				</tui:componentActions>
			</tui:component>

		</ui:form>

	</ui:widgetContext>
</jsp:root>
