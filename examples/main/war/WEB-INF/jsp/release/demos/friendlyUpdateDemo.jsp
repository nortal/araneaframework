<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<!-- Component starts here -->
	<ui:widgetContext>
		

			<tui:componentHeader>
				<tui:componentName><fmt:message key="ufriendly.component.title"/></tui:componentName>
			</tui:componentHeader>

			<!-- Another custom template tag, purely design-focused (look ComponentTag for source)-->
			<tui:component>
				  <p>
				<fmt:message key="ufriendly.component.intro"/>
		    	</p>

				<ui:form id="companyForm">
				<tui:componentForm rowClasses="cols4" cellClasses="name, inpt">
					<ui:updateRegionRows id="compFormRegion">
					<ui:row>
						<ui:formElement id="arkNumber">
							<ui:cell>
								<ui:label />
							</ui:cell>

							<ui:cell>
								<ui:numberInput/>
							</ui:cell>
						</ui:formElement>
						
						<ui:formElement id="bankAccount">
							<ui:cell>
								<ui:label />
							</ui:cell>

							<ui:cell>
								<ui:textInput />
							</ui:cell>
						</ui:formElement>

					</ui:row>

					<ui:row>
						<ui:formElement id="registryAddress">
							<ui:cell>
								<ui:label />
							</ui:cell>

							<ui:cell>
								<ui:textInput />
							</ui:cell>
						</ui:formElement>

						<ui:formElement id="postalAddress">
							<ui:cell>
								<ui:label />
							</ui:cell>

							<ui:cell>
								<ui:textInput />
							</ui:cell>
						</ui:formElement>

					</ui:row>
					
					<ui:row>
						<ui:formElement id="vatNumber">
							<ui:cell>
								<ui:label />
							</ui:cell>

							<ui:cell>
								<ui:numberInput />
							</ui:cell>
						</ui:formElement>

						<ui:formElement id="firmType">
							<ui:cell>
								<ui:label />
							</ui:cell>

							<ui:cell>
								<ui:select />
							</ui:cell>
						</ui:formElement>

					</ui:row>
					</ui:updateRegionRows>
				</tui:componentForm>
				</ui:form>
				
				<!-- pure design tag -->
				<tui:componentActions>
					<ui:eventButton eventId="fetchData" labelId="ufriendly.component.getdata" updateRegions="compFormRegion"/>
				</tui:componentActions>
				
				<ui:form id="invoiceForm">
					<tui:componentForm rowClasses="cols4" cellClasses="name, inpt">
					<ui:row>
						<ui:formElement id="id">
							<ui:cell>
								<ui:label />
							</ui:cell>

							<ui:cell>
								<ui:textInput/>
							</ui:cell>
						</ui:formElement>
						
						<ui:formElement id="date">
							<ui:cell>
								<ui:label />
							</ui:cell>

							<ui:cell>
								<ui:dateInput />
							</ui:cell>
						</ui:formElement>
					</ui:row>
					
					<ui:row>
						<ui:formElement id="sum">
							<ui:cell>
								<ui:label />
							</ui:cell>

							<ui:cell colspan="3">
								<ui:floatInput />
							</ui:cell>
						</ui:formElement>
					</ui:row>
					
					</tui:componentForm>
				</ui:form>
				
			</tui:component>

	</ui:widgetContext>
</jsp:root>
