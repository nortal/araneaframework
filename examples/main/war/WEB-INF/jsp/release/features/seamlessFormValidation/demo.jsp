<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core" 
	xmlns:fmt="http://java.sun.com/jstl/fmt" 
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.0">

	<ui:widgetContext>
		<ui:form id="form1">
			<p>
				<fmt:message key="seamless.intro"/>
			</p>
		
			<p>
				<fmt:message key="seamless.howto"/>
			</p>
		
			<tui:componentHeader>
				<tui:componentName><fmt:message key="seamless.withoutbg.form"/></tui:componentName>
		     </tui:componentHeader>
	
			<tui:component>
				<tui:componentForm rowClasses="cols4" cellClasses="name, inpt">
				<ui:row>
					<ui:formElement id="futureDate">
						<ui:cell>
							<ui:label/>
						</ui:cell>
						<ui:cell colspan="3">
							<ui:dateInput/>
						</ui:cell>
					</ui:formElement>
				</ui:row>

				<ui:row>
					<ui:formElement id="time">
						<ui:cell>
							<ui:label/>
						</ui:cell>
						<ui:cell colspan="3">
							<ui:timeInput/>								
						</ui:cell>
					</ui:formElement>
				</ui:row>
				
				<ui:row>
					<ui:formElement id="meetingroom">
						<ui:cell>
							<ui:label/>
						</ui:cell>
						<ui:cell colspan="3">
							<ui:select/>								
						</ui:cell>
					</ui:formElement>
				</ui:row>
						
				<ui:row>
					<ui:formElement id="attendees">
						<ui:cell>
							<ui:label/>
						</ui:cell>
						<ui:cell colspan="3">
							<ui:multiSelect/>								
						</ui:cell>
					</ui:formElement>
				</ui:row>
				
				
				</tui:componentForm>
				
				<tui:componentActions>
					<ui:eventButton labelId="common.Submit" eventId="submit" eventTarget="${formFullId}"/>
				</tui:componentActions>
			</tui:component>
		</ui:form>
	
		<ui:form id="form2">
			<tui:componentHeader>
				<tui:componentName><fmt:message key="seamless.bg.form"/></tui:componentName>
	        </tui:componentHeader>

			<tui:component>
				<tui:componentForm rowClasses="cols4" cellClasses="name, inpt">
					<ui:row>
						<ui:formElement id="futureDate">
							<ui:cell>
								<ui:label/>
							</ui:cell>
							<ui:cell colspan="3">
								<ui:dateInput/>								
							</ui:cell>
						</ui:formElement>
					</ui:row>
	
					<ui:row>
						<ui:formElement id="time">
							<ui:cell>
								<ui:label/>
							</ui:cell>
							<ui:cell colspan="3">
								<ui:timeInput/>								
							</ui:cell>
						</ui:formElement>
					</ui:row>
					
					<ui:row>
						<ui:formElement id="meetingroom">
							<ui:cell>
								<ui:label/>
							</ui:cell>
							<ui:cell colspan="3">
								<ui:select/>								
							</ui:cell>
						</ui:formElement>
					</ui:row>
							
					<ui:row>
						<ui:formElement id="attendees">
							<ui:cell>
								<ui:label/>
							</ui:cell>
							<ui:cell colspan="3">
								<ui:multiSelect/>								
							</ui:cell>
						</ui:formElement>
					</ui:row>

				</tui:componentForm>
				<tui:componentActions>
					<ui:eventButton labelId="common.Submit" eventId="submit" eventTarget="${formFullId}"/>
					<ui:eventButton labelId="common.Return" eventId="return"/>
				</tui:componentActions>
			</tui:component>
		</ui:form>

	</ui:widgetContext>
</jsp:root>
