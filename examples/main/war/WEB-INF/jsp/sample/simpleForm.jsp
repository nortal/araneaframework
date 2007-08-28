<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<!-- Component starts here -->
	<ui:widgetContext>

        <!-- Set the form context, the form we will be rendering here. 
            We cannot as simply (using non-qualified names) refer to form 
            elements unless we define the form context here. -->
		<ui:form id="simpleForm">

			<!-- Now, these are the first custom tags in template application. They do
			     nothing particularly interesting, just set up the HTML DIV element
			     containing the component header -->
			<tui:componentHeader>
				<tui:componentName>Tutorial first form</tui:componentName>
			</tui:componentHeader>

			<!-- Another custom template tag, purely design-focused (look ComponentTag for source)-->
			<tui:component>
			
				<!-- Custom tag, but more interesting that previous tags. It derives from
                     LayoutHtmlTag tag and allows putting row tags inside of it. 
                     Attribute rowClasses defines the styleClass attribute for rows inserted under 
                     componentForm here, cellClasses does the same for cells. These classes are repeating
                     e.g first cell in a row is with styleClass "name", second with "inpt", and third
                     cell again has styleClass "name". In HTML, this tag creates a TABLE. -->
				<tui:componentForm rowClasses="cols4" cellClasses="name, inpt">

					<!-- As we can insert rows now, we do just that. -->
					<ui:row>
						<!-- ... we can insert cells too! As we defined componentForm rowClass 
							to be cols4  we should insert 4 cells here... -->
						<ui:cell>
							<!-- label is formelement centric tag. As we are inside the form 
							already, we provide it with form element id and corresponding 
							form element label will be shown -->
							<ui:label id="checkbox1" />
						</ui:cell>

						<ui:cell>
							<!-- This is another approach to providing information about
								form element that tag should apply to - instead providing
								form element id to every single tag, tags could be enclosed
								inside single formElement tag.
							 -->
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

	</ui:widgetContext>
</jsp:root>
