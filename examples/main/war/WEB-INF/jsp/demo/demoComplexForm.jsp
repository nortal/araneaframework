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
					<!-- The label, defined in DemoComplexForm class. -->
					<fmt:message key="${contextWidget.data.formLabel}"/>
				</ui:componentName>
			</ui:componentHeader>

			<ui:component>

				<ui:componentForm>
					<ui:newRow>
						<ui:formElement id="beastSelection">
							<ui:newCell width="25%"/>
							<ui:newCell styleClass="name">
								<ui:label />
							</ui:newCell>
							<ui:newCell>
                            <!-- Note the updateRegions attribute - it specifies the regions
                                 that should be updated when event occurs (specifying this 
                                 attribute also means that upon listener activation AJAX
                                 request is made instead of ordinary HTTP request. -->
								<ui:select updateRegions="ajaxBeasts"/>
							</ui:newCell>
							<ui:newCell width="25%"/>
						</ui:formElement>
					</ui:newRow>
						
	                <!-- Here we define the update region. All tags inside the update region
	                    when AJAX request which update regions include "ajaxBeasts" is made. -->
					<ui:updateRegionRows id="ajaxBeasts">
						<!-- A way to test whether form elements are present. As both selectedBeastDesc 
							and concreteBeastControl are only added to the form if beast is selected,
							this needs to be done here. -->
						<c:if test="${not empty form.elements['concreteBeastControl']}">
							<ui:newRow>
								<ui:formElement id="selectedBeastDesc">
									<ui:newCell colspan="2" styleClass="wrap-centered" width="50%">
										<ui:textDisplay/>
									</ui:newCell>
								</ui:formElement>
			
								<ui:formElement id="concreteBeastControl">
									<ui:newCell styleClass="centered-name">
										<ui:label />
									</ui:newCell>
									<ui:newCell>
										<!-- Render MultiSelectControl with checkboxes. 
										     Instead ui:multiSelect could be used ... -->
										<ui:checkboxMultiSelect type="vertical" />
									</ui:newCell>
								</ui:formElement>
							</ui:newRow>
						</c:if>
					</ui:updateRegionRows>

				</ui:componentForm>

			</ui:component>

		</ui:form>

	</ui:widgetContext>
</jsp:root>
