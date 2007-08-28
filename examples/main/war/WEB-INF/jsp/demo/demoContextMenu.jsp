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
				<tui:componentName>Widget with context menu</tui:componentName>
			</tui:componentHeader>

			<tui:component>

				<tui:componentForm rowClasses="cols4" cellClasses="name, inpt">

					<!-- As we can insert rows now, we do just that. -->
					<ui:row>
						<!-- ... we can insert cells too! As we defined componentForm rowClass 
							to be cols4  we should insert 4 cells here... -->
						<ui:cell>

							<ui:label id="checkbox1" />
						</ui:cell>

						<ui:cell>

							<ui:formElement id="checkbox1">
								<!-- will draw a checkbox tied to form element with id "checkbox1" -->
								<ui:checkbox/>
							</ui:formElement>
						</ui:cell>

						<ui:cell>
							<ui:label id="textbox1" />
						</ui:cell>

						<ui:cell>
							<!-- As "textbox1" is TextControl, we choose the corresponding tag to render it -->
							<ui:textInput id="textbox1"/>
						</ui:cell>
					</ui:row>

					<!-- another row, and we just keep on going until all form elements have been described. -->
					<ui:row>
						<ui:formElement id="dateTime">
							<ui:cell>
							    <!-- "dateTime" label -->
								<ui:label />
							</ui:cell>

							<ui:cell>
          						<!-- "dateTime" input field -->
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
