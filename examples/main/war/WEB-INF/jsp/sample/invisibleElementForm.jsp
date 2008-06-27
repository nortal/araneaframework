<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.0">

	<!-- Component starts here -->
	<ui:widgetContext>
		<ui:form id="form">

			<tui:componentHeader>
				<tui:componentName>Tutorial invisible element form</tui:componentName>
			</tui:componentHeader>

			<tui:component>
				<!-- Body -->
				<tui:componentForm>					
					<ui:formElement id="showTitle">
						<ui:row>
							<ui:cell styleClass="name">
								<ui:simpleLabel labelId="#Client"/>
							</ui:cell>
							<ui:cell styleClass="inpt">
								<ui:checkbox/>
							</ui:cell>			
							<ui:cell styleClass="name">
								<c:if test="${formElementValue}">				
									<ui:label id="title"/>
								</c:if>
							</ui:cell>
							<ui:cell styleClass="inpt">
								<c:if test="${formElementValue}">				
									<ui:textInput id="title"/>
								</c:if>																	
							</ui:cell>															
						</ui:row>
					</ui:formElement>

					<ui:formElement id="firstName">					
						<ui:row>
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>
							<ui:cell styleClass="inpt">
								<ui:textInput />
							</ui:cell>
							<ui:cell colspan="2"/>
						</ui:row>
					</ui:formElement>

					<ui:formElement id="lastName">
						<ui:row>
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>
							<ui:cell styleClass="inpt">
								<ui:textInput />
							</ui:cell>
							<ui:cell colspan="2"/>			
						</ui:row>
					</ui:formElement>
				</tui:componentForm>

				<!-- Footer -->
				<tui:componentActions>
					<ui:eventButton eventId="test" labelId="#Refresh"/>
				</tui:componentActions>
			</tui:component>

		</ui:form>
	</ui:widgetContext>
</jsp:root>
