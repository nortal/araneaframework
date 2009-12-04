<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" 
       xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template" 
       xmlns:c="http://java.sun.com/jsp/jstl/core"
       xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
      version="2.0">

	<ui:widgetContext>
		<!-- Label -->
		<tui:componentHeader>
			<tui:componentName>File upload demo</tui:componentName>
		</tui:componentHeader>
		<tui:component>
			<ui:form id="uploadForm">

				<!-- Body -->
				<tui:componentForm>
					<ui:row>
						<ui:cell styleClass="name">
							<ui:label id="select" />
						</ui:cell>
						<ui:cell styleClass="data">
							<ui:select id="select" />
						</ui:cell>
						<ui:formElement id="encodingTest">
							<ui:cell styleClass="name">
								<ui:label />
							</ui:cell>
							<ui:cell styleClass="data">
								<ui:textInput />
							</ui:cell>
						</ui:formElement>
					</ui:row>
					<ui:row>
						<ui:cell styleClass="name">
							<ui:fileUpload id="file" />
						</ui:cell>
						<ui:formElement id="upload">
							<ui:cell styleClass="data">
								<ui:button />
							</ui:cell>
						</ui:formElement>
					</ui:row>
				</tui:componentForm>
			</ui:form>

			<c:if test="${not empty widget.children['uploadList']}">
				<ui:list id="uploadList">
					<tui:componentList>
						<tui:componentListHeader />

						<ui:listRows>
							<ui:row>
								<ui:cell>
									<c:set var="method" value="AraneaPage.downloadFile('download','${widgetId}','${rowRequestId}');" />
									<jsp:element name="a">
										<jsp:attribute name="href">javascript:false;</jsp:attribute>
										<jsp:attribute name="onclick" trim="true">
											this.href = <c:out value="${method}"/>;
										</jsp:attribute>
										<jsp:body>
											<c:out value="${row.fileName}" />
										</jsp:body>
									</jsp:element>
									<ui:entity code="nbsp"/>
									<jsp:element name="a">
										<jsp:attribute name="href">javascript:false;</jsp:attribute>
										<jsp:attribute name="onclick" trim="true">
											var result = <c:out value="${method}"/>; if (result) window.open(result);
										</jsp:attribute>
										<jsp:body>
											(open in a popup)
										</jsp:body>
									</jsp:element>
								</ui:cell>
								<ui:cell>
									<c:out value="${row.size}" />
								</ui:cell>
								<ui:cell>
									<c:out value="${row.contentType}" />
								</ui:cell>
								<ui:cell>

								</ui:cell>
							</ui:row>
						</ui:listRows>

					</tui:componentList>
				</ui:list>
			</c:if>

			<!-- pure design tag -->
			<tui:componentActions>
				<ui:eventButton eventId="validate" labelId="#Validate" />
			</tui:componentActions>

		</tui:component>

	</ui:widgetContext>
</jsp:root>
