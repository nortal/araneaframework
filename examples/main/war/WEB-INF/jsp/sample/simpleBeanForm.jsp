<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.0">
	<ui:widgetContext>

		<ui:form id="simpleForm">

			<tui:componentHeader>
				<tui:componentName><fmt:message key="demo.beanForm.tab"/></tui:componentName>
			</tui:componentHeader>

			<tui:component>

				<tui:componentForm rowClasses="cols4" cellClasses="name, inpt">

					<ui:row>
						<ui:cell styleClass="data" colspan="4">
							<fmt:message key="demo.beanForm.intro"/>
						</ui:cell>
					</ui:row>

					<ui:row>
						<ui:cell>
							<ui:label id="caseSensitive" />
						</ui:cell>

						<ui:cell>
							<ui:checkbox id="caseSensitive" />
						</ui:cell>

						<ui:cell>
							<ui:label id="searchString" />
						</ui:cell>

						<ui:cell>
							<ui:textInput id="searchString" />
						</ui:cell>
					</ui:row>

					<ui:row>
						<ui:cell>
							<ui:label id="createdDateTime"/>
						</ui:cell>

						<ui:cell>
							<ui:dateTimeInput id="createdDateTime"/>
						</ui:cell>

						<ui:cell>
							<ui:label id="createdTime"/>
						</ui:cell>

						<ui:cell>
							<ui:timeInput id="createdTime"/>
						</ui:cell>
					</ui:row>

					<ui:row>
						<ui:cell>
							<ui:label id="createdDate"/>
						</ui:cell>

						<ui:cell>
							<ui:dateInput id="createdDate"/>
						</ui:cell>

						<ui:cell>
							<ui:label id="length"/>
						</ui:cell>

						<ui:cell>
							<ui:floatInput id="length"/>
						</ui:cell>
					</ui:row>

					<ui:row>
						<ui:cell>
							<ui:label id="siblingsCount"/>
						</ui:cell>

						<ui:cell>
							<ui:numberInput id="siblingsCount"/>
						</ui:cell>

						<ui:cell>
							<ui:label id="peopleCount"/>
						</ui:cell>

						<ui:cell>
							<ui:numberInput id="peopleCount"/>
						</ui:cell>
					</ui:row>

					<ui:row>
						<ui:cell>
							<ui:label id="weight"/>
						</ui:cell>

						<ui:cell>
							<ui:floatInput id="weight"/>
						</ui:cell>

						<ui:cell>
							<ui:label id="preciseWeight"/>
						</ui:cell>

						<ui:cell>
							<ui:floatInput id="preciseWeight"/>
						</ui:cell>
					</ui:row>

				</tui:componentForm>

				<!-- pure design tag -->
				<tui:componentActions>
					<ui:eventButton eventId="testSimpleBeanForm" labelId="button.submit"/>
				</tui:componentActions>
			</tui:component>

		</ui:form>

	</ui:widgetContext>
</jsp:root>
