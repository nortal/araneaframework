<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.1">

	<!-- Component starts here -->
	<ui:widgetContext>
		<ui:form id="searchForm">

			<tui:componentHeader>
				<tui:componentName>Tutorial search form</tui:componentName>
			</tui:componentHeader>

			<tui:component>
				<!-- Body -->
				<tui:componentForm>
					<ui:row>
						<ui:cell colspan="2">Client</ui:cell>
					</ui:row>

					<ui:formElement id="clientFirstName">
						<ui:row>
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>
							<ui:cell styleClass="inpt">
								<ui:textInput />
							</ui:cell>
						</ui:row>
					</ui:formElement>

					<ui:formElement id="clientLastName">
						<ui:row>
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>
							<ui:cell styleClass="inpt">
								<ui:textInput />
							</ui:cell>
						</ui:row>
					</ui:formElement>

					<ui:row>
						<ui:cell colspan="2">Client personal Id</ui:cell>
					</ui:row>

					<ui:formElement id="clientPersonalId">
						<ui:row>
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>
							<ui:cell styleClass="inpt">
								<ui:textInput />
							</ui:cell>
						</ui:row>
					</ui:formElement>

					<ui:row>
						<ui:cell colspan="2">Address</ui:cell>
					</ui:row>

					<ui:formElement id="clientAddressTown">
						<ui:row>
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>
							<ui:cell styleClass="inpt">
								<ui:textInput />
							</ui:cell>
						</ui:row>
					</ui:formElement>

					<ui:formElement id="clientAddressStreet">
						<ui:row>
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>
							<ui:cell styleClass="inpt">
								<ui:textInput />
							</ui:cell>
						</ui:row>
					</ui:formElement>

					<ui:formElement id="clientAddressHouse">
						<ui:row>
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>
							<ui:cell styleClass="inpt">
								<ui:textInput />
							</ui:cell>
						</ui:row>
					</ui:formElement>
				</tui:componentForm>

				<!-- Footer -->
				<tui:componentActions>
					<ui:eventButton id="search" eventId="search" labelId="#Search"/>
				</tui:componentActions>
			</tui:component>

		</ui:form>
	</ui:widgetContext>
</jsp:root>
