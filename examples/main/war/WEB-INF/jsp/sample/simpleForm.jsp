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
						<ui:cell colspan="2"/>

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

		</ui:form>

	</ui:widgetContext>
</jsp:root>
