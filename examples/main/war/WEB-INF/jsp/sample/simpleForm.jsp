<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt" 
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<!-- Component starts here -->
	<ui:widgetContext>

		<ui:form id="simpleForm">

			<ui:componentHeader>
				<ui:componentName>Tutorial first form</ui:componentName>
			</ui:componentHeader>

			<ui:component>
				<ui:componentForm rowClass="cols4">

					<ui:row>
						<ui:cell styleClass="name">
							<ui:label id="checkbox1" />
						</ui:cell>

						<ui:cell styleClass="inpt">
							<ui:formElement id="checkbox1">
								<ui:checkbox id="checkbox1" />
							</ui:formElement>
						</ui:cell>

						<ui:cell styleClass="name">
							<ui:label id="textbox1" />
						</ui:cell>

						<ui:cell styleClass="inpt">
							<ui:textInput id="textbox1"/>
						</ui:cell>
					</ui:row>
					
					<ui:row>
						<ui:formElement id="dateTime">
							<ui:cell styleClass="name">
								<ui:label/>
							</ui:cell>
	
							<ui:cell styleClass="inpt">
								<ui:dateTimeInput />
							</ui:cell>
						</ui:formElement>
						
						<ui:formElement id="time">
							<ui:cell styleClass="name">
								<ui:label/>
							</ui:cell>
	
							<ui:cell styleClass="inpt">
								<ui:timeInput />
							</ui:cell>
						</ui:formElement>
					</ui:row>
					
					<ui:row>
						<ui:formElement id="date">
							<ui:cell styleClass="name">
								<ui:label/>
							</ui:cell>
	
							<ui:cell styleClass="inpt">
								<ui:dateInput />
							</ui:cell>
						</ui:formElement>
					</ui:row>
					

				</ui:componentForm>
				
				<ui:componentActions>
					<ui:eventButton id="button1" eventId="testSimpleForm" labelId="#Button" />
				</ui:componentActions>
			</ui:component>

		</ui:form>

	</ui:widgetContext>
</jsp:root>
