<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	version="1.2">
	<ui:widgetContext>
		<ui:form id="form">

			<tui:componentHeader>
				<tui:componentName><fmt:message key="ComboTextInput"/></tui:componentName>
			</tui:componentHeader>

			<tui:component>
				<p>
					<fmt:message key="combo.intro"/>
				</p>

				<tui:componentForm rowClasses="cols4" cellClasses="name, inpt">
					<ui:row>

							<ui:formElement id="sex">
								<ui:cell>
									<ui:label/>
								</ui:cell>
		
								<ui:cell styleClass="data">
									<ui:textInputDisplay/>
								</ui:cell>
							</ui:formElement>
							
							<ui:formElement id="country">
								<ui:cell>
									<ui:label/>
								</ui:cell>
		
								<ui:cell styleClass="data">
									<ui:textInputDisplay/>
								</ui:cell>
							</ui:formElement>
							
					</ui:row>
						
					<ui:row>
							<ui:formElement id="forename">
								<ui:cell>
									<ui:label/>
								</ui:cell>
		
								<ui:cell styleClass="data">
									<ui:textInputDisplay/>
								</ui:cell>
							</ui:formElement>
					
							<ui:formElement id="surname">
								<ui:cell>
									<ui:label/>
								</ui:cell>
		
								<ui:cell styleClass="data">
									<ui:textInputDisplay/>
								</ui:cell>
							</ui:formElement>
					</ui:row>

				</tui:componentForm>

				<!-- pure design tag -->
				<tui:componentActions>
					<ui:eventButton eventId="return" labelId="button.back"/>
				</tui:componentActions>
			</tui:component>

		</ui:form>

	</ui:widgetContext>
</jsp:root>
