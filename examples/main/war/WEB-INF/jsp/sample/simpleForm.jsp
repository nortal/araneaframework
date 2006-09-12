<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<!-- Component starts here -->
	<ui:widgetContext>

		<!-- Set the form context, the form we will be rendering here. 
			We cannot as simply refer to form elements unless we define it here. -->
		<ui:form id="simpleForm">

			<!-- Now, these are the first custom tags in template application. They do
			     nothing particularly interesting, just set up the HTML DIV element
			     containing the component header -->
			<ui:componentHeader>
				<ui:componentName>Tutorial first form</ui:componentName>
			</ui:componentHeader>

			<!-- Another custom template tag, purely design-focused (look ComponentTag for source)-->
			<ui:component>
			
				<!-- Custom tag, but more interesting that previous tags. It derives from
                     UiLayoutBaseTag tag, it gives one ability to put row tags inside of it. 
                     Attribute rowClasses defines the styleClass attribute for rows inserted under 
                     componentForm here, cellclasses does the same for cells. In HTML, this tag creates a TABLE. -->
				<ui:componentForm rowClasses="cols4" cellClasses="name, inpt">

					<ui:updateRegionRows id="damnRegion">
					<!-- As we can insert rows now, we just do that. -->
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
					</ui:updateRegionRows>

					<!-- another row, and we just keep on going until all form elements have been described. -->
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
								<ui:floatInput updateRegions="damnRegion"/>
							</ui:cell>
						</ui:formElement>
					</ui:row>

				</ui:componentForm>

				<!-- template design tag -->
				<ui:componentActions>
					<ui:formElement id="button">
						<ui:button/>
					</ui:formElement>
				</ui:componentActions>
			</ui:component>

		</ui:form>

	</ui:widgetContext>
</jsp:root>
