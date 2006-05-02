<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<!-- Component starts here -->
	<ui:widgetContext>
		<ui:form id="searchForm">

			<ui:componentHeader>
				<ui:componentName>Tutorial search form</ui:componentName>
			</ui:componentHeader>

			<ui:component>
				<!-- Body -->
				<ui:componentForm>
					<ui:newRow>
						<ui:newCell colspan="2">Client</ui:newCell>
					</ui:newRow>

					<ui:formElement id="clientFirstName">
						<ui:newRow>
							<ui:newCell styleClass="name">
								<ui:label />
							</ui:newCell>
							<ui:newCell styleClass="inpt">
								<ui:textInput />
							</ui:newCell>
						</ui:newRow>
					</ui:formElement>

					<ui:formElement id="clientLastName">
						<ui:newRow>
							<ui:newCell styleClass="name">
								<ui:label />
							</ui:newCell>
							<ui:newCell styleClass="inpt">
								<ui:textInput />
							</ui:newCell>
						</ui:newRow>
					</ui:formElement>

					<ui:formElement id="clientPersonalId">
						<ui:newRow>
							<ui:newCell styleClass="name">
								<ui:label />
							</ui:newCell>
							<ui:newCell styleClass="inpt">
								<ui:textInput />
							</ui:newCell>
						</ui:newRow>
					</ui:formElement>

					<ui:newRow>
						<ui:newCell colspan="2">Address</ui:newCell>
					</ui:newRow>

					<ui:formElement id="clientAddressTown">
						<ui:newRow>
							<ui:newCell styleClass="name">
								<ui:label />
							</ui:newCell>
							<ui:newCell styleClass="inpt">
								<ui:textInput />
							</ui:newCell>
						</ui:newRow>
					</ui:formElement>

					<ui:formElement id="clientAddressStreet">
						<ui:newRow>
							<ui:newCell styleClass="name">
								<ui:label />
							</ui:newCell>
							<ui:newCell styleClass="inpt">
								<ui:textInput />
							</ui:newCell>
						</ui:newRow>
					</ui:formElement>

					<ui:formElement id="clientAddressHouse">
						<ui:newRow>
							<ui:newCell styleClass="name">
								<ui:label />
							</ui:newCell>
							<ui:newCell styleClass="inpt">
								<ui:textInput />
							</ui:newCell>
						</ui:newRow>
					</ui:formElement>
				</ui:componentForm>

				<!-- Footer -->
				<ui:componentActions>
					<ui:eventButton id="search" eventId="search" labelId="#Search"/>
				</ui:componentActions>
			</ui:component>

		</ui:form>
	</ui:widgetContext>
</jsp:root>
