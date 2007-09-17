<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<!-- Component starts here -->
	<ui:widgetContext>
	
		<ui:widgetMarker id="simpleForm">
		<ui:form id="simpleForm">

			<tui:componentHeader>
				<tui:componentName><fmt:message key="context.menu.compheader"/></tui:componentName>
			</tui:componentHeader>

			<tui:component>

				<p>
					<fmt:message key="context.menu.intro"/>
			    </p>
			    
			    <p>
			    	
			    </p>
		
			    <br/>

				<tui:componentForm rowClasses="cols4" cellClasses="name, inpt">

					<ui:row>

						<ui:cell>

							<ui:label id="checkbox1" />
						</ui:cell>

						<ui:cell>

							<ui:formElement id="checkbox1">
								<ui:checkbox/>
							</ui:formElement>
						</ui:cell>

						<ui:cell>
							<ui:label id="textbox1" />
						</ui:cell>

						<ui:cell>
							<ui:textInput id="textbox1"/>
						</ui:cell>
					</ui:row>

					<ui:row>
						<ui:formElement id="dateTime">
							<ui:cell>
								<ui:label />
							</ui:cell>

							<ui:cell>
   
								<ui:dateTimeInput/>
							</ui:cell>
						</ui:formElement>

						<ui:formElement id="time">
							<ui:cell>
								<ui:label />
							</ui:cell>

							<ui:cell>
								<ui:timeInput />
							</ui:cell>
						</ui:formElement>
					</ui:row>

					<ui:row>
						<ui:formElement id="date">
							<ui:cell>
								<ui:label />
							</ui:cell>

							<ui:cell>
								<ui:dateInput />
							</ui:cell>
						</ui:formElement>

						<ui:formElement id="number">
							<ui:cell>
								<ui:label />
							</ui:cell>

							<ui:cell>
								<ui:floatInput />
							</ui:cell>
						</ui:formElement>
					</ui:row>

				</tui:componentForm>

				<!-- pure design tag -->
				<tui:componentActions>
					<ui:formElement id="button">
						<ui:button/>
					</ui:formElement>
				</tui:componentActions>
			</tui:component>

			<ui:contextMenu id="simpleForm.ctxMenu"/>

		</ui:form>
		</ui:widgetMarker>

	</ui:widgetContext>
</jsp:root>
