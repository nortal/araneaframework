<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	version="1.2">
	<ui:widgetContext>
		<ui:form id="complexForm">

			<ui:componentHeader>
				<ui:componentName>
					<!-- The label, defined on DemoComplexForm class. -->
					<fmt:message key="${contextWidget.data.formLabel}"/>
				</ui:componentName>
			</ui:componentHeader>

			<ui:component>

				<ui:componentForm>
					<ui:row>
						<ui:formElement id="beastSelection">
							<ui:cell width="25%"/>
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>
							<ui:cell>
								<ui:select updateRegions="ajaxBeasts"/>
							</ui:cell>
							<ui:cell width="25%"/>
						</ui:formElement>
					</ui:row>


						<!-- A way to test whether form elements are present. As both selectedBeastDesc 
							and concreteBeastControl are only added to the form if beast is selected,
							this needs to be done here. -->
						<ui:updateRegionRows id="ajaxBeasts">
							<c:if test="${not empty form.elements['concreteBeastControl']}">
								<ui:row>
									<ui:formElement id="selectedBeastDesc">
										<ui:cell colSpan="2" styleClass="wrap-centered" width="50%">
											<ui:textDisplay/>
										</ui:cell>
									</ui:formElement>
				
									<ui:formElement id="concreteBeastControl">
										<ui:cell styleClass="centered-name">
											<ui:label />
										</ui:cell>
										<ui:cell>
											<!-- Render MultiSelectControl with checkboxes. 
											     Instead ui:multiSelect could be used ... -->
											<ui:checkboxMultiSelect type="vertical" />
										</ui:cell>
									</ui:formElement>
								</ui:row>
							</c:if>
						</ui:updateRegionRows>

				</ui:componentForm>

				<ui:componentActions>
					<ui:eventButton eventId="save" labelId="#Add" />
					<ui:eventButton eventId="cancel" labelId="#Cancel" />
				</ui:componentActions>
			</ui:component>

		</ui:form>

	</ui:widgetContext>
</jsp:root>
