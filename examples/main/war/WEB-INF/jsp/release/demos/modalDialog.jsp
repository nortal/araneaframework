<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<!-- Component starts here -->
	<ui:widgetContext>
		<ui:form id="form">

			<tui:componentHeader>
				<tui:componentName>Modal dialog demo</tui:componentName>
			</tui:componentHeader>

			<!-- Another custom template tag, purely design-focused (look ComponentTag for source)-->
			<tui:component>
				 <p>
		      		There are times when one needs to prevent users from wandering around in parallel usecases, 
		      		shooting themselves in the foot at the go. Aranea provides a way to present all fully-functional Aranea 
		      		components in modal dialogs (aka <i>overlay</i>), allowing user to complete a parallel usecase or just respond
		      		to an alert. This avoids the need to open additional browser windows that are often blocked by modern 
		      		browsers or might just go unnoticed by user. 
			    </p>
			    
			    <p>
			    	Start another identical widget by clicking on a "Start next" or "Start next in overlay" buttons. Once
			    	the modal dialog (overlay mode) is activated, both buttons will act identically. When overlay completes (this 
			    	will happen when "Return to previous" button is used in activated overlay where there are no more flows
			    	that are running inside overlay), user is returned to main usecase.
			    </p>

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
					<ui:eventButton eventId="nextFlow" labelId="#Start next"/>
					<ui:eventButton eventId="nextFlowOverlay" labelId="#Start next in overlay"/>
					<ui:formElement id="button">
						<ui:button/>
					</ui:formElement>
					<c:if test="${widget.nested}">
						<ui:eventButton eventId="return" labelId="#Return to previous"/>
					</c:if>
				</tui:componentActions>
			</tui:component>

		</ui:form>

	</ui:widgetContext>
</jsp:root>
