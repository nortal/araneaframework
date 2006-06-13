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
		<ui:form id="testform">

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
				<ui:componentForm>

					<!-- As we can insert rows now, we just do that. -->
					<ui:row>
						<ui:formElement id="autocompletedTextBox1">
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>

							<ui:cell styleClass="inpt">
								<ui:autoCompleteTextInput />
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
