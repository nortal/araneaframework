<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.1">

	<ui:widgetContext>
		<ui:form id="listenerForm">

			<tui:componentHeader>
				<tui:componentName><fmt:message key="onChangeListener.http.title"/></tui:componentName>
			</tui:componentHeader>

			<tui:component>
				<tui:componentForm rowClasses="cols4" cellClasses="name, inpt">
					<ui:row>

						<ui:formElement id="dateTime1">
							<ui:cell>
								<ui:label />
							</ui:cell>
							<ui:cell>
								<ui:dateTimeInput/>
							</ui:cell>
						</ui:formElement>

						<ui:formElement id="time1">
							<ui:cell>
								<ui:label />
							</ui:cell>
							<ui:cell>
								<ui:timeInput/>
							</ui:cell>
						</ui:formElement>
					</ui:row>

					<ui:row>
						<ui:formElement id="date1">
							<ui:cell>
								<ui:label />
							</ui:cell>
							<ui:cell>
								<ui:dateInput />
							</ui:cell>
						</ui:formElement>

						<ui:formElement id="float1">
							<ui:cell>
								<ui:label />
							</ui:cell>
							<ui:cell>
								<ui:floatInput/>
							</ui:cell>
						</ui:formElement>

					</ui:row>
				</tui:componentForm>
			</tui:component>

			<tui:componentHeader>
				<tui:componentName><fmt:message key="onChangeListener.ajax.title"/></tui:componentName>
			</tui:componentHeader>

			<tui:component>
				<tui:componentForm rowClasses="cols4" cellClasses="name, inpt">
					<ui:updateRegionRows id="woot">
						<ui:row>

							<ui:formElement id="dateTime2">
								<ui:cell>
									<ui:label />
								</ui:cell>
								<ui:cell>
									<ui:dateTimeInput  updateRegions="woot"/>
								</ui:cell>
							</ui:formElement>

							<ui:formElement id="time2">
								<ui:cell>
									<ui:label />
								</ui:cell>
								<ui:cell>
									<ui:timeInput updateRegions="woot"/>
								</ui:cell>
							</ui:formElement>
						</ui:row>

						<ui:row>
							<ui:formElement id="date2">
								<ui:cell>
									<ui:label />
								</ui:cell>
								<ui:cell>
									<ui:dateInput updateRegions="woot"/>
								</ui:cell>
							</ui:formElement>

							<ui:formElement id="float2">
								<ui:cell>
									<ui:label />
								</ui:cell>
								<ui:cell>
									<ui:floatInput updateRegions="woot"/>
								</ui:cell>
							</ui:formElement>

						</ui:row>
					</ui:updateRegionRows>
				</tui:componentForm>
			</tui:component>

			<tui:componentHeader>
				<tui:componentName><fmt:message key="onChangeListener.action.title"/></tui:componentName>
			</tui:componentHeader>

			<tui:component>
				<tui:componentForm rowClasses="cols4" cellClasses="name, inpt">
					<ui:row>

						<ui:formElement id="suggestBox">
							<ui:cell colspan="2">
								<ui:label />
							</ui:cell>
							<ui:cell colspan="2">
								<ui:autoCompleteTextInput globalUpdateRegions="demo-messages"/>
							</ui:cell>
						</ui:formElement>

					</ui:row>
				</tui:componentForm>
			</tui:component>

		</ui:form>
	</ui:widgetContext>
</jsp:root>
