<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<!-- Component starts here -->
	<ui:widgetContext>
		<ui:form id="form">

			<ui:componentHeader>
				<ui:componentName>Tutorial invisible element form</ui:componentName>
			</ui:componentHeader>

			<ui:component>
				<!-- Body -->
				<ui:componentForm>					
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
							<ui:cell colSpan="2"/>
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
							<ui:cell colSpan="2"/>			
						</ui:row>
					</ui:formElement>
				</ui:componentForm>

				<!-- Footer -->
				<ui:componentActions>
					<ui:eventButton eventId="test" labelId="#Refresh"/>
				</ui:componentActions>
			</ui:component>

		</ui:form>
	</ui:widgetContext>
</jsp:root>
