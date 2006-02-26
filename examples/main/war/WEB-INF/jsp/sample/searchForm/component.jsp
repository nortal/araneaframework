<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<!-- Component starts here -->
	<ui:widgetContext>
		<ui:form id="searchForm">
			<ui:container>
			
				<!-- Label -->
				<ui:containerLabel>Tutorial search form</ui:containerLabel>

				<!-- Body -->
				<ui:containerFormBody>
					<ui:row>
						<ui:cell colSpan="2">Client</ui:cell>
					</ui:row>
					<ui:formElement id="clientFirstName">
						<ui:row>
							<ui:cell styleClass="label">
								<ui:label />
							</ui:cell>
							<ui:cell>
								<ui:textInput />
							</ui:cell>
						</ui:row>
					</ui:formElement>
					<ui:formElement id="clientLastName">
						<ui:row>
							<ui:cell styleClass="label">
								<ui:label />
							</ui:cell>
							<ui:cell>
								<ui:textInput />
							</ui:cell>
						</ui:row>
					</ui:formElement>
					<ui:formElement id="clientPersonalId">
						<ui:row>
							<ui:cell styleClass="label">
								<ui:label />
							</ui:cell>
							<ui:cell>
								<ui:textInput />
							</ui:cell>
						</ui:row>
					</ui:formElement>
					<ui:row>
						<ui:cell colSpan="2">Address</ui:cell>
					</ui:row>
					<ui:formElement id="clientAddressTown">
						<ui:row>
							<ui:cell styleClass="label">
								<ui:label />
							</ui:cell>
							<ui:cell>
								<ui:textInput />
							</ui:cell>
						</ui:row>
					</ui:formElement>
					<ui:formElement id="clientAddressStreet">
						<ui:row>
							<ui:cell styleClass="label">
								<ui:label />
							</ui:cell>
							<ui:cell>
								<ui:textInput />
							</ui:cell>
						</ui:row>
					</ui:formElement>
					<ui:formElement id="clientAddressHouse">
						<ui:row>
							<ui:cell styleClass="label">
								<ui:label />
							</ui:cell>
							<ui:cell>
								<ui:textInput />
							</ui:cell>
						</ui:row>
					</ui:formElement>
				</ui:containerFormBody>
				<!-- Footer -->
				<ui:containerFooter>
					<ui:eventButton id="search" eventId="search" labelId="#Search"/>
				</ui:containerFooter>
			</ui:container>
		</ui:form>
	</ui:widgetContext>
</jsp:root>
