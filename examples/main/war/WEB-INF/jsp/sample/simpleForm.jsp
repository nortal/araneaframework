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
                     Attribute rowClass defines the styleClass attribute for rows inserted under 
                     componentForm here. In HTML, this tag creates a TABLE. -->
				<ui:componentForm rowClasses="cols4">

					<!-- As we can insert rows now, we just do that. -->
					<ui:row>
						<!-- ... we can insert cells too! As we defined componentForm rowClass 
							to be cols4  we should insert 4 cells here... -->
						<ui:cell styleClass="name">
							<!-- label is formelement centric tag. As we are inside the form 
							already, we provide it with form element id and corresponding 
							form element label will be shown -->
							<ui:label id="checkbox1" />
						</ui:cell>

						<ui:cell styleClass="inpt">
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

						<ui:cell styleClass="name">
							<ui:label id="textbox1" />
						</ui:cell>

						<ui:cell styleClass="inpt">
							<!-- As "textbox1" is TextControl, we choose the corresponding tag to render it -->
							<ui:textInput id="textbox1">
								<ui:attribute name="onfocus" value="function() {alert('woot');}"/>
							</ui:textInput>
						</ui:cell>
					</ui:row>

					<!-- another row, and we just keep on going until all form elements have been described. -->
					<ui:row>
						<ui:formElement id="dateTime">
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>

							<ui:cell styleClass="inpt">
								<ui:dateTimeInput/>
							</ui:cell>
						</ui:formElement>

						<ui:formElement id="time">
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>

							<ui:cell styleClass="inpt">
								<ui:timeInput />
							</ui:cell>
						</ui:formElement>
					</ui:row>

					<ui:row>
						<ui:formElement id="date">
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>

							<ui:cell styleClass="inpt">
								<ui:dateInput />
							</ui:cell>
						</ui:formElement>

						<ui:formElement id="number">
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>

							<ui:cell styleClass="inpt">
								<ui:floatInput/>
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
